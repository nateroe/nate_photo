package com.nateroe.photo.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.common.base.Objects;

@XmlRootElement
public class Taxon extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private TaxonomicRank rank;
	private String name;
	private List<String> commonNames = new LinkedList<>();
	private int tsn;
	private Set<Taxon> children = new HashSet<>();

	@JsonIgnore
	private Taxon parent;

	public Taxon() {
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

	public List<String> getCommonNames() {
		return commonNames;
	}

	public void addCommonName(String commonName) {
		commonNames.add(commonName);
	}

	public void addCommonNames(List<String> commonNames) {
		this.commonNames.addAll(commonNames);
	}

	public int getTsn() {
		return tsn;
	}

	public void setTsn(int tsn) {
		this.tsn = tsn;
	}

	public Taxon getParent() {
		return parent;
	}

	public void setParent(Taxon parent) {
		this.parent = parent;
		if (parent != null) {
			parent.addChild(this);
		}
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
