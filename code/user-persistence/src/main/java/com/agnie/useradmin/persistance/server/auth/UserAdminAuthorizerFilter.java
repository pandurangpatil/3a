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
package com.agnie.useradmin.persistance.server.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.common.server.auth.ACLContext;
import com.agnie.useradmin.persistance.server.dao.UserProfileDao;
import com.agnie.useradmin.session.server.injector.SessionServletModule;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * This filter will look for selected domain in query parameter or if not found then inside HTTP header. Will retrieve
 * corresponding Admin ACL for given domain for logged in user and set it in Guice context so that it is accessible
 * inside application.
 * 
 */
@Singleton
public class UserAdminAuthorizerFilter implements Filter {

	@Inject
	@Named(SessionServletModule.SESSION_ID)
	Provider<String>		sessionId;

	@Inject
	@Named(SessionServletModule.CURRENT_USER)
	Provider<UserAccount>	curntUser;
	@Inject
	UserProfileDao			updao;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String selDomain = httpRequest.getParameter(QueryString.SELECTED_DOMAIN.getKey());
		// first check if selected domain can be identified from query string.
		if (selDomain == null) {
			// if it is not passed on through query string then look in HTTP headers
			selDomain = httpRequest.getHeader(QueryString.SELECTED_DOMAIN.getKey());
		}

		if (selDomain != null) {
			// there is possibility that domain is not selected.
			httpRequest.setAttribute(Key.get(String.class, Names.named(DomainAuthorizer.SELECTED_DOMAIN)).toString(), selDomain);
			ACLContext aclCtx = new ACLContext(updao.getAdminAclForDomain(selDomain, sessionId.get()));
			httpRequest.setAttribute(Key.get(ACLContext.class, Names.named(DomainAuthorizer.SELECTED_DOMAIN_ACL)).toString(), aclCtx);
		}

		String selContext = httpRequest.getParameter(QueryString.SELECTED_CONTEXT.getKey());
		// first check if selected context can be identified from query string.
		if (selContext == null) {
			// if it is not passed on through query string the look in HTTP headers.
			selContext = httpRequest.getHeader(QueryString.SELECTED_CONTEXT.getKey());
		}
		// to select the context one need to first select the domain.
		if (selContext != null && selDomain != null) {
			// there is possibility that context is not selected.
			httpRequest.setAttribute(Key.get(String.class, Names.named(DomainContextAuthorizer.SELECTED_CONTEXT)).toString(), selContext);
			ACLContext aclCtx = new ACLContext(updao.getAdminAclForContext(selDomain, selContext, sessionId.get()));
			httpRequest.setAttribute(Key.get(ACLContext.class, Names.named(DomainContextAuthorizer.SELECTED_DOMAIN_CONTEXT_ACL)).toString(), aclCtx);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
