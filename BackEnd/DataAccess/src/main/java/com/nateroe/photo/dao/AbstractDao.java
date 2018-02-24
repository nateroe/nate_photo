package com.nateroe.photo.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.core.GenericTypeResolver;

import com.nateroe.photo.model.CommonName;

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

	public void save(CommonName entity) {
		em.persist(entity);
	}

	public void delete(CommonName entity) {
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
