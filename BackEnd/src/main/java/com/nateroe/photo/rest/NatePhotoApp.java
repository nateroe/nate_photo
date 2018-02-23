package com.nateroe.photo.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationPath("/rest")
public class NatePhotoApp extends javax.ws.rs.core.Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(NatePhotoApp.class);

	@Override
	public Set<Class<?>> getClasses() {
		LOGGER.warn("Someone called getClasses()!!");

		Set<Class<?>> s = new HashSet<>();
		s.add(TaxaHandler.class);

		return s;
	}
}
