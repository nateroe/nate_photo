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

package com.nateroe.photo.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

/**
 * 
 * @author nate
 */
@XmlRootElement
public class Photo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "increment")
	@SequenceGenerator(name = "increment", sequenceName = "Photo_pk_seq")
	private Long id;
	private String title;
	private String description;
	private Integer rating;
	private Integer width;
	private Integer height;
	private String path;
	private Date date;
	private String mapUrl;
	private String camera;
	private String lens;
	private String aperture;
	private String shutterSpeed;
	private String iso;
	private Boolean isFlashFired;
	private Integer focusDistance; // in whole decimeters
	private String copyright;
	private Boolean isMakingOf;
	private Boolean isPublished;
	private final AtomicLong visitCount;
	private final AtomicLong favoriteCount;

	private Expedition expedition;
	private Taxon taxon;
	private Set<Tag> tags = new HashSet<>();

	public Photo() {
		visitCount = new AtomicLong(0);
		favoriteCount = new AtomicLong(0);
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMapUrl() {
		return mapUrl;
	}

	public void setMapUrl(String mapUrl) {
		this.mapUrl = mapUrl;
	}

	public String getCamera() {
		return camera;
	}

	public void setCamera(String camera) {
		this.camera = camera;
	}

	public String getLens() {
		return lens;
	}

	public void setLens(String lens) {
		this.lens = lens;
	}

	public String getAperture() {
		return aperture;
	}

	public void setAperture(String aperture) {
		this.aperture = aperture;
	}

	public String getShutterSpeed() {
		return shutterSpeed;
	}

	public void setShutterSpeed(String shutterSpeed) {
		this.shutterSpeed = shutterSpeed;
	}

	public String getIso() {
		return iso;
	}

	public void setIso(String iso) {
		this.iso = iso;
	}

	public Boolean isFlashFired() {
		return isFlashFired;
	}

	public void setFlashFired(Boolean isFlashFired) {
		this.isFlashFired = isFlashFired;
	}

	public Integer getFocusDistance() {
		return focusDistance;
	}

	public void setFocusDistance(Integer focusDistance) {
		this.focusDistance = focusDistance;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public Expedition getExpedition() {
		return expedition;
	}

	public void setExpedition(Expedition expedition) {
		this.expedition = expedition;
	}

	public Taxon getTaxon() {
		return taxon;
	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	public Boolean isMakingOf() {
		return isMakingOf;
	}

	public void setMakingOf(Boolean isMakingOf) {
		this.isMakingOf = isMakingOf;
	}

	public Boolean isPublished() {
		return isPublished;
	}

	public void setPublished(Boolean isPublished) {
		this.isPublished = isPublished;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void addTag(Tag tag) {
		this.tags.add(tag);
	}

	/**
	 * Threadsafe
	 * 
	 * @return the new value
	 */
	public long incrementVisitCount() {
		return visitCount.incrementAndGet();
	}

	public long getVisitCount() {
		return visitCount.get();
	}

	public void setVisitCount(long count) {
		visitCount.set(count);
	}

	/**
	 * Threadsafe
	 * 
	 * @return the new value
	 */
	public long incrementFavoriteCount() {
		return favoriteCount.incrementAndGet();
	}

	public long getFavoriteCount() {
		return favoriteCount.get();
	}

	public void setFavoriteCount(long count) {
		favoriteCount.set(count);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId(), title, description, rating, width, height, path, date,
				mapUrl, camera, lens, aperture, shutterSpeed, iso, isFlashFired, focusDistance,
				copyright, expedition, taxon, isMakingOf, isPublished, tags);
	}

	@Override
	public boolean equals(Object obj) {
		boolean returnVal;

		if (obj == null) {
			returnVal = false;
		} else if (getClass() != obj.getClass()) {
			returnVal = false;
		} else {
			Photo other = (Photo) obj;

			returnVal = Objects.equal(this.getId(), other.getId()) //
					&& Objects.equal(this.title, other.title) //
					&& Objects.equal(this.description, other.description) //
					&& Objects.equal(this.rating, other.rating) //
					&& Objects.equal(this.width, other.width) //
					&& Objects.equal(this.height, other.height) //
					&& Objects.equal(this.path, other.path) //
					&& Objects.equal(this.date, other.date) //
					&& Objects.equal(this.mapUrl, other.mapUrl) //
					&& Objects.equal(this.camera, other.camera) //
					&& Objects.equal(this.lens, other.lens) //
					&& Objects.equal(this.aperture, other.aperture) //
					&& Objects.equal(this.shutterSpeed, other.shutterSpeed) //
					&& Objects.equal(this.iso, other.iso) //
					&& Objects.equal(this.isFlashFired, other.isFlashFired) //
					&& Objects.equal(this.focusDistance, other.focusDistance) //
					&& Objects.equal(this.copyright, other.copyright) //
					&& Objects.equal(this.expedition, other.expedition) //
					&& Objects.equal(this.taxon, other.taxon) //
					&& Objects.equal(this.isMakingOf, other.isMakingOf) //
					&& Objects.equal(this.isPublished, other.isPublished) //
					&& Objects.equal(this.tags, other.tags);
		}

		return returnVal;
	}

	@Override
	public String toString() {
		return getTitle() + "(" + getId() + ")";
	}
}
