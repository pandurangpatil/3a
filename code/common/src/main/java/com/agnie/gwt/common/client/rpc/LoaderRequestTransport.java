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
package com.agnie.gwt.common.client.rpc;

import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.common.gwt.serverclient.client.helper.URLGenerator;
import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.common.gwt.serverclient.client.injector.CommonServerClientModule;
import com.agnie.gwt.common.client.widget.Loader;
import com.agnie.gwt.common.client.widget.LoaderResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

/**
 * Request factory intercepter to show loader image while call is under progress and stop it once we recieve the
 * response.
 * 
 */
@Singleton
public class LoaderRequestTransport extends DefaultRequestTransport {

	@Inject
	@Named(CommonServerClientModule.CURRENT_APP_DOMAIN)
	String			appDomain;
	@Inject
	URLGenerator	urlGenerator;
	@Inject
	URLInfo			urlInfo;

	private Loader	loader;

	public LoaderRequestTransport(Loader loader) {
		this.loader = loader;
		loader.hide();
	}

	public LoaderRequestTransport() {
		this(new Loader(LoaderResources.INSTANCE.communicating()));
	}

	@Override
	public void send(final String payload, final TransportReceiver receiver) {
		GWT.log("making rpc");
		final TransportReceiver proxy = new TransportReceiver() {
			@Override
			public void onTransportFailure(final ServerFailure failure) {
				GWT.log("rpc returned");
				loader.hide();
				receiver.onTransportFailure(failure);
			}

			@Override
			public void onTransportSuccess(final String payload) {
				loader.hide();
				GWT.log("rpc returned");
				receiver.onTransportSuccess(payload);
			}
		};
		loader.show();
		super.send(payload, proxy);
	}

	protected RequestCallback createRequestCallback(final TransportReceiver receiver) {
		final RequestCallback callback = super.createRequestCallback(receiver);
		RequestCallback newCallback = new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {
				if (Response.SC_UNAUTHORIZED == response.getStatusCode()) {
					GWT.log("User session timed out or user logged out");
					Window.Location.assign(urlGenerator.getClientSideLoginURL(urlInfo, appDomain, urlInfo.getParameter(QueryString.GWT_DEV_MODE.getKey())));
				} else {
					callback.onResponseReceived(request, response);
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				callback.onError(request, exception);

			}
		};
		return newCallback;
	}
}
