package com.nateroe.photo.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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

import org.apache.log4j.Logger;

/**
 * Hibernate session-keeping. Uses ThreadLocal to provide the same session to the same Thread during
 * repeated calls to getSession().
 * 
 * @author nate
 */
public class JpaUtil {
	private static final Logger LOGGER = Logger.getLogger(JpaUtil.class);

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
			returnVal = getEntityManager();
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
		if (entityManager != null) {
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
