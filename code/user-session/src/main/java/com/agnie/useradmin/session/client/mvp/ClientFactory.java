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
package com.agnie.useradmin.session.client.mvp;

import com.agnie.common.gwt.serverclient.client.enums.Cokie;
import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.useradmin.session.client.service.UserSessionService;
import com.agnie.useradmin.session.client.service.UserSessionServiceAsync;
import com.agnie.gwt.common.client.rpc.LoaderRpcRequestBuilder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ClientFactory {

	private UserSessionServiceAsync	userSessionService;

	public UserSessionServiceAsync getUserSessionService() {
		if (userSessionService == null) {
			userSessionService = GWT.create(UserSessionService.class);
			ServiceDefTarget target = (ServiceDefTarget) userSessionService;
			target.setRpcRequestBuilder(loaderBuilder);
		}
		return userSessionService;
	}

	@Inject
	protected SessionIdRpcRequestBuilder	loaderBuilder;

	@Singleton
	public static class SessionIdRpcRequestBuilder extends LoaderRpcRequestBuilder {
		@Override
		protected RequestBuilder doCreate(String serviceEntryPoint) {
			RequestBuilder rb = super.doCreate(serviceEntryPoint);
			String sessionId = Cookies.getCookie(Cokie.AUTH.getKey());
			if (sessionId != null && !sessionId.isEmpty()) {
				rb.setHeader(QueryString.SESSION.getKey(), sessionId);
			}
			return rb;
		}
	}
}
