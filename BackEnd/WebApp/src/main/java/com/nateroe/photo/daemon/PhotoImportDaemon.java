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
import java.util.Map;
import java.util.TimeZone;
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

import com.nateroe.photo.dao.PhotoDao;
import com.nateroe.photo.model.ImageResource;
import com.nateroe.photo.model.Photo;
import com.nateroe.photo.util.SystemUtil;

/**
 * The PhotoImportDaemon is a simplistic file-system based import utility.
 * 
 * @author nate
 */
@WebListener
public class PhotoImportDaemon implements ServletContextListener, Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(PhotoImportDaemon.class);

	/**
	 * Files to be imported are dumped in the handoff directory by the user
	 */
	private static final String HANDOFF_DIRECTORY = "/var/photo/handoff/";

	/**
	 * Results (scaled images and the imported original file) go in the destination directory
	 */
	private static final String DESTINATION_DIRECTORY = "/var/photo/www/";

	/**
	 * The files in the destination director are served at this URL path
	 */
	private static final String URL_PATH = "/res/";

	private static final int LARGE_WIDTH = 2048;
	private static final int SMALL_WIDTH = 256;

	@Resource(lookup = "java:jboss/ee/concurrency/scheduler/PhotoImportDaemonExecutor")
	private ManagedScheduledExecutorService scheduler;

	@EJB
	PhotoDao photoDao;

	private long lastPolled = 0;
	private ScheduledFuture<?> future;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		future = scheduler.scheduleAtFixedRate(this, 0, 10, TimeUnit.SECONDS);
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		future.cancel(true);
	}

	@Override
	public void run() {
		try {
			final long pollTime = System.currentTimeMillis();

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
			Path dir = Paths.get(HANDOFF_DIRECTORY);
			// SimpleDateFormat is not threadsafe so instantiate a new one every time.
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, filter)) {
				for (Path entry : stream) {
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("Found new file " + entry.toAbsolutePath().toString()
								+ " pollDate: " + dateFormatter.format(new Date(pollTime))
								+ " last polled: " + dateFormatter.format(new Date(lastPolled)));
					}
					try {
						acceptImage(entry);
					} catch (IOException ioe) {
						LOGGER.warn("Failed to import image " + entry, ioe);
					}
				}
			} catch (IOException x) {
				LOGGER.error("Failed", x);
			}

			// this is not threadsafe but there's only supposed to be one Daemon thread anyway.
			lastPolled = pollTime;
		} catch (Throwable t) {
			LOGGER.error("Failed", t);
		}
	}

	private void acceptImage(Path imagePath) throws IOException {
		long startTime = System.currentTimeMillis();

		File jpegFile = imagePath.toFile();

		// Read the first two bytes for JPEG magic number
		boolean isJpeg = false;
		try (RandomAccessFile raf = new RandomAccessFile(jpegFile, "r")) {
			isJpeg = raf.readShort() == (short) 0xFFD8;
		}

		if (isJpeg) {
			Photo photo = new Photo();
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

				photo.setTitle(tagMap.get("-Title"));
				photo.setDescription(tagMap.get("-Description"));
				photo.setRating(parseInt(tagMap.get("-Rating")));
				photo.setDate(parseDate(tagMap.get("-DateTimeOriginal")));
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

				// XXX for now, just auto-publish each photo on import
				photo.setPublished(true);

				LOGGER.info("Exposure time: {}", photo.getShutterSpeed());

				// =============
				// Process Image
				// =============
				BufferedImage image = ImageIO.read(jpegFile);
				int origWidth = image.getWidth();
				int origHeight = image.getHeight();

				// Generate scaled image resources for every power of 2 division between LARGE_WIDTH
				// and SMALL_WIDTH
				for (int maxDimension = LARGE_WIDTH; maxDimension >= SMALL_WIDTH; maxDimension /= 2) {
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

					String fileName = appendFileName(jpegFile.getName(), "_" + maxDimension);
					Path dest = Paths.get(DESTINATION_DIRECTORY + File.separator + fileName);
					encodeJpeg(scaledImage, 0.9f, dest.toFile());

					// copy only specific EXIF tags
					command = "exiftool -tagsFromFile " + imagePath
							+ " -Title -Description -Rating -DateTimeOriginal "
							+ "-Model -LensModel -FocalLength -ApproximateFocusDistance -FNumber -ExposureTime "
							+ "-ISO -Flash -Rights -UsageTerms -GPSLatitude -GPSLongitude -GPSAltitude -Subject "
							+ dest;
					SystemUtil.executeScript(command);

					ImageResource imageResource = new ImageResource();
					imageResource.setUrl(URL_PATH + fileName);
					imageResource.setWidth(width);
					imageResource.setHeight(height);
					photo.addImageResource(imageResource);

					// use this mipmap for next scaling operation
					image = scaledImage;
				}

				// Copy the original image to destination
				String fileName = appendFileName(jpegFile.getName(), "_orig");
				Path dest = Paths.get(DESTINATION_DIRECTORY + File.separator + fileName);
				FileUtils.copyFile(jpegFile, dest.toFile());

				// delete all EXIF tags from the new copy
				// (imported photos have only the subset of metadata that the app actually displays)
				command = "exiftool -all= " + dest;
				SystemUtil.executeScript(command);

				// copy only specific EXIF tags
				command = "exiftool -tagsFromFile " + imagePath
						+ " -Title -Description -Rating -DateTimeOriginal "
						+ "-Model -LensModel -FocalLength -ApproximateFocusDistance -FNumber -ExposureTime "
						+ "-ISO -Flash -Rights -UsageTerms -GPSLatitude -GPSLongitude -GPSAltitude -Subject "
						+ dest;
				SystemUtil.executeScript(command);

				ImageResource imageResource = new ImageResource();
				imageResource.setUrl(URL_PATH + fileName);
				imageResource.setWidth(origWidth);
				imageResource.setHeight(origHeight);
				photo.addImageResource(imageResource);

				// Persist the domain object
				photoDao.save(photo);

				// Delete original file last, if import completely succeeded.
				FileUtils.deleteQuietly(jpegFile);

				if (LOGGER.isInfoEnabled()) {
					float seconds = (float) (System.currentTimeMillis() - startTime) / 1000.0f;
					LOGGER.info("File import successful: {} in {}", fileName,
							String.format("%3.1fs", seconds));
				}
			} catch (Throwable t) {
				// If any exception, roll back the processed files
				String fileFilter = jpegFile.getName() + "_*";
				try (DirectoryStream<Path> stream = Files
						.newDirectoryStream(Paths.get(DESTINATION_DIRECTORY), fileFilter)) {
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
	 * 
	 * @param integerString
	 * @return
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
	 * 
	 * @param floatString
	 * @return
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
	 * 
	 * @param dateString
	 * @return
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

	private static String appendFileName(String name, String append) {
		int extensionStart = name.lastIndexOf(".");

		StringBuffer returnVal = new StringBuffer();
		returnVal.append(name.substring(0, extensionStart));
		returnVal.append(append);
		returnVal.append(".jpg");

		return returnVal.toString();
	}
}
