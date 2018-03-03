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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.iptc.IptcDirectory;
import com.nateroe.photo.dao.PhotoDao;
import com.nateroe.photo.model.ImageResource;
import com.nateroe.photo.model.Photo;

@WebListener
public class PhotoImportDaemon implements ServletContextListener, Runnable {
	private static final Logger logger = LoggerFactory.getLogger(PhotoImportDaemon.class);

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
					if (logger.isInfoEnabled()) {
						logger.info("Found new file " + entry.toAbsolutePath().toString()
								+ " pollDate: " + dateFormatter.format(new Date(pollTime))
								+ " last polled: " + dateFormatter.format(new Date(lastPolled)));
					}
					try {
						acceptImage(entry);
					} catch (IOException ioe) {
						logger.warn("Failed to import image " + entry, ioe);
					}
				}
			} catch (IOException x) {
				logger.error("Failed", x);
			}

			// this is not threadsafe but there's only supposed to be one Daemon thread anyway.
			lastPolled = pollTime;
		} catch (Throwable t) {
			logger.error("Failed", t);
		}
	}

	private void acceptImage(Path imagePath) throws ImageProcessingException, IOException {
		File jpegFile = imagePath.toFile();
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
				Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);

				ExifIFD0Directory ifd0Directory = metadata
						.getFirstDirectoryOfType(ExifIFD0Directory.class);
				ExifSubIFDDirectory subIfdDirectory = metadata
						.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
				IptcDirectory iptcDirectory = metadata.getFirstDirectoryOfType(IptcDirectory.class);

				photo.setTitle(iptcDirectory.getString(IptcDirectory.TAG_OBJECT_NAME));
				photo.setDescription(iptcDirectory.getString(IptcDirectory.TAG_CAPTION));
				// photo.setRating(...)
				photo.setDate(subIfdDirectory.getDate(ExifDirectoryBase.TAG_DATETIME_ORIGINAL));
				photo.setCamera(ifd0Directory.getString(ExifDirectoryBase.TAG_MODEL));
				photo.setLens(subIfdDirectory.getString(ExifDirectoryBase.TAG_LENS_MODEL));
				photo.setAperture(subIfdDirectory.getString(ExifDirectoryBase.TAG_APERTURE));
				photo.setShutterSpeed(
						subIfdDirectory.getString(ExifDirectoryBase.TAG_SHUTTER_SPEED));
				photo.setIso(subIfdDirectory.getString(ExifDirectoryBase.TAG_ISO_EQUIVALENT));
				photo.setFlashFired(subIfdDirectory.getString(ExifDirectoryBase.TAG_FLASH) != null
						&& !subIfdDirectory.getString(ExifDirectoryBase.TAG_FLASH)
								.contains("did not fire"));
				photo.setFocalLength(subIfdDirectory.getString(ExifDirectoryBase.TAG_FOCAL_LENGTH));
				// photo.setFocusDistance(...)
				photo.setCopyright(iptcDirectory.getString(IptcDirectory.TAG_COPYRIGHT_NOTICE));
				photo.setMakingOf(false);
				photo.setPublished(true);

				// =============
				// Process Image
				// =============
				BufferedImage image = ImageIO.read(jpegFile);

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
					encodeJpeg(scaledImage, 0.9f,
							new File(DESTINATION_DIRECTORY + File.separator + fileName));

					// XXX copy EXIF tags

					ImageResource imageResource = new ImageResource();
					imageResource.setUrl(URL_PATH + fileName);
					imageResource.setWidth(width);
					imageResource.setHeight(height);
					photo.addImageResource(imageResource);
				}

				// Move the original image to destination
				String fileName = appendFileName(jpegFile.getName(), "_orig");
				FileUtils.copyFile(jpegFile,
						new File(DESTINATION_DIRECTORY + File.separator + fileName));

				ImageResource imageResource = new ImageResource();
				imageResource.setUrl(URL_PATH + fileName);
				imageResource.setWidth(image.getWidth());
				imageResource.setHeight(image.getHeight());
				photo.addImageResource(imageResource);

				// Persist the domain object
				photoDao.save(photo);

				// Delete original file last, if import completely succeeded.
				FileUtils.deleteQuietly(jpegFile);

				logger.info("File import successful: {}", fileName);
			} catch (Throwable t) {
				// If any exception, roll back the processed files
				String fileFilter = jpegFile.getName() + "_*";
				try (DirectoryStream<Path> stream = Files
						.newDirectoryStream(Paths.get(DESTINATION_DIRECTORY), fileFilter)) {
					for (Path path : stream) {
						logger.info("Rolling back failed import. Delete file {}", path);
						Files.delete(path);
					}
				}

				throw t;
			}
		} else {
			throw new IOException("Not a JPEG file.");
		}
	}

	public static BufferedImage getScaledInstance(BufferedImage image, int targetWidth,
			int targetHeight) {
		int type = (image.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;

		BufferedImage returnVal = new BufferedImage(targetWidth, targetHeight, type);
		Graphics2D g2 = returnVal.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
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
