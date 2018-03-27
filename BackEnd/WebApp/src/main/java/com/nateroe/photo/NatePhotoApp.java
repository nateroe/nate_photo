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

package com.nateroe.photo;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nateroe.photo.rest.RestHandler;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

/**
 * Tell JAX-RS about our rest handlers.
 * 
 * @author nate
 */
@ApplicationPath("/rest")
public class NatePhotoApp extends javax.ws.rs.core.Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(NatePhotoApp.class);

	@Override
	public Set<Class<?>> getClasses() {
		LOGGER.debug("getClasses()");

		// Scan for all classes annotated @RestHandler in any "com.nateroe..." package
		Set<Class<?>> s = new HashSet<>();
		new FastClasspathScanner("com.nateroe").matchClassesWithAnnotation(RestHandler.class, c -> {
			s.add(c);
			LOGGER.debug("Found REST handler {}", c.getName());
		}).scan();

		return s;
	}
}
