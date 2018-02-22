package com.nateroe.photo.init;

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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Context listener for initialization hooks.
 * 
 * @author nate
 */
@WebListener
public class WebAppContextListener implements ServletContextListener {
	private static final Logger logger = LogManager.getLogger(WebAppContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		logger.info("Context initialized.");

//		try {
//			Context initContext = new InitialContext();
//			Context webContext = (Context) initContext.lookup("java:/comp/env");
//
//			DataSource ds = (DataSource) webContext.lookup("java:/comp/env/jdbc/natephotodb");
//			Connection dbCon = ds.getConnection();
//			dbCon.close();
//		} catch (Exception e) {
//			logger.warn("failed to init datasource", e);
//		}
//
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		logger.info("Context destroyed.");
	}
}
