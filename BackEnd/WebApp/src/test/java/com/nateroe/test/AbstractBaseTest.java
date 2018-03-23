package com.nateroe.test;

import java.util.UUID;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

public class AbstractBaseTest {
	@Before
	public void initMockito() {
		MockitoAnnotations.initMocks(this);
	}

	protected static int randomInt(int max) {
		return (int) Math.floor(Math.random() * (double) max);
	}

	protected static long randomLong(long max) {
		return (long) Math.floor(Math.random() * (double) max);
	}

	protected static float randomFloat(float max) {
		return (float) Math.floor(Math.random() * max);
	}

	protected static String randomString() {
		return UUID.randomUUID().toString();
	}
}
