package com.nateroe.photo.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.core.GenericTypeResolver;

import com.nateroe.photo.db.JpaUtil;
import com.nateroe.photo.model.CommonName;

public abstract class AbstractDao<T, ID extends Serializable> {
	protected Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public AbstractDao() {
		// find the Class of T
		this.entityClass = (Class<T>) GenericTypeResolver.resolveTypeArguments(getClass(),
				AbstractDao.class)[0];
	}

	public void save(CommonName entity) {
		JpaUtil.getEntityManager().persist(entity);
	}

	public void delete(CommonName entity) {
		JpaUtil.getEntityManager().remove(entity);
	}

	public T findByPrimaryKey(ID id) {
		return JpaUtil.getEntityManager().find(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return JpaUtil.getEntityManager().createQuery("from " + entityClass.getName())
				.getResultList();
	}
}
