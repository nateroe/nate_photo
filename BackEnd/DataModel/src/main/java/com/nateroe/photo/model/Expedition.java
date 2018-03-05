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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "Expedition")
public class Expedition extends AbstractEntity {
	private String name;
	private String description;
	private Date beginDate;
	private Date endDate;

	/**
	 * systemName is used by the importer to match known, previously-imported Expeditions when an
	 * expedition is updated by a new import. "systemName" is the file name of the directory from
	 * which the expedition was imported.
	 */
	private String systemName;

	// XXX it's too early to deal with Places
//	private List<Place> places = new LinkedList<>();

	public Expedition() {
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
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

	@Override
	public int hashCode() {
		return Objects.hashCode(getId(), name, systemName, description, beginDate, endDate);
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

			returnVal = Objects.equal(this.getId(), other.getId())
					&& Objects.equal(this.name, other.name)
					&& Objects.equal(this.systemName, other.systemName)
					&& Objects.equal(this.description, other.description)
					&& Objects.equal(this.beginDate, other.beginDate)
					&& Objects.equal(this.endDate, other.endDate);
		}

		return returnVal;
	}

}
