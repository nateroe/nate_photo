package com.nateroe.photo.rest.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.nateroe.photo.dao.PhotoDao;
import com.nateroe.photo.model.ImageResource;
import com.nateroe.photo.model.Photo;
import com.nateroe.test.AbstractBaseTest;

/**
 * Tests REST request handler. Mostly this amounts to testing that
 * PhotoHandler.sanitizeResources(...) gets called.
 * 
 * @author nate
 */
public class PhotoHandlerTest extends AbstractBaseTest {
	@Mock
	PhotoDao photoDao;

	PhotoHandler photoHandler;

	@Before
	public void setUp() {
		photoHandler = new PhotoHandler(photoDao);
	}

	@Test
	public void testGetPhotoById() {
		// Set up the EntityManager to return a Photo
		Photo testPhoto = generatePhoto();
		Mockito.when(photoDao.findByPrimaryKey(testPhoto.getId())).thenReturn(testPhoto);

		// Operation under test
		Photo result = photoHandler.getPhotoById(testPhoto.getId());

		// Verify that it called to the database
		Mockito.verify(photoDao).findByPrimaryKey(testPhoto.getId());
		// Assert the result matches expected
		Assert.assertSame(testPhoto, result);
		// Validate the Photo's ImageResources
		validateResources(result);
	}

	@Test
	public void testGetAllPhotos() {
		// Set up the EntityManager to return a List<Photo>
		List<Photo> testPhotos = generatePhotos(20);
		Mockito.when(photoDao.findAll()).thenReturn(testPhotos);

		// Operation under test
		List<Photo> result = photoHandler.getAllPhotos();

		// Verify that it called to the database
		Mockito.verify(photoDao).findAll();
		// Assert the result matches expected
		Assert.assertSame(testPhotos, result);
		// Validate each Photo's ImageResources
		validateResources(result);
	}

	@Test
	public void testGetBestPhotos() {
		// Set up the EntityManager to return a List<Photo>
		List<Photo> testPhotos = generatePhotos(20);
		Mockito.when(photoDao.findAllHighlights()).thenReturn(testPhotos);

		// Operation under test
		List<Photo> result = photoHandler.getBestPhotos();

		// Verify that it called to the database
		Mockito.verify(photoDao).findAllHighlights();
		// Assert the result matches expected
		Assert.assertSame(testPhotos, result);
		// Validate each Photo's ImageResources
		validateResources(result);
	}

	@Test
	public void testGetPhotosByExpedition() {
		// Set up the EntityManager to return a List<Photo>
		List<Photo> testPhotos = generatePhotos(20);
		Mockito.when(photoDao.findByExpeditionId(1L)).thenReturn(testPhotos);

		// Operation under test
		List<Photo> result = photoHandler.getPhotosByExpedition(1);

		// Verify that it called to the database
		Mockito.verify(photoDao).findByExpeditionId(1L);
		// Assert the result matches expected
		Assert.assertSame(testPhotos, result);
		// Validate each Photo's ImageResources
		validateResources(result);
	}

	@Test
	public void testGetPhotosByExpeditionHighlight() {
		// Set up the EntityManager to return a List<Photo>
		List<Photo> testPhotos = generatePhotos(5);
		Mockito.when(photoDao.findHighlightsByExpeditionId(1L)).thenReturn(testPhotos);

		// Operation under test
		List<Photo> result = photoHandler.getPhotosByExpeditionHighlight(1);

		// Verify that it called to the database
		Mockito.verify(photoDao).findHighlightsByExpeditionId(1L);
		// Assert the result matches expected
		Assert.assertSame(testPhotos, result);
		// Validate each Photo's ImageResources
		validateResources(result);
	}

	/**
	 * Assert that none of this Photo's resources exceed PhotoHandler.MAX_RES
	 */
	private void validateResources(Photo photo) {
		for (ImageResource resource : photo.getImageResources()) {
			Assert.assertTrue(resource.getWidth() <= PhotoHandler.MAX_RES
					&& resource.getHeight() <= PhotoHandler.MAX_RES);
		}
	}

	private void validateResources(List<Photo> photos) {
		for (Photo photo : photos) {
			validateResources(photo);
		}
	}

	/**
	 * Create a Photo object with randomized values, with ImageResources in several sizes
	 * less than, equal to, and greater than PhotoHandler.MAX_RES
	 */
	private Photo generatePhoto() {
		Photo result = new Photo();
		result.setId(randomLong(Long.MAX_VALUE));
		result.setTitle(randomString());
		result.setDescription(randomString());
		result.setRating(randomInt(5) + 1);
		result.setDate(new Date());
		result.setCamera(randomString());
		result.setLens(randomString());
		result.setAperture(randomString());
		result.setShutterSpeed(randomString());
		result.setIso(randomString());
		result.setFlash(randomInt(256));
		result.setFocalLength(randomFloat(Float.MAX_VALUE));
		result.setFocusDistance(randomFloat(Float.MAX_VALUE));
		result.setCopyright(randomString());
		result.setUsageTerms(randomString());
		result.setPublished(randomInt(2) > 0);

		// Create ImageResources in several sizes less than, equal to, and greater than
		// PhotoHandler.MAX_RES
		for (int i = 0; i < 5; i++) {
			ImageResource resource = new ImageResource();
			resource.setWidth(
					(int) Math.round((double) PhotoHandler.MAX_RES / Math.pow(2.0, i - 2)));
			resource.setHeight((int) Math.round((double) resource.getWidth() * 0.666));
			resource.setUrl(randomString());
			resource.setFileName(randomString());

			result.addImageResource(resource);
		}

		return result;
	}

	/**
	 * Generate a List of Photos
	 */
	private List<Photo> generatePhotos(int count) {
		List<Photo> result = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			result.add(generatePhoto());
		}
		return result;
	}
}
