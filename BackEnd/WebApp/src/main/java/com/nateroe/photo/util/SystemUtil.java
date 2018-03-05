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

package com.nateroe.photo.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemUtil.class);

	public static String executeScript(String command) {
		String result = null;
		try {
			ProcessBuilder builder = new ProcessBuilder(command.split(" "));
			Process p = builder.start();
			try (InputStream is = p.getInputStream()) {
				int returnCode = p.waitFor();
				result = IOUtils.toString(is, "UTF-8");

				if (returnCode != 0) {
					LOGGER.debug("ERROR '{}' : {}", command, returnCode);
				}
			}
		} catch (InterruptedException | IOException ignore) {
			LOGGER.debug("Ignoring", ignore);
		}
		return result;
	}

}
