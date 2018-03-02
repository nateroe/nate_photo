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

package com.nateroe.photo.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.core.GenericTypeResolver;

public abstract class AbstractDao<T, ID extends Serializable> {
	protected Class<T> entityClass;

	@PersistenceContext(unitName = "NatePhotoDB")
	protected EntityManager em;

	@SuppressWarnings("unchecked")
	public AbstractDao() {
		// find the Class of T
		this.entityClass = (Class<T>) GenericTypeResolver.resolveTypeArguments(getClass(),
				AbstractDao.class)[0];
	}

	public void save(T entity) {
		em.persist(entity);
	}

	public void delete(T entity) {
		em.remove(entity);
	}

	public T findByPrimaryKey(ID id) {
		return em.find(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return em.createQuery("from " + entityClass.getName()).getResultList();
	}
}
