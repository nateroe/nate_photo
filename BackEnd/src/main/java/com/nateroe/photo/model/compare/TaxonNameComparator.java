package com.nateroe.photo.model.compare;

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
