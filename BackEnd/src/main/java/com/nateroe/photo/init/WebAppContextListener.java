package com.nateroe.photo.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

/**
 * Context listener for initialization hooks.
 * 
 * @author nate
 */
@WebListener
public class WebAppContextListener implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(WebAppContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		logger.info("Context initialized.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		logger.info("Context destroyed.");
	}
}
