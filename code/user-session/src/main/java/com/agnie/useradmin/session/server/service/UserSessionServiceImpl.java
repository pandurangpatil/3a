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
package com.agnie.useradmin.session.server.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.agnie.common.cache.CacheService;
import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.useradmin.session.client.service.UserSessionService;
import com.agnie.useradmin.session.server.dao.UserSessionDao;
import com.agnie.useradmin.session.server.injector.AgnieSessionFilter;
import com.agnie.useradmin.session.server.injector.SessionServletModule;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class UserSessionServiceImpl extends RemoteServiceServlet implements UserSessionService {

	@SuppressWarnings("unused")
	private static org.apache.log4j.Logger	logger				= Logger.getLogger(UserSessionServiceImpl.class);
	private static final long				serialVersionUID	= 1L;
	@Inject
	private Injector						injector;
	@Inject
	@Named(SessionServletModule.SESSION_ID)
	private Provider<String>				sessionId;
	@Inject
	@Named(SessionServletModule.AGNIE_APPLCATION)
	Provider<String>						agnieApplication;
	@Inject
	UserSessionDao							usdao;
	@Inject
	CacheService							cacheService;
	@Inject
	AgnieSessionFilter						asf;

	@Override
	public UserAccount getLoggedInUserAccount() {
		return usdao.getUserAccountBySessionId(sessionId.get());
	}

	@Override
	public AccessControlList getAclForDomain() {
		return usdao.getAclForDomain(agnieApplication.get(), sessionId.get());
	}

	@Override
	public AccessControlList getAclForContext(String context) {
		return usdao.getAclForContext(agnieApplication.get(), context, sessionId.get());
	}

	@Override
	public void logout() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession httpsession = request.getSession(false);
		if (httpsession != null) {
			httpsession.invalidate();
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			asf.clearSessionCookie(request.getServerName(), this.getThreadLocalResponse());
		}
		UserSessionDao usdao = injector.getInstance(UserSessionDao.class);
		usdao.removeSession(sessionId.get());
		cacheService.put(sessionId.get(), null);
	}

}
