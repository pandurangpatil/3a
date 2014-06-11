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
package com.agnie.useradmin.persistance.server.injector;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.agnie.common.injector.PersistenceLifeCycleManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UserAdminPersistFilter implements Filter {
	private final PersistenceLifeCycleManager	manager;

	@Inject
	public UserAdminPersistFilter(@UserAdminPersistService PersistenceLifeCycleManager manager) {
		this.manager = manager;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.manager.startService();
	}

	public void destroy() {
		this.manager.stopService();
	}

	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {

		this.manager.beginUnitOfWork();
		try {
			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			this.manager.endUnitOfWork();
		}
	}
}
