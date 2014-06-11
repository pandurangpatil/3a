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
package com.agnie.useradmin.main.client;

import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.useradmin.common.client.injector.ApplicationACLProvider;
import com.agnie.useradmin.common.client.ui.UACommonViewFactory;
import com.agnie.useradmin.main.client.mvp.ClientFactory;
import com.agnie.useradmin.main.client.mvp.MainAppController;
import com.agnie.useradmin.main.client.ui.ViewFactory;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
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
	private MainAppController	appController;
	@Inject
	UserContext					userContext;
	@Inject
	URLInfo						urlInfo;
	@Inject
	UserAdminURLGenerator		uaug;
	@Inject
	ClientFactory				clientFactory;
	@Inject
	UACommonViewFactory			commonViewFactory;
	@Inject
	ApplicationACLProvider		aclProvider;
	@Inject
	Helper						helper;
	private ViewFactory			viewFactory;

	public void start() {
		// To load useradmin application you need to have selected domain other wise user need to be redirected to
		// landing page.
		final String selectedDomain = Window.Location.getParameter(QueryString.SELECTED_DOMAIN.getKey());
		if (selectedDomain == null || selectedDomain.isEmpty()) {
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
					clientFactory.getuserProfileService().getAdminAclForDomain(new AsyncCallback<AccessControlList>() {

						@Override
						public void onSuccess(AccessControlList result) {
							if (result != null) {
								if (result.check(Permissions.APPLICATION)) {
									aclProvider.set(result);
									loadApplications();
								} else {
									GWT.log("In Useradmin APPLICATION permission denied");
									Window.Location.assign(uaug.getLandingPageUrl(urlInfo, UserAdminURLGenerator.USERADMIN, urlInfo.getParameter(QueryString.GWT_DEV_MODE.getKey())));
								}
							} else {
								GWT.log("In Useradmin getuserProfileService result null");
								Window.Location.assign(uaug.getLandingPageUrl(urlInfo, UserAdminURLGenerator.USERADMIN, urlInfo.getParameter(QueryString.GWT_DEV_MODE.getKey())));
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}
					});

					// To set domain url below header image
					viewFactory.getMenu().setDomain(selectedDomain);
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
		appController.setViewFactory(viewFactory);
		appController.go();
		Document.get().getBody().addClassName("fullpage");
	}
}
