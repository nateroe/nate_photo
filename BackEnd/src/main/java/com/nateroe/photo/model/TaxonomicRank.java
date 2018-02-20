package com.nateroe.photo.model;

import java.io.Serializable;

import com.google.common.base.Objects;

public class TaxonomicRank extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private Integer ordinal;

	public TaxonomicRank() {
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
