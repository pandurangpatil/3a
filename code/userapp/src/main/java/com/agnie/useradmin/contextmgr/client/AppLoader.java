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
package com.agnie.useradmin.contextmgr.client;

import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.useradmin.common.client.injector.ContextACLProvider;
import com.agnie.useradmin.common.client.ui.UACommonViewFactory;
import com.agnie.useradmin.contextmgr.client.mvp.ClientFactory;
import com.agnie.useradmin.contextmgr.client.mvp.CtxMgrAppController;
import com.agnie.useradmin.contextmgr.client.ui.ViewFactory;
import com.agnie.useradmin.persistance.client.helper.Permissions;
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
	UserAdminURLGenerator	uaug;
	@Inject
	UACommonViewFactory		commonViewFactory;
	@Inject
	ClientFactory			clientFactory;
	@Inject
	CtxMgrAppController		appController;
	@Inject
	ContextACLProvider		ctxAclProvider;
	@Inject
	Helper					helper;
	ViewFactory				viewFactory;

	public void start() {
		// To load Context Manager application you need to have selected domain as well as selected context other wise
		// user need to be redirected to
		// landing page.
		final String selectedDomain = Window.Location.getParameter(QueryString.SELECTED_DOMAIN.getKey());
		final String selectedContext = Window.Location.getParameter(QueryString.SELECTED_CONTEXT.getKey());
		if (selectedDomain == null || selectedDomain.isEmpty() || selectedContext == null || selectedContext.isEmpty()) {
			Window.Location.assign(uaug.getLandingPageUrl(urlInfo, UserAdminURLGenerator.USERADMIN, urlInfo.getParameter(QueryString.GWT_DEV_MODE.getKey())));
		} else {
			userContext.loadLoggedInUser(new ApplicationLoader() {

				@Override
				public void onFailure(Throwable caught) {
					helper.logout();
					Window.Location.assign(uaug.getClientSideLoginURL(urlInfo));
				}

				@Override
				public void load() {
					commonViewFactory.getHeaderView().setUserAcc(userContext.getCurrentUser());
					viewFactory.getMenu().setDomain(selectedDomain, selectedContext);
					AuthenticateServiceAsync authService = clientFactory.getAuthenticateService();
					authService.getApplicationDetails(selectedDomain, new AsyncCallback<Application>() {

						@Override
						public void onSuccess(Application result) {
							commonViewFactory.getHeaderView().setHeaderImg(result.getIconURL());
							// To set domain url below header image
							clientFactory.getuserProfileService().getAdminAclForContext(new AsyncCallback<AccessControlList>() {

								@Override
								public void onSuccess(AccessControlList result) {
									if (result != null) {
										if (result.check(Permissions.CONTEXT)) {
											ctxAclProvider.set(result);
											loadApplications();
										} else {
											Window.Location.assign(uaug.getLandingPageUrl(urlInfo, UserAdminURLGenerator.USERADMIN, urlInfo.getParameter(QueryString.GWT_DEV_MODE.getKey())));
										}
									} else {
										Window.Location.assign(uaug.getLandingPageUrl(urlInfo, UserAdminURLGenerator.USERADMIN, urlInfo.getParameter(QueryString.GWT_DEV_MODE.getKey())));
									}
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
