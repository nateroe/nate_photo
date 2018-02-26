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

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.google.common.base.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "Taxon")
public class Taxon {
	@XmlTransient
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "increment")
	@SequenceGenerator(name = "increment", sequenceName = "Taxon_pk_seq")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	private TaxonomicRank rank;
	private String name;
	private int tsn;

	@XmlTransient
	@ManyToOne(fetch = FetchType.EAGER)
	private Taxon parent;

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	private Set<Taxon> children = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<CommonName> commonNames = new LinkedList<>();

	public Taxon() {
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public TaxonomicRank getRank() {
		return rank;
	}

	public void setRank(TaxonomicRank rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CommonName> getCommonNames() {
		return commonNames;
	}

	public void addCommonName(CommonName commonName) {
		commonNames.add(commonName);
	}

	public void addCommonNames(List<CommonName> commonNames) {
		this.commonNames.addAll(commonNames);
	}

	public void setCommonNames(List<CommonName> commonNames) {
		this.commonNames = commonNames;
	}

	public int getTsn() {
		return tsn;
	}

	public void setTsn(int tsn) {
		this.tsn = tsn;
	}

	public Set<Taxon> getChildren() {
		return children;
	}

	public void addChildren(Collection<Taxon> children) {
		this.children.addAll(children);
	}

	public void addChild(Taxon child) {
		this.children.add(child);
	}

	public void setChildren(Set<Taxon> children) {
		this.children = children;
	}

	public Taxon getParent() {
		return parent;
	}

	public void setParent(Taxon parent) {
		this.parent = parent;
		parent.addChild(this);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId(), rank, name);
	}

	@Override
	public boolean equals(Object obj) {
		boolean returnVal;

		if (obj == null) {
			returnVal = false;
		} else if (getClass() != obj.getClass()) {
			returnVal = false;
		} else {
			Taxon other = (Taxon) obj;

			returnVal = Objects.equal(this.getId(), other.getId()) //
					&& Objects.equal(this.rank, other.rank) //
					&& Objects.equal(this.name, other.name);
		}

		return returnVal;
	}

	@Override
	public String toString() {
		return getName() + "(" + getId() + ")";
	}
}
