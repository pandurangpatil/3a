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
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * RpcRequest builder to show modal loader .gif image before making call and hide once we receive the response.
 * 
 */
@Singleton
public class LoaderRpcRequestBuilder extends RpcRequestBuilder {
	@Inject
	@Named(CommonServerClientModule.CURRENT_APP_DOMAIN)
	String			appDomain;
	@Inject
	URLGenerator	urlGenerator;
	@Inject
	URLInfo			urlInfo;

	private Loader	loader;

	private class RequestCallbackWrapper implements RequestCallback {

		private RequestCallback	callback;

		RequestCallbackWrapper(RequestCallback aCallback) {
			this.callback = aCallback;
		}

		@Override
		public void onResponseReceived(Request request, Response response) {
			loader.hide();
			if (Response.SC_UNAUTHORIZED == response.getStatusCode()) {
				GWT.log("User session timed out or user logged out");
				Window.Location.assign(urlGenerator.getClientSideLoginURL(urlInfo, appDomain, urlInfo.getParameter(QueryString.GWT_DEV_MODE.getKey())));
			} else {
				callback.onResponseReceived(request, response);
			}
		}

		@Override
		public void onError(Request request, Throwable exception) {
			loader.hide();
			callback.onError(request, exception);
		}

	}

	public LoaderRpcRequestBuilder(Loader loader) {
		this.loader = loader;
		loader.hide();
	}

	public LoaderRpcRequestBuilder() {
		this(new Loader());
	}

	@Override
	protected RequestBuilder doCreate(String serviceEntryPoint) {
		RequestBuilder rb = super.doCreate(serviceEntryPoint);
		loader.show();
		return rb;
	}

	@Override
	protected void doFinish(RequestBuilder rb) {
		super.doFinish(rb);
		rb.setCallback(new RequestCallbackWrapper(rb.getCallback()));
	}
}
