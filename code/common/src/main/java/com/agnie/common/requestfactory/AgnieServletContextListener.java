/*******************************************************************************
 * Copyright (c) 2014 Agnie Technologies.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Agnie Technologies - initial API and implementation
 ******************************************************************************/
package com.agnie.common.requestfactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.agnie.common.shutdown.ShutdownProcessor;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Super Guice context listener to inject Guice injector inside servlet context so that it can be accessed inside jsp.
 * 
 * @author Pandurang Patil 13-Feb-2014
 * 
 */
public abstract class AgnieServletContextListener extends GuiceServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// No call to super as it also calls getInjector()
		ServletContext sc = servletContextEvent.getServletContext();
		sc.setAttribute(Injector.class.getName(), getInjector());
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		ServletContext sc = servletContextEvent.getServletContext();
		Injector injector = (Injector) sc.getAttribute(Injector.class.getName());
		ShutdownProcessor shutdownProc = injector.getInstance(ShutdownProcessor.class);
		shutdownProc.shutdown(false);
		sc.removeAttribute(Injector.class.getName());
		super.contextDestroyed(servletContextEvent);
	}

}
