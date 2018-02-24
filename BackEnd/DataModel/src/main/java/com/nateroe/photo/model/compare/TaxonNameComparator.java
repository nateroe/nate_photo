package com.nateroe.photo.model.compare;

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

import java.util.Comparator;

import com.nateroe.photo.model.Taxon;

/**
 * Alphabetize taxa by name.
 * 
 * @author nate
 */
public class TaxonNameComparator implements Comparator<Taxon> {
	@Override
	public int compare(Taxon o1, Taxon o2) {
		return o1.getName().compareToIgnoreCase(o2.getName());
	}
}
