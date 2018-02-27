package com.nateroe.photo.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
@Table(name = "ImageResource")
public class ImageResource {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "increment")
	@SequenceGenerator(name = "increment", sequenceName = "ImageResource_pk_seq")
	private Long id;

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	private Photo parent;

	private String url;
	private int width;
	private int height;

	private ImageResource() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Photo getParent() {
		return parent;
	}

	public void setParent(Photo parent) {
		this.parent = parent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId(), parent, url, width, height);
	}

	@Override
	public boolean equals(Object obj) {
		boolean returnVal;

		if (obj == null) {
			returnVal = false;
		} else if (getClass() != obj.getClass()) {
			returnVal = false;
		} else {
			ImageResource other = (ImageResource) obj;

			returnVal = Objects.equal(this.getId(), other.getId()) //
					&& Objects.equal(this.parent, other.parent) //
					&& Objects.equal(this.url, other.url) //
					&& Objects.equal(this.width, other.width) //
					&& Objects.equal(this.height, other.height) //
			;
		}

		return returnVal;
	}
}
