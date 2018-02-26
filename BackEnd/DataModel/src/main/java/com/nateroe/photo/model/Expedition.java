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

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement
public class Expedition {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "increment")
	@SequenceGenerator(name = "increment", sequenceName = "Expedition_pk_seq")
	private Long id;
	private String name;
	private String description;
	private String mapUrl;
	private Date beginDate;
	private Date endDate;
	private Place place;
	private Set<Photo> highlights = new HashSet<>();

	public Expedition() {
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMapUrl() {
		return mapUrl;
	}

	public void setMapUrl(String mapUrl) {
		this.mapUrl = mapUrl;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public void addHighlight(Photo photo) {
		highlights.add(photo);
	}

	public Set<Photo> getHighlights() {
		return highlights;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId(), name, description, mapUrl, beginDate, endDate, place);
	}

	@Override
	public boolean equals(Object obj) {
		boolean returnVal;

		if (obj == null) {
			returnVal = false;
		} else if (getClass() != obj.getClass()) {
			returnVal = false;
		} else {
			Expedition other = (Expedition) obj;

			returnVal = Objects.equal(this.getId(), other.getId()) //
					&& Objects.equal(this.name, other.name)
					&& Objects.equal(this.description, other.description)
					&& Objects.equal(this.mapUrl, other.mapUrl)
					&& Objects.equal(this.beginDate, other.beginDate)
					&& Objects.equal(this.endDate, other.endDate)
					&& Objects.equal(this.place, other.place);
		}

		return returnVal;
	}
}
