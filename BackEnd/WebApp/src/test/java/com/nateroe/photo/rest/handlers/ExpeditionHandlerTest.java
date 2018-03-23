package com.nateroe.photo.rest.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.nateroe.photo.dao.ExpeditionDao;
import com.nateroe.photo.model.Expedition;
import com.nateroe.test.AbstractBaseTest;

public class ExpeditionHandlerTest extends AbstractBaseTest {
	@Mock
	ExpeditionDao expeditionDao;

	ExpeditionHandler expeditionHandler;

	@Before
	public void setUp() {
		expeditionHandler = new ExpeditionHandler(expeditionDao);
	}

	@Test
	public void testGetExpeditionById() {
		// Set up the EntityManager to return a Photo
		Expedition testExpedition = generateExpedition();
		Mockito.when(expeditionDao.findByPrimaryKey(testExpedition.getId()))
				.thenReturn(testExpedition);

		// Operation under test
		Expedition result = expeditionHandler.getExpeditionById(testExpedition.getId());

		// Verify that it called to the database
		Mockito.verify(expeditionDao).findByPrimaryKey(testExpedition.getId());
		// Assert the result matches expected
		Assert.assertSame(testExpedition, result);
	}

	@Test
	public void testGetAllExpeditions() {
		// Set up the EntityManager to return a List<Photo>
		List<Expedition> testExpeditions = generateExpeditions(20);
		Mockito.when(expeditionDao.findAll()).thenReturn(testExpeditions);

		// Operation under test
		List<Expedition> result = expeditionHandler.getAllExpeditions();

		// Verify that it called to the database
		Mockito.verify(expeditionDao).findAll();
		// Assert the result matches expected
		Assert.assertSame(testExpeditions, result);
	}

	private Expedition generateExpedition() {
		Expedition result = new Expedition();
		result.setId(randomLong(Long.MAX_VALUE));
		result.setTitle(randomString());
		result.setDescription(randomString());
		result.setBeginDate(new Date());
		result.setEndDate(new Date());

		return result;
	}

	private List<Expedition> generateExpeditions(int count) {
		List<Expedition> result = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			result.add(generateExpedition());
		}
		return result;
	}
}
