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
package com.agnie.useradmin.session.client.helper;

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.gwt.serverclient.client.helper.URLGenerator;
import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.useradmin.session.client.mvp.ClientFactory;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UserContext {

	private UserAccount		currentUser;
	@Inject
	private URLGenerator	usug;
	@Inject
	Helper					helper;
	@Inject
	ClientFactory			clientFactory;

	public UserAccount getCurrentUser() {
		return currentUser;
	}

	public void loadLoggedInUser(final ApplicationLoader appLoader, final URLInfo params, final String domain, final String devMode) {
		clientFactory.getUserSessionService().getLoggedInUserAccount(new AsyncCallback<UserAccount>() {

			@Override
			public void onSuccess(UserAccount result) {
				if (result != null) {
					currentUser = result;
					appLoader.load();
				} else {
					helper.logout();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				helper.logout();
				appLoader.onFailure(caught);
				Window.Location.assign(usug.getClientSideLoginURL(params, domain, devMode));
			}
		});
	}

	public void logoutUser(final URLInfo params, final String domain, final String devMode) {
		clientFactory.getUserSessionService().logout(new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				Window.Location.assign(usug.getClientSideLoginURL(params, domain, devMode));
			}

			@Override
			public void onFailure(Throwable caught) {
				helper.logout();
				Window.Location.assign(usug.getClientSideLoginURL(params, domain, devMode));
			}
		});
	}

}
