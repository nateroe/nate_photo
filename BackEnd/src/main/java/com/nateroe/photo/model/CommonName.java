package com.nateroe.photo.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "CommonName")
public class CommonName {
	@Id
	@GeneratedValue(generator = "CommonName_pk_seq")
	@GenericGenerator(name = "CommonName_pk_seq", strategy = "increment")
	private Long id;

	private String name;

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Taxon parent;

	public CommonName() {
	}

	public CommonName(String name, Taxon parent) {
		this.name = name;
		this.parent = parent;
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

	public Taxon getParent() {
		return parent;
	}

	public void setParent(Taxon parent) {
		this.parent = parent;
	}

}
