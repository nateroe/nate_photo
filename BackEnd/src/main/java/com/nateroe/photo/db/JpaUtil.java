package com.nateroe.photo.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hibernate session-keeping. Uses ThreadLocal to provide the same session to the same Thread during
 * repeated calls to getSession().
 * 
 * @author nate
 */
public class JpaUtil {
	private static final Logger LOGGER = LogManager.getLogger(JpaUtil.class);

	private static EntityManagerFactory entityManagerFactory;

	private static final ThreadLocal<EntityManager> LOCAL_ENTITY_MANAGER = new ThreadLocal<>();

	public static EntityManagerFactory getEntityManagerFactory() {
		if (entityManagerFactory == null) {
			try {
				entityManagerFactory = Persistence.createEntityManagerFactory("NatePhotoDB");
			} catch (Exception e) {
				LOGGER.warn("Failed to create EntityManagerFactory", e);
			}
		}
		return entityManagerFactory;
	}

	/**
	 * Get the EntityManager for the current Thread, creating a new session if
	 * necessary.
	 * 
	 * @return a {@link org.hibernate.Session Session} instance
	 */
	public static EntityManager getEntityManager() {
		LOGGER.debug("getSession()");
		EntityManager returnVal = LOCAL_ENTITY_MANAGER.get();

		if (returnVal == null) {
			LOGGER.debug("getEntityManager() -- new EntityManager.");
			returnVal = getEntityManagerFactory().createEntityManager();
			LOCAL_ENTITY_MANAGER.set(returnVal);
		}

		return returnVal;
	}

	/**
	 * Close this thread's current session, if any.
	 */
	public static void closeEntityManager() {
		LOGGER.debug("closeEntityManager()");

		EntityManager entityManager = LOCAL_ENTITY_MANAGER.get();
		if (entityManager != null && entityManager.isOpen()) {
			LOGGER.debug("closeEntityManager() for real");
			entityManager.close();
			LOCAL_ENTITY_MANAGER.set(null);
		}
	}

	/**
	 * Begins a Transaction using the current session, creating a new session if
	 * necessary.
	 * 
	 * @return a Transaction instance
	 */
	public static EntityTransaction beginTransaction() {
		LOGGER.debug("beginTransaction()");
		EntityTransaction entityTransaction = getEntityManager().getTransaction();
		entityTransaction.begin();
		return entityTransaction;
	}

	static void shutdown() {
		if (entityManagerFactory != null) {
			entityManagerFactory.close();
		}
	}
}
