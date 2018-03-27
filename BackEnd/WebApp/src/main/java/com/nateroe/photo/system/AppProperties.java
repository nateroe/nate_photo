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
package com.nateroe.photo.system;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nateroe.photo.daemon.PhotoImportDaemon;

/**
 * This properties class and accompanying .properties file are used
 * to configure the webapp. Examples can be found in WebApp/conf.
 * 
 * @author nate
 */
public class AppProperties {
	private static final Logger LOGGER = LoggerFactory.getLogger(PhotoImportDaemon.class);

	private static AppProperties instance = null;

	private Properties properties;

	/**
	 * Singleton
	 */
	private AppProperties() {
		properties = new Properties();
	}

	public static void init() {
		instance = new AppProperties();

		// for now, the properties file is loaded from /opt/wildfly/natephoto.properties
		File file = new File("/opt/wildfly/natephoto.properties");
		if (file.exists()) {
			try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
				instance.properties.load(bis);
				LOGGER.info("AppProperties initialized.");
			} catch (IOException e) {
				LOGGER.warn("AppProperties not loaded.", e);
			}
		} else {
			LOGGER.warn("AppProperties not loaded. Not found: {}", file.getAbsolutePath());
		}
	}

	/**
	 * Not found logs a warning and returns false.
	 */
	public static boolean getBoolean(AppPropertyKey key) {
		boolean result = false;

		try {
			String string = instance.properties.getProperty(key.toString());
			if (string != null) {
				result = Boolean.parseBoolean(string);
			}
		} catch (Throwable t) {
			LOGGER.warn("Failed to parse Boolean property \"" + key + "\" (value: "
					+ instance.properties.getProperty(key.toString()) + ")", t);
		}

		return result;
	}

	/**
	 * Not found logs a warning and returns 0.
	 */
	public static int getInt(AppPropertyKey key) {
		int result = 0;

		try {
			String string = instance.properties.getProperty(key.toString());
			if (string != null) {
				result = Integer.parseInt(string);
			}
		} catch (Throwable t) {
			LOGGER.warn("Failed to parse Integer property \"" + key + "\" (value: "
					+ instance.properties.getProperty(key.toString()) + ")", t);
		}

		return result;
	}

	/**
	 * Not found returns null.
	 */
	public static String getString(AppPropertyKey key) {
		return instance.properties.getProperty(key.toString());
	}
}
