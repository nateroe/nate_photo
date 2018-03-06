package com.nateroe.photo.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nateroe.photo.model.AbstractEntity;

public class AbstractEntityDao<T extends AbstractEntity> extends AbstractDao<T, Long> {
	private static Logger LOGGER = LoggerFactory.getLogger(AbstractEntityDao.class);

	public boolean isAttached(T entity) {
		return entity != null && (entity.getId() != null || em.contains(entity));
	}

	public T saveOrUpdate(T entity) {
		T result;
		if (isAttached(entity)) {
			LOGGER.debug("Merge entity {}:{}", entity.getClass(), entity.getId());
			result = em.merge(entity);
		} else {
			LOGGER.debug("Persist entity {}:{}", entity.getClass());
			em.persist(entity);
			result = entity;
		}
		return result;
	}
}
