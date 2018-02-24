package com.nateroe.photo.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.base.Objects;

@XmlRootElement
public class Expedition {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
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
