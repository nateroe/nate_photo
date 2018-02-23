package com.nateroe.photo.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;

import com.nateroe.photo.model.CommonName;

@Stateless
public abstract class AbstractDao<T, ID extends Serializable> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDao.class);
	protected Class<T> entityClass;

	@PersistenceContext(unitName = "NatePhotoDB")
	EntityManager em;

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

//		EntityManagerFactory factory = Persistence.createEntityManagerFactory("NatePhotoDB");
//		em = factory.createEntityManager();

		return em.find(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return em.createQuery("from " + entityClass.getName()).getResultList();
	}
}
