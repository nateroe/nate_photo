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

/**
 * Lookup keys for app properties.
 */
public enum AppPropertyKey {
	HANDOFF_DIRECTORY("import.HANDOFF_DIRECTORY"), //
	IS_USE_S3("import.IS_USE_S3"), //
	S3_ACCESS_KEY("import.S3_ACCESS_KEY"), //
	S3_SECRET_KEY("import.S3_SECRET_KEY"), //
	S3_BUCKET_NAME("import.S3_BUCKET_NAME"), //
	DESTINATION_DIRECTORY("import.DESTINATION_DIRECTORY"), //
	URL_PATH("import.URL_PATH"), //
	LARGE_WIDTH("import.LARGE_WIDTH"), //
	SMALL_WIDTH("import.SMALL_WIDTH");

	private String name;

	private AppPropertyKey(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
