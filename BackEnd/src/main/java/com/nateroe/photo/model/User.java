package com.nateroe.photo.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

/**
 * Users, user views, and user favorites are not cached. (Maybe view count and fave count per photo
 * is cached, but there's no reason to store the complete list of a given user's favorites unless
 * that user is logged in.)
 * 
 * Password, salt, and token are server-side only. Token never expires, stored in cookie.
 * 
 * @author nate
 */
@XmlRootElement
public class User extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String email;
	private String password;
	private String salt;
	private String token;
	private Role role;

	public User() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public Long getId() {
		return getId();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId(), email, password, salt, token, role);
	}

	@Override
	public boolean equals(Object obj) {
		boolean returnVal;

		if (obj == null) {
			returnVal = false;
		} else if (getClass() != obj.getClass()) {
			returnVal = false;
		} else {
			User other = (User) obj;

			returnVal = Objects.equal(this.getId(), other.getId()) //
					&& Objects.equal(this.email, other.email)
					&& Objects.equal(this.password, other.password)
					&& Objects.equal(this.salt, other.salt)
					&& Objects.equal(this.token, other.token)
					&& Objects.equal(this.role, other.role);
		}

		return returnVal;
	}
}
