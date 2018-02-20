package com.nateroe.photo.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement
public class Tag extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String value;

	public Tag() {
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		boolean returnVal;

		if (obj == null) {
			returnVal = false;
		} else if (getClass() != obj.getClass()) {
			returnVal = false;
		} else {
			Tag other = (Tag) obj;

			returnVal = Objects.equal(this.getId(), other.getId()) //
					&& Objects.equal(this.value, other.value);
		}

		return returnVal;
	}
}
