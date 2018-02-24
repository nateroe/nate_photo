package com.nateroe.photo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.base.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "TaxonomicRank")
public class TaxonomicRank {
	@Id
	@GenericGenerator(name = "TaxonomicRank_pk_seq", strategy = "increment")
	@GeneratedValue(generator = "TaxonomicRank_pk_seq")
	private Long id;
	private String name;
	private Integer ordinal;

	public TaxonomicRank() {
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

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getId(), this.ordinal, this.name);
	}

	@Override
	public boolean equals(Object obj) {
		boolean returnVal;

		if (obj == null) {
			returnVal = false;
		} else if (getClass() != obj.getClass()) {
			returnVal = false;
		} else {
			TaxonomicRank other = (TaxonomicRank) obj;

			returnVal = Objects.equal(this.getId(), other.getId()) //
					&& Objects.equal(this.ordinal, other.ordinal) //
					&& Objects.equal(this.name, other.name);
		}

		return returnVal;
	}

	@Override
	public String toString() {
		return getName() + "(" + getId() + ")";
	}
}
