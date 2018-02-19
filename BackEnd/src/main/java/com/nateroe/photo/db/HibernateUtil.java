package com.nateroe.photo.db;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Hibernate session-keeping. Uses ThreadLocal to provide the same session to the same Thread during
 * repeated calls to getSession().
 * 
 * @author nate
 */
public class HibernateUtil {
	private static final Logger LOGGER = Logger.getLogger(HibernateUtil.class);

	private static StandardServiceRegistry registry;
	private static SessionFactory sessionFactory;

	private static final ThreadLocal<Session> LOCAL_SESSION = new ThreadLocal<>();

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				// Create registry
				registry = new StandardServiceRegistryBuilder().configure().build();

				// Create MetadataSources
				MetadataSources sources = new MetadataSources(registry);

				// Create Metadata
				Metadata metadata = sources.getMetadataBuilder().build();

				// Create SessionFactory
				sessionFactory = metadata.getSessionFactoryBuilder().build();

			} catch (Exception e) {
				e.printStackTrace();
				if (registry != null) {
					StandardServiceRegistryBuilder.destroy(registry);
				}
			}
		}
		return sessionFactory;
	}

	/**
	 * Get the session for the current Thread, creating a new session if
	 * necessary.
	 * 
	 * @return a {@link org.hibernate.Session Session} instance
	 */
	public static Session getSession() {
		LOGGER.debug("getSession()");
		Session returnVal = LOCAL_SESSION.get();

		if (returnVal == null) {
			LOGGER.debug("getSession() -- new session.");
			returnVal = getSessionFactory().openSession();
			LOCAL_SESSION.set(returnVal);
		}

		return returnVal;
	}

	/**
	 * Close this thread's current session, if any.
	 */
	public static void closeSession() {
		LOGGER.debug("closeSession()");

		Session session = LOCAL_SESSION.get();
		if (session != null) {
			LOGGER.debug("closeSession() -- really close");
			session.close();
			LOCAL_SESSION.set(null);
		}
	}

	/**
	 * Begins a Transaction using the current session, creating a new session if
	 * necessary.
	 * 
	 * @return a Transaction instance
	 */
	public static Transaction beginTransaction() {
		LOGGER.debug("beginTransaction()");
		return getSession().beginTransaction();
	}

	static void shutdown() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}

		if (registry != null) {
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}
}
