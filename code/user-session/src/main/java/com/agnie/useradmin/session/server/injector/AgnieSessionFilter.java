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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agnie.common.gwt.serverclient.client.enums.Cokie;
import com.agnie.common.helper.ServerURLInfo;
import com.agnie.common.injector.CommonModule;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * AuthSessionFilter will intercept every incoming request and try to look for session id cookie. If cookie is present
 * then it will extract the cookie and bind it in current Guice request scope.
 * 
 */
@Singleton
public class AgnieSessionFilter implements Filter {

	@Inject
	@Named(SessionCommonModule.DEFAULT_EXP_TIME)
	private int		defaultExpTime;

	@Inject
	@Named(CommonModule.SERVER_IP)
	private String	serverIp;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Cookie[] cookies = httpRequest.getCookies();
		String cookieSessionId = null;
		boolean refreshSessionCookie = false;
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (Cokie.AUTH.getKey().equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
					cookieSessionId = cookie.getValue();

					// Here we are extracting session id from cookie and setting in httpRequest as attribute which Guice
					// servlet injector can read it and use it in request scope where ever it is required in request
					// scope
					httpRequest.setAttribute(Key.get(String.class, Names.named(SessionServletModule.SESSION_ID)).toString(), cookieSessionId);
				} else if (Cokie.DNT_RMEMBER.getKey().equals(cookie.getName())) {
					refreshSessionCookie = true;
				} else {
					// all the cookie values are available in Guice environment to access.
					httpRequest.setAttribute(Key.get(String.class, Names.named(cookie.getName())).toString(), cookie.getValue());
				}
			}
		}
		chain.doFilter(request, response);
		if (refreshSessionCookie) {
			HttpServletResponse resp = (HttpServletResponse) response;
			setSessionCookie(defaultExpTime, httpRequest.getServerName(), cookieSessionId, resp, true);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	public void setSessionCookie(int expiry, String serverName, String session, HttpServletResponse resp, boolean rememberMe) {
		Cookie auck = new Cookie(Cokie.AUTH.getKey(), session);
		Cookie dnRmb = new Cookie(Cokie.DNT_RMEMBER.getKey(), "true");
		auck.setPath("/");
		dnRmb.setPath("/");
		if (!("127.0.0.1".equals(serverName) || "localhost".equals(serverName) || serverName.equals(serverIp))) {
			auck.setDomain("." + ServerURLInfo.AGNIE_BASE_DOMAIN);
			dnRmb.setDomain("." + ServerURLInfo.AGNIE_BASE_DOMAIN);
		}
		auck.setMaxAge(expiry);
		dnRmb.setMaxAge(expiry);
		resp.addCookie(auck);
		if (!rememberMe)
			resp.addCookie(dnRmb);
	}

	public void clearSessionCookie(String serverName, HttpServletResponse resp) {
		Cookie cuurentDomainCookie = new Cookie(Cokie.AUTH.getKey(), null);
		Cookie dnRmb = new Cookie(Cokie.DNT_RMEMBER.getKey(), null);
		cuurentDomainCookie.setPath("/");
		dnRmb.setPath("/");
		if (!("127.0.0.1".equals(serverName) || "localhost".equals(serverName) || serverName.equals(serverIp))) {
			cuurentDomainCookie.setDomain("." + ServerURLInfo.AGNIE_BASE_DOMAIN);
			dnRmb.setDomain("." + ServerURLInfo.AGNIE_BASE_DOMAIN);
		}
		cuurentDomainCookie.setMaxAge(0);
		dnRmb.setMaxAge(0);
		// Here setting null value to session cookie.
		resp.addCookie(cuurentDomainCookie);
		resp.addCookie(dnRmb);
	}

}
