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
package com.agnie.useradmin.landing.client;

import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.useradmin.common.client.helper.LoginQSProcessor;
import com.agnie.useradmin.common.client.injector.UserACLProvider;
import com.agnie.useradmin.common.client.ui.UACommonViewFactory;
import com.agnie.useradmin.landing.client.mvp.ClientFactory;
import com.agnie.useradmin.landing.client.mvp.LandingAppController;
import com.agnie.useradmin.landing.client.ui.ViewFactory;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.persistance.client.service.AuthenticateServiceAsync;
import com.agnie.useradmin.persistance.client.service.dto.Application;
import com.agnie.useradmin.session.client.helper.ApplicationLoader;
import com.agnie.useradmin.session.client.helper.Helper;
import com.agnie.useradmin.session.client.helper.UserContext;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AppLoader {
	@Inject
	UserContext				userContext;
	@Inject
	URLInfo					urlInfo;
	@Inject
	LandingAppController	appController;
	@Inject
	UserAdminURLGenerator	uaug;
	@Inject
	ClientFactory			clientFactory;
	@Inject
	UACommonViewFactory		commonViewFactory;
	@Inject
	UserACLProvider			usACLProvider;
	@Inject
	Helper					helper;

	ViewFactory				viewFactory;

	public void start() {
		userContext.loadLoggedInUser(new ApplicationLoader() {

			@Override
			public void onFailure(Throwable caught) {
				helper.logout();
				Window.Location.assign(uaug.getClientSideLoginURL(urlInfo));
			}

			@Override
			public void load() {
				commonViewFactory.getHeaderView().setUserAcc(userContext.getCurrentUser());
				final String domain = LoginQSProcessor.getDomain();
				AuthenticateServiceAsync authService = clientFactory.getAuthenticateService();
				authService.getApplicationDetails(domain, new AsyncCallback<Application>() {

					@Override
					public void onSuccess(Application result) {
						commonViewFactory.getHeaderView().setHeaderImg(result.getIconURL());
						clientFactory.getUserSessionService().getAclForDomain(new AsyncCallback<AccessControlList>() {

							@Override
							public void onSuccess(AccessControlList result) {
								usACLProvider.set(result);
								loadApplications();
							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}
						});
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});

			}
		}, urlInfo, UserAdminURLGenerator.USERADMIN, urlInfo.getParameter(QueryString.GWT_DEV_MODE.getKey()));

	}

	/**
	 * @param viewFactory
	 *            the viewFactory to set
	 */
	public void setViewFactory(ViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}

	private void loadApplications() {
		GWT.log("onModuleLoad()");
		appController.setViewFactory(viewFactory);
		appController.go();
		Document.get().getBody().addClassName("fullpage");
	}

}
