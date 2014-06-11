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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.gwt.serverclient.client.enums.Cokie;
import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.common.gwt.serverclient.client.helper.URLGenerator;
import com.agnie.useradmin.persistance.client.exception.DomainAuthException;
import com.agnie.useradmin.persistance.client.exception.InvalidDomain;
import com.agnie.useradmin.persistance.client.exception.RegistrationDisabledException;
import com.agnie.useradmin.persistance.client.exception.RequestedException;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.persistance.server.dao.AuthenticateManager;
import com.agnie.useradmin.persistance.server.util.UserAdminServerURLInfo;
import com.agnie.useradmin.session.client.helper.RequestType;
import com.agnie.useradmin.session.client.helper.UserNotAuthenticated;
import com.agnie.useradmin.session.server.injector.AgnieAuthCheckFilter;
import com.agnie.useradmin.session.server.injector.SessionServletModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

@Singleton
public class UserIdentityVerificationFilter implements Filter {
	private static org.apache.log4j.Logger	log						= Logger.getLogger(UserIdentityVerificationFilter.class);
	@Inject
	private Injector						injector;

	@Inject
	@Named(SessionServletModule.SESSION_ID)
	Provider<String>						sessionId;
	@Inject
	AgnieAuthCheckFilter					aacf;
	@Inject
	@Named(SessionServletModule.CURRENT_USER)
	Provider<UserAccount>					curntUser;
	/*
	 * TODO: initialize it from property file
	 */
	private static final String				LOGIN_SERVICE_REQUEST	= "/Login/authService";
	@Inject
	private UserAdminURLGenerator			uaug;

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		// Setting the dev mode parameter which cab be used to generate login url.
		String devMode = httpRequest.getParameter(SessionServletModule.DEV_MODE);
		if (devMode != null) {
			httpRequest.setAttribute(Key.get(String.class, Names.named(SessionServletModule.DEV_MODE)).toString(), devMode);
		}
		HttpServletResponse httpResp = (HttpServletResponse) response;
		String reqURI = httpRequest.getRequestURI();
		String location = httpRequest.getRequestURL().toString();
		location = location.substring(0, location.lastIndexOf("/"));
		UserAdminServerURLInfo params = new UserAdminServerURLInfo(httpRequest, sessionId.get());
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
			if (reqURI.contains(URLGenerator.LOGIN_REQUEST)) {
				// User browser has requested login page to be presented.
				String source = httpRequest.getParameter(QueryString.SOURCE.getKey());
				String domain = httpRequest.getParameter(QueryString.DOMAIN.getKey());
				try {
					if (aacf.injectCurrentUser(httpRequest, httpResp)) {
						// If injection of user successful that means. User is already logged in and session is also
						// valid session. In that case check if user has access to given domain.
						AuthenticateManager am = injector.getInstance(AuthenticateManager.class);
						String redirectUrl = am.validateApplicationAccess(domain, source);
						// if we get redirect url that means user has the active access to given domain. In any other
						// case exception is thrown.
						httpResp.sendRedirect(uaug.getAfterLoginRedirectUrl(params, domain, redirectUrl));
						return;
					}
				} catch (RegistrationDisabledException e) {
					// TODO : redirect to error page
					Cookie cok = new Cookie(Cokie.STATUS.getKey(), e.getClass().getName());
					((HttpServletResponse) response).addCookie(cok);
					log.info("User registration is in disabled state");
				} catch (RequestedException e) {
					// TODO : redirect to error page
					Cookie cok = new Cookie(Cokie.STATUS.getKey(), e.getClass().getName());
					((HttpServletResponse) response).addCookie(cok);
					log.info("User request is not yet approved by the domain");
				} catch (DomainAuthException e) {
					// TODO : redirect to error page
					Cookie cok = new Cookie(Cokie.STATUS.getKey(), e.getClass().getName());
					((HttpServletResponse) response).addCookie(cok);
					cok = new Cookie(Cokie.USER.getKey(), curntUser.get().getUserName());
					((HttpServletResponse) response).addCookie(cok);
					log.info("User is not registered with given domain");
				} catch (InvalidDomain e) {
					// TODO : redirect to error page
					Cookie cok = new Cookie(Cokie.STATUS.getKey(), e.getClass().getName());
					((HttpServletResponse) response).addCookie(cok);
					log.error("Requested domain is not registered '" + source + "'");
				} catch (UserNotAuthenticated e) {
					// Ideally this case should never occur here as validationApplicationAccess is called after check is
					// done for valid logged in user.
					log.error("User is authenticated");
				}
				chain.doFilter(httpRequest, response);
			} else if (reqURI.contains(LOGIN_SERVICE_REQUEST)) {
				/**
				 * TODO: Instead of checking for contains, we need to identify some better way to check for login
				 * service related requested to by pass.
				 */
				log.info("login service requests");
				chain.doFilter(httpRequest, response);
				log.info("response is given back");
			} else {
				aacf.authEnforcer(request, response, chain);
				return;
			}
		} else {
			chain.doFilter(httpRequest, response);
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
