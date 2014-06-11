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
package com.agnie.useradmin.session.server.injector;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.common.gwt.serverclient.client.helper.URLGenerator;
import com.agnie.common.helper.ServerURLInfo;
import com.agnie.common.server.auth.ACLContext;
import com.agnie.common.time.DateService;
import com.agnie.useradmin.session.client.helper.RequestType;
import com.agnie.useradmin.session.server.auth.Authorizer;
import com.agnie.useradmin.session.server.auth.ContextAuthorizer;
import com.agnie.useradmin.session.server.dao.UserAuthSessionManager;
import com.agnie.useradmin.session.server.dao.UserSessionDao;
import com.agnie.useradmin.session.server.entity.UserAuthSession;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * This filter will set logged in user details in UserAccount object in RequestScope of Guice Context. In addition to
 * that it will also extracts Logged in users ACL on agnie application set in context.
 */
@Singleton
public class AgnieAuthCheckFilter implements Filter {

	@Inject
	private Injector		injector;

	@Inject
	@Named(SessionServletModule.SESSION_ID)
	Provider<String>		sessionId;

	// DOMAIN and DEV_MODE properties will be defined inside respective project server module.
	@Inject
	@Named(SessionServletModule.AGNIE_APPLCATION)
	Provider<String>		agnieApplication;

	@Inject
	@Named(SessionServletModule.DEV_MODE)
	Provider<String>		devMode;

	@Inject
	@Named(SessionServletModule.REQUEST_TYPE)
	Provider<RequestType>	reqType;

	@Inject
	DateService				dateService;

	@Inject
	private URLGenerator	usg;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String reqURI = httpRequest.getRequestURI();
		String location = httpRequest.getRequestURL().toString();
		location = location.substring(0, location.lastIndexOf("/"));
		if (reqURI.endsWith(".jsp") || reqURI.endsWith("Service") || reqURI.endsWith("gwtRequest") || reqURI.contains("/rest/")) {
			RequestType type = null;
			if (reqURI.contains("/rest/")) {
				type = RequestType.REST;
			} else if (reqURI.endsWith("gwtRequest")) {
				type = RequestType.REQ_FACTORY;
			} else if (reqURI.endsWith("Service")) {
				type = RequestType.GWT_RPC;
			} else {
				type = RequestType.RESOURCE;
			}
			httpRequest.setAttribute(Key.get(String.class, Names.named(SessionServletModule.REQUEST_TYPE_STR)).toString(), type.name());
			authEnforcer(request, response, chain);
		} else {
			chain.doFilter(httpRequest, httpResponse);
		}
	}

	public void authEnforcer(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		if (injectCurrentUser(httpRequest, httpResponse)) {
			// session is valid session and current user details are injected.
			chain.doFilter(httpRequest, httpResponse);
			return;
		}
		respondNotAuthenticated(httpRequest, httpResponse);
	}

	public void respondNotAuthenticated(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
		ServerURLInfo sui = new ServerURLInfo(httpRequest);
		/**
		 * TODO: Check for GWT RPC call or RequestFactory call and respond accordingly for those AJAX calls.
		 * 
		 * Execution comes here that means user is not authenticated.
		 */
		switch (reqType.get()) {
		case GWT_RPC:
		case REQ_FACTORY:
		case REST:
			httpResponse.setStatus(Status.UNAUTHORIZED.getStatusCode());
			break;
		default:
			httpResponse.sendRedirect(usg.getLoginURL(sui, agnieApplication.get(), devMode.get()));
			break;
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	public boolean injectCurrentUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
		UserSessionDao usd = injector.getInstance(UserSessionDao.class);
		String cookieSessionId = this.sessionId.get();
		String queryParamSessionId = httpRequest.getParameter(QueryString.SESSION.getKey());
		String headerSessionId = httpRequest.getHeader(QueryString.SESSION.getKey());

		/*
		 * NOTE: session id can be passed from three source cookie, query parameter and HTTP header. Session id should
		 * at least present in any two sources and both the values should match.
		 */
		if ((cookieSessionId != null && !cookieSessionId.isEmpty() && RequestType.RESOURCE.equals(reqType.get()))
				|| (cookieSessionId != null && !cookieSessionId.isEmpty() && cookieSessionId.equals(queryParamSessionId))
				|| (headerSessionId != null && !headerSessionId.isEmpty() && headerSessionId.equals(queryParamSessionId))
				|| (headerSessionId != null && !headerSessionId.isEmpty() && headerSessionId.equals(cookieSessionId))) {
			if (cookieSessionId == null || cookieSessionId.isEmpty()) {
				cookieSessionId = queryParamSessionId;
				httpRequest.setAttribute(Key.get(String.class, Names.named(SessionServletModule.SESSION_ID)).toString(), cookieSessionId);
			}
			UserAccount ua = usd.getUserAccountBySessionId(cookieSessionId);
			if (ua != null) {

				UserAuthSessionManager uasm = injector.getInstance(UserAuthSessionManager.class);
				UserAuthSession uas = uasm.getUserAuthBySessionId(cookieSessionId);
				if (uas != null) {
					httpRequest.setAttribute(Key.get(UserAccount.class, Names.named(SessionServletModule.CURRENT_USER)).toString(), ua);
					ACLContext aclCtx = new ACLContext(usd.getAclForDomain(agnieApplication.get(), cookieSessionId));
					httpRequest.setAttribute(Key.get(ACLContext.class, Names.named(Authorizer.APP_ACL_CTX)).toString(), aclCtx);

					String context = httpRequest.getParameter(QueryString.CONTEXT.getKey());
					// first check if selected context can be identified from query string.
					if (context == null) {
						// if it is not passed on through query string the look in HTTP headers.
						context = httpRequest.getHeader(QueryString.CONTEXT.getKey());
					}
					// to select the context one need to first select the domain.
					if (context != null && agnieApplication.get() != null) {
						// there is possibility that context is not selected.
						httpRequest.setAttribute(Key.get(String.class, Names.named(ContextAuthorizer.CONTEXT)).toString(), context);
						ACLContext contextAclCtx = new ACLContext(usd.getAclForContext(agnieApplication.get(), context, cookieSessionId));
						httpRequest.setAttribute(Key.get(ACLContext.class, Names.named(ContextAuthorizer.CONTEXT_ACL_CTX)).toString(), contextAclCtx);
					}
					// TODO: Also need to update the cookie expire time
					Date crntTime = dateService.getCurrentDate();
					Calendar cal = Calendar.getInstance();
					cal.setTime(crntTime);
					// TODO: Need to have more configurable mechanism to set frequency after which we hit to DB to
					// update lastAccess Time stamp.
					cal.add(Calendar.MINUTE, -3);
					if (ua.getLastUpdateTime() == null || cal.getTime().after(ua.getLastUpdateTime())) {
						uasm.updateSession(uas, ua);
					}
					return true;
				}
			}
		}
		return false;
	}

}
