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
package com.agnie.useradmin.contextmgr.client.mvp;

import com.agnie.common.gwt.serverclient.client.enums.Cokie;
import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.gwt.common.client.rpc.LoaderRequestTransport;
import com.agnie.useradmin.persistance.client.service.AuthenticateService;
import com.agnie.useradmin.persistance.client.service.AuthenticateServiceAsync;
import com.agnie.useradmin.persistance.client.service.UserProfileService;
import com.agnie.useradmin.persistance.client.service.UserProfileServiceAsync;
import com.agnie.useradmin.persistance.shared.service.MVPRequestFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ClientFactory extends com.agnie.useradmin.session.client.mvp.ClientFactory {
	@Inject
	ContextMVPRequestTransport			mvpRequestTransport;
	@Inject
	SelDomainRpcRequestBuilder			selDomainRpcRequestBuilder;
	private MVPRequestFactory			requestFactory;
	private EventBus					eventBus			= new SimpleEventBus();
	private UserProfileServiceAsync		userProfileService;
	private AuthenticateServiceAsync	authenticateService;
	private RootPanelFactory			rootPanelFactory	= GWT.create(RootPanelFactory.class);

	@Singleton
	public static class SelDomainRpcRequestBuilder extends SessionIdRpcRequestBuilder {
		@Override
		protected RequestBuilder doCreate(String serviceEntryPoint) {
			RequestBuilder rb = super.doCreate(serviceEntryPoint);
			rb.setHeader(QueryString.SELECTED_DOMAIN.getKey(), Window.Location.getParameter(QueryString.SELECTED_DOMAIN.getKey()));
			String context = Window.Location.getParameter(QueryString.SELECTED_CONTEXT.getKey());
			if (context != null && !context.isEmpty()) {
				rb.setHeader(QueryString.SELECTED_CONTEXT.getKey(), context);
			}
			return rb;
		}
	}

	public AuthenticateServiceAsync getAuthenticateService() {
		if (authenticateService == null) {
			authenticateService = GWT.create(AuthenticateService.class);
			ServiceDefTarget target = (ServiceDefTarget) authenticateService;
			target.setRpcRequestBuilder(loaderBuilder);
		}
		return authenticateService;
	}

	public UserProfileServiceAsync getuserProfileService() {
		if (userProfileService == null) {
			userProfileService = GWT.create(UserProfileService.class);
			ServiceDefTarget target = (ServiceDefTarget) userProfileService;
			target.setRpcRequestBuilder(selDomainRpcRequestBuilder);
		}
		return userProfileService;
	}

	public MVPRequestFactory getRequestFactory() {
		if (requestFactory == null) {
			requestFactory = GWT.create(MVPRequestFactory.class);
			requestFactory.initialize(eventBus, mvpRequestTransport);
		}
		return requestFactory;
	}

	@Singleton
	public static class ContextMVPRequestTransport extends LoaderRequestTransport {
		@Override
		protected void configureRequestBuilder(RequestBuilder builder) {
			String sessionId = Cookies.getCookie(Cokie.AUTH.getKey());
			if (sessionId != null) {
				builder.setHeader(QueryString.SESSION.getKey(), sessionId);
			}
			// Sending selected admin domain
			/*
			 * TODO: We shouldn't have to set this Content-Type it should have taken cared by RequestFactory framework
			 * only but because of some reason it is sending content type as text. Which resulted in following exception
			 * [WARN] /gwtRequest javax.servlet.ServletException: Content-Type was 'text/plain; charset=utf-8'. Expected
			 * 'application/json'. at com.google.gwt.user.server.rpc.RPCServletUtils
			 * .checkContentTypeIgnoreCase(RPCServletUtils.java:476)
			 * 
			 * We should identify root issue and remove below setting of Content-Type.
			 */
			builder.setHeader("Content-Type", "application/json; charset=UTF-8");
			String context = Window.Location.getParameter(QueryString.SELECTED_CONTEXT.getKey());
			if (context != null && !context.isEmpty()) {
				builder.setHeader(QueryString.SELECTED_CONTEXT.getKey(), context);
			}
			builder.setHeader(QueryString.SELECTED_DOMAIN.getKey(), Window.Location.getParameter(QueryString.SELECTED_DOMAIN.getKey()));
		}
	}

	/**
	 * @return the rootPanelFactory
	 */
	public RootPanelFactory getRootPanelFactory() {
		return rootPanelFactory;
	}

}
