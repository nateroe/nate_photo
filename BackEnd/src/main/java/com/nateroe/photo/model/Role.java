package com.nateroe.photo.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * User roles in order of increasing privilege. Each user has a single role. Roles are additive: a
 * User has all the privileges of a Guest, plus additional features.
 * 
 * @author nate
 */
@XmlRootElement
public enum Role {
	GUEST(1), USER(2), FRIEND(3), ADMIN(4);

	private Long id;

	private Role(long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
