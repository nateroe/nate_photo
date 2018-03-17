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

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.google.common.base.Objects;

/**
 * 
 * @author nate
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "Photo")
public class Photo extends AbstractEntity {
	private String title;
	private String description;
	private Integer rating;
	private Date date;
	private String camera;
	private String lens;
	private String aperture;
	private String shutterSpeed;
	private String iso;
	private Integer flash;
	private Float focalLength;
	private Float focusDistance;
	private Float latitude;
	private Float longitude;
	private Float altitude;
	private String copyright;
	private String usageTerms;
	private Boolean isPublished;

	@XmlTransient
	private String origFileName;

	// separate queries to find the Expedition, just the id here
	private Long expeditionId;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.ALL,
			orphanRemoval = true)
	@OrderBy("width DESC")
	private List<ImageResource> images = new LinkedList<>();

// XXX ---- this stuff isn't required yet and it's easier to ignore it for now.
//	private String mapUrl;

//	private final AtomicLong visitCount;
//	private final AtomicLong favoriteCount;

//	private Taxon taxon;
//	private Set<Tag> tags = new HashSet<>();

	public Photo() {
	}

	public void addImageResource(ImageResource imageResource) {
		images.add(imageResource);
		imageResource.setParent(this);
	}

	public void addImageResources(List<ImageResource> imageResources) {
		this.images.addAll(imageResources);
		for (ImageResource imageResource : imageResources) {
			imageResource.setParent(this);
		}
	}

	public boolean removeImageResource(ImageResource imageResource) {
		imageResource.setParent(null);
		return images.remove(imageResource);
	}

	public Float getAltitude() {
		return altitude;
	}

	public void setAltitude(Float altitude) {
		this.altitude = altitude;
	}

	public String getAperture() {
		return aperture;
	}

	public void setAperture(String aperture) {
		this.aperture = aperture;
	}

	public String getCamera() {
		return camera;
	}

	public void setCamera(String camera) {
		this.camera = camera;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getExpeditionId() {
		return expeditionId;
	}

	public void setExpeditionId(Long expeditionId) {
		this.expeditionId = expeditionId;
	}

	public Integer getFlash() {
		return flash;
	}

	public void setFlash(Integer flash) {
		this.flash = flash;
	}

	public Float getFocalLength() {
		return focalLength;
	}

	public void setFocalLength(Float focalLength) {
		this.focalLength = focalLength;
	}

	public Float getFocusDistance() {
		return focusDistance;
	}

	public void setFocusDistance(Float focusDistance) {
		this.focusDistance = focusDistance;
	}

	public List<ImageResource> getImageResources() {
		return new ArrayList<>(images);
	}

	public void setImageResources(List<ImageResource> imageResources) {
		this.images.clear();
		addImageResources(imageResources);
	}

	public String getIso() {
		return iso;
	}

	public void setIso(String iso) {
		this.iso = iso;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public String getLens() {
		return lens;
	}

	public void setLens(String lens) {
		this.lens = lens;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Boolean isPublished() {
		return isPublished;
	}

	public void setPublished(Boolean isPublished) {
		this.isPublished = isPublished;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getShutterSpeed() {
		return shutterSpeed;
	}

	public void setShutterSpeed(String shutterSpeed) {
		this.shutterSpeed = shutterSpeed;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUsageTerms() {
		return usageTerms;
	}

	public void setUsageTerms(String usageTerms) {
		this.usageTerms = usageTerms;
	}

	public String getOrigFileName() {
		return origFileName;
	}

	public void setOrigFileName(String origFileName) {
		this.origFileName = origFileName;
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
					&& Objects.equal(this.date, other.date) //
					&& Objects.equal(this.camera, other.camera) //
					&& Objects.equal(this.lens, other.lens) //
					&& Objects.equal(this.aperture, other.aperture) //
					&& Objects.equal(this.shutterSpeed, other.shutterSpeed) //
					&& Objects.equal(this.iso, other.iso) //
					&& Objects.equal(this.flash, other.flash) //
					&& Objects.equal(this.focalLength, other.focalLength) //
					&& Objects.equal(this.focusDistance, other.focusDistance) //
					&& Objects.equal(this.copyright, other.copyright) //
					&& Objects.equal(this.isPublished, other.isPublished);
		}

		return returnVal;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId(), title, description, rating, date, camera, lens, aperture,
				shutterSpeed, iso, flash, focalLength, focusDistance, copyright, isPublished);
	}

	@Override
	public String toString() {
		return getTitle() + "(" + getId() + ")";
	}

}
