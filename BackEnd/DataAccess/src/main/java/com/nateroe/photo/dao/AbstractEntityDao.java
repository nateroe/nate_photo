package com.nateroe.photo.dao;

import com.nateroe.photo.model.AbstractEntity;

public class AbstractEntityDao<T extends AbstractEntity> extends AbstractDao<T, Long> {
	public boolean isAttached(T entity) {
		return entity != null && entity.getId() != null && em.contains(entity);
	}

	public void saveOrUpdate(T entity) {
		if (isAttached(entity)) {
			em.merge(entity);
		} else {
			em.persist(entity);
		}
	}
}
