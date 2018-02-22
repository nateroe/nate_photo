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
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.base.Objects;

@XmlRootElement
@Entity
@Table(name = "Taxon")
public class Taxon {
	@Id
	@GenericGenerator(name = "Taxon_pk_seq", strategy = "increment")
	@GeneratedValue(generator = "Taxon_pk_seq")
	private Long id;
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private TaxonomicRank rank;
	private String name;
	private int tsn;

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Taxon parent;

	@OneToMany(mappedBy = "parent")
	private Set<Taxon> children = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
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
