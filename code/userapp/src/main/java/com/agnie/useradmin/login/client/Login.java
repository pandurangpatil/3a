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
package com.agnie.useradmin.login.client;

import com.agnie.gwt.common.client.base.BasePageResourceLoader;
import com.agnie.useradmin.common.client.base.UserAdminBasePageResourceLoader;
import com.agnie.useradmin.common.client.helper.LoginQSProcessor;
import com.agnie.useradmin.login.client.injector.ApplicationProvider;
import com.agnie.useradmin.login.client.mvp.LoginAppController;
import com.agnie.useradmin.login.client.mvp.PlatformFactory;
import com.agnie.useradmin.login.client.ui.LoginPageResourceLoader;
import com.agnie.useradmin.persistance.client.service.AuthenticateServiceAsync;
import com.agnie.useradmin.persistance.client.service.dto.Application;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class Login implements EntryPoint {
	private PlatformFactory			platformFactory		= GWT.create(PlatformFactory.class);
	BasePageResourceLoader			bPResourceLoader	= new BasePageResourceLoader();
	UserAdminBasePageResourceLoader	uABPRL				= new UserAdminBasePageResourceLoader();
	LoginPageResourceLoader			lPRL				= new LoginPageResourceLoader();

	@Override
	public void onModuleLoad() {
		clearAppLoader();
		final String domain = LoginQSProcessor.getDomain();
		if (domain != null && !domain.isEmpty()) {
			AuthenticateServiceAsync authService = platformFactory.getInjector().getClientFactory().getAuthenticateService();
			authService.getApplicationDetails(domain, new AsyncCallback<Application>() {

				@Override
				public void onSuccess(Application result) {
					if (result != null) {
						ApplicationProvider appProvider = platformFactory.getInjector().getApplicationProvider();
						platformFactory.getInjector().getCommonViewFactory().getHeaderView().setHeaderImg(result.getIconURL());
						appProvider.set(result);
						loadApplications();
					} else {
						Window.alert("Invalid domain '" + domain + "'");
					}

				}

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}
			});

		} else {
			// If domain is not specified then default domain will be used hence there won't be any issues.
			loadApplications();
		}
	}

	private void clearAppLoader() {
		RootPanel pageLoader = platformFactory.getInjector().getClientFactory().getRootPanelFactory().getPageLoader();
		pageLoader.setVisible(false);
		Document.get().getBody().addClassName("fullpage");
	}

	private void loadApplications() {
		GWT.log("onModuleLoad()");
		LoginAppController appController = platformFactory.getInjector().getAppController();
		appController.setViewFactory(platformFactory.getViewFactory());
		appController.go();

	}

}
