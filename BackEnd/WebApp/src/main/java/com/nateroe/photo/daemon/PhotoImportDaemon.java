/**
 * NatePhoto - A photo catalog and presentation application.
 * Copyright (C) 2018 Nathaniel Roe
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact nate [at] nateroe [dot] com
 */

package com.nateroe.photo.daemon;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nateroe.photo.dao.ExpeditionDao;
import com.nateroe.photo.dao.PhotoDao;
import com.nateroe.photo.model.Expedition;
import com.nateroe.photo.model.ImageResource;
import com.nateroe.photo.model.Photo;
import com.nateroe.photo.system.AppProperties;
import com.nateroe.photo.system.AppPropertyKey;
import com.nateroe.photo.util.SystemUtil;

/**
 * The PhotoImportDaemon is a simplistic file-system based import utility.
 * 
 * @author nate
 */
@WebListener
public class PhotoImportDaemon implements ServletContextListener, Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(PhotoImportDaemon.class);

	@Resource(lookup = "java:jboss/ee/concurrency/scheduler/PhotoImportDaemonExecutor")
	private ManagedScheduledExecutorService scheduler;

	@EJB
	PhotoDao photoDao;
	@EJB
	ExpeditionDao expeditionDao;

	private long lastPolled = 0;
	private ScheduledFuture<?> future;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		future = scheduler.scheduleAtFixedRate(this, 10, 20, TimeUnit.SECONDS);
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		future.cancel(true);
	}

	@Override
	public void run() {
		try {
			final long pollTime = System.currentTimeMillis();
			LOGGER.trace("--------- BEGIN POLLING ---------");

			// Filter for files modified after the last poll time and before this poll time.
			DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
				@Override
				public boolean accept(Path file) throws IOException {
					boolean returnVal;

					try {
						BasicFileAttributes attr = Files.readAttributes(file,
								BasicFileAttributes.class);
						returnVal = attr.creationTime().toMillis() >= lastPolled
								&& attr.creationTime().toMillis() < pollTime;
					} catch (IOException x) {
						returnVal = false;
					}

					return returnVal;
				}
			};

			// Stream the delivery directory's contents and filter
			Path dir = Paths.get(AppProperties.getString(AppPropertyKey.HANDOFF_DIRECTORY));
			// SimpleDateFormat is not threadsafe so instantiate a new one every time.
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			boolean hasDoneWork = false;
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, filter)) {
				for (Path entry : stream) {
					hasDoneWork = true;
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Found new file " + entry.toAbsolutePath().toString()
								+ " pollDate: " + dateFormatter.format(new Date(pollTime))
								+ " last polled: " + dateFormatter.format(new Date(lastPolled)));
					}
					try {
						if (entry.toFile().isDirectory()) {
							// import directory as an expedition
							acceptExpedition(entry);
						} else if (entry.toFile().isFile()) {
							// import file as an image
							acceptImage(entry, null);
						} // else ignore
					} catch (IOException ioe) {
						LOGGER.warn("Failed to import image " + entry, ioe);
					}
				}
			} catch (IOException x) {
				LOGGER.error("Failed", x);
			}

			if (hasDoneWork) {
				// even when you tell it -overwrite_original, sometimes
				// exiftool leaves "*_original" files behind,
				// so if we did any image importing, then delete all
				// such files from the destination directory
				try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(
						Paths.get(AppProperties.getString(AppPropertyKey.DESTINATION_DIRECTORY)),
						"*_original")) {
					for (Path path : dirStream) {
						delete(path);
					}
				}

				// Log time elapsed when any files processed
				if (LOGGER.isDebugEnabled()) {
					String timeElapsed = String.format("%3.1f",
							((System.currentTimeMillis() - pollTime) / 1000.0f));
					LOGGER.debug("Import completed in {}", timeElapsed);
				}
			}

			// this is not threadsafe but there's only supposed to be one daemon thread anyway.
			lastPolled = pollTime;
			LOGGER.trace("--------- END POLLING ---------");
		} catch (Throwable t) {
			LOGGER.error("Failed", t);
		}
	}

	/**
	 * Import an Expedition (ie, a directory filled with photos)
	 */
	private void acceptExpedition(Path expeditionPath) throws IOException {
		// If present in the expedition folder, expedition.json will be read
		File jsonFile = expeditionPath.resolve("expedition.json").toFile();

		Expedition expedition = null;
		if (jsonFile.exists()) {
			try {
				String json = new String(Files.readAllBytes(jsonFile.toPath()), "UTF-8");
				Gson gson = new GsonBuilder().create();
				expedition = gson.fromJson(json, Expedition.class);

				LOGGER.debug("Read expedition.json:\n{}", json);
			} catch (Throwable ignore) {
				LOGGER.debug("Failed to read file " + jsonFile, ignore);
			}
		} else {
			LOGGER.debug("File not found: {}", jsonFile);
		}

		if (expedition == null) {
			expedition = new Expedition();
			expedition.setTitle(expeditionPath.getFileName().toString());
		}
		expedition.setSystemName(expeditionPath.getFileName().toString());

		Expedition existingExpedition = expeditionDao.findByNames(expedition.getSystemName());
		if (existingExpedition != null) {
			existingExpedition.copyFrom(expedition);
			expedition = existingExpedition;
		}
		expedition = expeditionDao.saveOrUpdate(expedition);

		// If we imported the expedition okay (haven't thrown so far)
		FileUtils.deleteQuietly(jsonFile);

		int failCount = 0;
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(expeditionPath)) {
			for (Path entry : stream) {
				try {
					if (entry.toFile().isFile()) {
						// import as an image, updating the Expedition if necessary
						acceptImage(entry, expedition);
					} // else ignore
				} catch (IOException ioe) {
					failCount++;
					LOGGER.warn("Failed to import image " + entry, ioe);
				}
			}
		}

		// persist any changes to the Expedition
		expeditionDao.saveOrUpdate(expedition);

		// If everything went well
		if (failCount == 0) {
			// Delete the entire expedition from the handoff area
			FileUtils.deleteQuietly(expeditionPath.toFile());
		}
	}

	/**
	 * Import an image from the given image path, joining it to the Expedition if specified.
	 * Modifies the begin/end dates of the Expedition (if any) if necessary based on this photo's
	 * date, if any.
	 * 
	 * @param imagePath
	 * @param expedition
	 *            nullable, if not null begin/end dates MAY be updated by acceptImage(...)
	 * @throws IOException
	 */
	private void acceptImage(Path imagePath, Expedition expedition) throws IOException {
		long startTime = System.currentTimeMillis();

		File jpegFile = imagePath.toFile();
		LOGGER.debug("accept image: {}", jpegFile);

		// Read the first two bytes for JPEG magic number
		boolean isJpeg = false;
		try (RandomAccessFile raf = new RandomAccessFile(jpegFile, "r")) {
			isJpeg = raf.readShort() == (short) 0xFFD8;
		}

		if (isJpeg) {
			try {
				// =========
				// Read EXIF
				// =========
				// Note: I tried a couple Java-based photo metadata libraries but neither
				// found all the tags I wanted to import, so ExifTool it is.
				String command = "exiftool -args -n -Title -Description -Rating -DateTimeOriginal "
						+ "-Model -LensModel -FocalLength -ApproximateFocusDistance -FNumber -ExposureTime "
						+ "-ISO -Flash -Rights -UsageTerms -GPSLatitude -GPSLongitude -GPSAltitude -Subject ";
				command += imagePath;
				String exifString = SystemUtil.executeScript(command);

				LOGGER.trace("Ran command:\n{}\nProduced Results:\n{}\n", command, exifString);

				Pattern.compile("\n").splitAsStream(exifString);
				Map<String, String> tagMap = new HashMap<>();
				try (Stream<String> stream = Pattern.compile("\n").splitAsStream(exifString)) {
					stream.forEach(line -> {
						if (line.length() > 2) {
							String[] pieces = line.split("=");
							if (pieces.length == 2) {
								tagMap.put(pieces[0].trim(), pieces[1].trim());
							} else {
								LOGGER.warn("Cannot parse invalid EXIFTool result: {}", line);
							}
						}
					});
				}

				Date date = parseDate(tagMap.get("-DateTimeOriginal"));

				Photo photo = photoDao.findByOrigFilename(imagePath.getFileName().toString(), date);
				List<ImageResource> oldResources = new LinkedList<>();
				if (photo == null) {
					photo = new Photo();
				} else {
					// We are re-importing an existing photo so we need to remove the old images
					// we don't want to delete them here because processing has not yet occurred,
					// but we need to cache them and then clear the list of resources so it will
					// only contain new images
					oldResources.addAll(photo.getImageResources());
					photo.getImageResources().clear();
				}
				photo.setOrigFileName(imagePath.getFileName().toString());

				photo.setTitle(tagMap.get("-Title"));
				photo.setDescription(tagMap.get("-Description"));
				photo.setRating(parseInt(tagMap.get("-Rating")));
				photo.setDate(date);
				photo.setCamera(tagMap.get("-Model"));
				photo.setLens(tagMap.get("-LensModel"));
				photo.setAperture(tagMap.get("-FNumber"));

				Float shutterSpeed = parseFloat(tagMap.get("-ExposureTime"));
				if (shutterSpeed != null) {
					Integer roundFraction = Math.round(1.0f / shutterSpeed);
					if (roundFraction > 1) {
						photo.setShutterSpeed("1/" + roundFraction);
					} else {
						photo.setShutterSpeed(Integer.toString(Math.round(shutterSpeed)));
					}
				} else {
					photo.setShutterSpeed(null);
				}

				photo.setIso(tagMap.get("-ISO"));
				photo.setFlash(parseInt(tagMap.get("-Flash")));
				photo.setFocalLength(parseFloat(tagMap.get("-FocalLength")));
				photo.setFocusDistance(parseFloat(tagMap.get("-ApproximateFocusDistance")));
				if (photo.getFocusDistance() != null && photo.getFocusDistance() > 100000) {
					// if the focus distance is very large (> 100km) it's probably being
					// misinterpreted (for some photos I get values such as 4294967300 m)
					// in these cases, "null" is closer to correct.
					photo.setFocusDistance(null);
				}

				photo.setLatitude(parseFloat("-GPSLatitude"));
				photo.setLongitude(parseFloat("-GPSLongitude"));
				photo.setAltitude(parseFloat("-GPSAltitude"));
				photo.setCopyright(tagMap.get("-Rights"));
				photo.setUsageTerms(tagMap.get("-UsageTerms"));
				photo.setExpeditionId(expedition != null ? expedition.getId() : null);

				// XXX for now, just auto-publish each photo on import
				photo.setPublished(true);

				// Maybe update the Expedition if new begin or end date
				if (expedition != null) {
					if (photo.getDate() != null && (expedition.getBeginDate() == null
							|| photo.getDate().before(expedition.getBeginDate()))) {
						expedition.setBeginDate(photo.getDate());
					}
					if (photo.getDate() != null && (expedition.getEndDate() == null
							|| photo.getDate().after(expedition.getEndDate()))) {
						expedition.setEndDate(photo.getDate());
					}
				}

				// =============
				// Process Image
				// =============
				BufferedImage image = ImageIO.read(jpegFile);
				int origWidth = image.getWidth();
				int origHeight = image.getHeight();

				// Generate scaled image resources for every power of 2 division between LARGE_WIDTH
				// and SMALL_WIDTH
				final int largeWidth = AppProperties.getInt(AppPropertyKey.LARGE_WIDTH);
				final int smallWidth = AppProperties.getInt(AppPropertyKey.SMALL_WIDTH);
				for (int maxDimension = largeWidth; maxDimension >= smallWidth; maxDimension /= 2) {
					// constrain the largest dimension to a fixed value and
					// scale the other dimension to maintain aspect
					double scalingFactor;
					int width, height;
					if (image.getWidth() > image.getHeight()) {
						width = maxDimension;
						scalingFactor = (double) width / (double) image.getWidth();
						height = (int) (image.getHeight() * scalingFactor);

					} else {
						height = maxDimension;
						scalingFactor = (double) height / (double) image.getHeight();
						width = (int) (image.getWidth() * scalingFactor);
					}

					BufferedImage scaledImage = getScaledInstance(image, width, height);

					String fileName = generateFileName();
					Path dest = Paths
							.get(AppProperties.getString(AppPropertyKey.DESTINATION_DIRECTORY)
									+ File.separator + fileName);
					encodeJpeg(scaledImage, 0.9f, dest.toFile());

					// copy only specific EXIF tags
					command = "exiftool -overwrite_original -tagsFromFile " + imagePath
							+ " -Title -Description -Rating -DateTimeOriginal "
							+ "-Model -LensModel -FocalLength -ApproximateFocusDistance -FNumber -ExposureTime "
							+ "-ISO -Flash -Rights -UsageTerms -GPSLatitude -GPSLongitude -GPSAltitude -Subject "
							+ dest;
					SystemUtil.executeScript(command);

					String url;
					if (AppProperties.getBoolean(AppPropertyKey.IS_USE_S3)) {
						// upload mipmap to S3
						uploadImage(dest.toFile());
						FileUtils.deleteQuietly(dest.toFile());
						url = AppProperties.getString(AppPropertyKey.URL_PATH)
								+ AppProperties.getString(AppPropertyKey.S3_BUCKET_NAME) + "/"
								+ fileName;
					} else {
						url = AppProperties.getString(AppPropertyKey.URL_PATH) + fileName;
					}

					ImageResource imageResource = new ImageResource();
					imageResource.setUrl(url);
					imageResource.setWidth(width);
					imageResource.setHeight(height);
					photo.addImageResource(imageResource);

					// use this mipmap for next scaling operation
					image = scaledImage;
				}

				// Copy the original image to destination
				String fileName = generateFileName();
				Path dest = Paths.get(AppProperties.getString(AppPropertyKey.DESTINATION_DIRECTORY)
						+ File.separator + fileName);
				FileUtils.copyFile(jpegFile, dest.toFile());

				// delete all EXIF tags from the new copy
				// (imported photos have only the subset of metadata that the app actually displays)
				command = "exiftool -all= " + dest;
				SystemUtil.executeScript(command);

				// copy only specific EXIF tags
				command = "exiftool -overwrite_original -tagsFromFile " + imagePath
						+ " -Title -Description -Rating -DateTimeOriginal "
						+ "-Model -LensModel -FocalLength -ApproximateFocusDistance -FNumber -ExposureTime "
						+ "-ISO -Flash -Rights -UsageTerms -GPSLatitude -GPSLongitude -GPSAltitude -Subject "
						+ dest;
				SystemUtil.executeScript(command);

				String url;
				if (AppProperties.getBoolean(AppPropertyKey.IS_USE_S3)) {
					// upload mipmap to S3
					uploadImage(dest.toFile());
					FileUtils.deleteQuietly(dest.toFile());
					url = AppProperties.getString(AppPropertyKey.URL_PATH)
							+ AppProperties.getString(AppPropertyKey.S3_BUCKET_NAME) + "/"
							+ fileName;
				} else {
					url = AppProperties.getString(AppPropertyKey.URL_PATH) + fileName;
				}

				ImageResource imageResource = new ImageResource();
				imageResource.setUrl(url);
				imageResource.setWidth(origWidth);
				imageResource.setHeight(origHeight);
				photo.addImageResource(imageResource);

				// Persist the domain object
				photoDao.saveOrUpdate(photo);

				// Delete original file last, if import completely succeeded.
				FileUtils.deleteQuietly(jpegFile);

				// at this point, delete all old (original) resources
				for (ImageResource resource : oldResources) {
					String pathString = AppProperties
							.getString(AppPropertyKey.DESTINATION_DIRECTORY) + File.separator
							+ resource.getFileName();
					delete(Paths.get(pathString));
				}

				if (LOGGER.isInfoEnabled()) {
					float seconds = (float) (System.currentTimeMillis() - startTime) / 1000.0f;
					LOGGER.info("File import successful: {} in {}s", fileName,
							String.format("%3.1fs", seconds));
				}
			} catch (Throwable t) {
				// If any exception, roll back the processed files
				String fileFilter = jpegFile.getName() + "_*";
				try (DirectoryStream<Path> stream = Files.newDirectoryStream(
						Paths.get(AppProperties.getString(AppPropertyKey.DESTINATION_DIRECTORY)),
						fileFilter)) {
					for (Path path : stream) {
						LOGGER.info("Rolling back failed import. Delete file {}", path);
						Files.delete(path);
					}
				}

				throw t;
			}
		} else {
			throw new IOException("Not a JPEG file.");
		}
	}

	/**
	 * Try to parse, returning null on failure (failing silently)
	 */
	private static Integer parseInt(String integerString) {
		Integer result = null;
		try {
			result = Integer.parseInt(integerString);
		} catch (Exception ignore) {
		}
		return result;
	}

	/**
	 * Try to parse, returning null on failure (failing silently)
	 */
	private static Float parseFloat(String floatString) {
		Float result = null;
		try {
			result = Float.parseFloat(floatString);
		} catch (Exception ignore) {
		}
		return result;
	}

	/**
	 * Try to parse, returning null on failure (failing silently)
	 */
	private static Date parseDate(String dateString) {
		Date result = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
			// XXX not sure what to do about the timezone, I don't see it in the ExifTool results.
			// XXX my camera is pretty much always set to PDT (even in winter) so that's what I'm
			// using for now...
			formatter.setTimeZone(TimeZone.getTimeZone("PDT"));
			result = formatter.parse(dateString);
		} catch (Exception ignore) {
		}
		return result;
	}

	/**
	 * Create a scaled version of the given BufferedImage
	 */
	private static BufferedImage getScaledInstance(BufferedImage image, int targetWidth,
			int targetHeight) {
		int type = (image.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;

		BufferedImage returnVal = new BufferedImage(targetWidth, targetHeight, type);
		Graphics2D g2 = returnVal.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(image, 0, 0, targetWidth, targetHeight, null);
		g2.dispose();

		return returnVal;
	}

	/**
	 * Encode a BufferedImage with JPEG using the specified quality factor
	 */
	private static void encodeJpeg(BufferedImage image, float quality, File file)
			throws IOException {
		JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
		jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpegParams.setCompressionQuality(quality);

		ImageWriter writer = ImageIO.getImageWritersByFormatName("JPEG").next();
		ImageOutputStream ios = ImageIO.createImageOutputStream(file);
		writer.setOutput(ios);
		writer.write(null, new IIOImage(image, null, null), jpegParams);
	}

	/**
	 * Generate a filename from a new random UUID
	 */
	private static String generateFileName() {
		String result;
		do {
			result = UUID.randomUUID().toString() + ".jpg";
		} while (new File(result).exists());
		return result;
	}

	/**
	 * Quietly attempt to delete the given regular file
	 */
	private static void delete(Path path) {
		File file = path.toFile();
		if (file.exists()) {
			FileUtils.deleteQuietly(file);
		}
	}

	/**
	 * Upload the given file to S3
	 */
	private static void uploadImage(File file) {
		try {
			// XXX cache the S3 instance
			final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_2)
					.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
							AppProperties.getString(AppPropertyKey.S3_ACCESS_KEY),
							AppProperties.getString(AppPropertyKey.S3_SECRET_KEY))))
					.build();
			s3.putObject(AppProperties.getString(AppPropertyKey.S3_BUCKET_NAME), file.getName(),
					file);
			LOGGER.info("Saved to S3: {}", file.getName());
		} catch (AmazonServiceException e) {
			LOGGER.warn("Failed to save to S3", e);
		}
	}
}
