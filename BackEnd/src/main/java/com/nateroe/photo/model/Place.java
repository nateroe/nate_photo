package com.nateroe.photo.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement
public class Place extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String county;
	private String state;
	private String country;

	public Place() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId(), name, county, state, country);
	}

	@Override
	public boolean equals(Object obj) {
		boolean returnVal;

		if (obj == null) {
			returnVal = false;
		} else if (getClass() != obj.getClass()) {
			returnVal = false;
		} else {
			Place other = (Place) obj;

			returnVal = Objects.equal(this.getId(), other.getId()) //
					&& Objects.equal(this.name, other.name)
					&& Objects.equal(this.county, other.county)
					&& Objects.equal(this.state, other.state)
					&& Objects.equal(this.country, other.country);
		}

		return returnVal;
	}
}
