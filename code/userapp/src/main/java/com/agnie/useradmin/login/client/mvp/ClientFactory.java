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
package com.agnie.useradmin.login.client.mvp;

import com.agnie.useradmin.persistance.client.service.AuthenticateService;
import com.agnie.useradmin.persistance.client.service.AuthenticateServiceAsync;
import com.agnie.useradmin.persistance.client.service.UserProfileService;
import com.agnie.useradmin.persistance.client.service.UserProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.inject.Singleton;

@Singleton
public class ClientFactory extends com.agnie.useradmin.session.client.mvp.ClientFactory {

	private AuthenticateServiceAsync	authenticateService;
	private UserProfileServiceAsync		userProfileService;

	private RootPanelFactory			rootPanelFactory	= GWT.create(RootPanelFactory.class);

	/**
	 * @return the rootPanelFactory
	 */
	public RootPanelFactory getRootPanelFactory() {
		return rootPanelFactory;
	}

	public AuthenticateServiceAsync getAuthenticateService() {
		if (authenticateService == null) {
			authenticateService = GWT.create(AuthenticateService.class);
			ServiceDefTarget target = (ServiceDefTarget) authenticateService;
			target.setRpcRequestBuilder(loaderBuilder);
		}
		return authenticateService;
	}

	public UserProfileServiceAsync getUserProfileService() {
		if (userProfileService == null) {
			userProfileService = GWT.create(UserProfileService.class);
			ServiceDefTarget target = (ServiceDefTarget) userProfileService;
			target.setRpcRequestBuilder(loaderBuilder);
		}
		return userProfileService;
	}
}
