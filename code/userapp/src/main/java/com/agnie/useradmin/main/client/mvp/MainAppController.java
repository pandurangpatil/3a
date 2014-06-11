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
package com.agnie.useradmin.main.client.mvp;

import com.agnie.gwt.common.client.mvp.AppController;
import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.useradmin.common.client.injector.ApplicationACLProvider;
import com.agnie.useradmin.main.client.Presenter;
import com.agnie.useradmin.main.client.injector.MVPInjector;
import com.agnie.useradmin.main.client.ui.ViewFactory;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MainAppController extends AppController<PlaceToken> {

	@Inject
	ApplicationACLProvider	appACLProvider;
	@Inject
	MVPInjector				injector;

	ViewFactory				viewFactory;

	public MainAppController() {
		super(PlaceToken.class);
	}

	@Override
	protected HTMLPanel getMainContentRootPanel() {
		return viewFactory.getCenterContentPanel();
	}

	@Override
	public PlaceToken getDefaultPlace() {
		if (appACLProvider.get().check(Permissions.APPLICATION_USER_MANAGER))
			return PlaceToken.APP_USERS;
		if (appACLProvider.get().check(Permissions.ROLE))
			return PlaceToken.ROLES;
		if (appACLProvider.get().check(Permissions.PERMISSION))
			return PlaceToken.PERMISSION;
		if (appACLProvider.get().check(Permissions.DOMAIN_SETTINGS))
			return PlaceToken.DOMAIN_SETTINGS;
		if (appACLProvider.get().check(Permissions.MANAGE_CONTEXT))
			return PlaceToken.CTXS;
		GWT.log("In MainAppController User Not Authonticated for all tabs");
		return null;

	}

	@Override
	protected Presenter getPresenterForPlace(Place<PlaceToken> place) {
		GWT.log("getPresenterForPlace==" + place);
		Presenter presenter = null;
		if (place != null) {
			switch (place.getPlace()) {
			case APP_USERS:
				presenter = injector.getApplicationUserPresenter();
				break;
			case ROLES:
				presenter = injector.getRolesPresenter();
				break;
			case PERMISSION:
				presenter = injector.getPermissionsPresenter();
				break;
			case DOMAIN_SETTINGS:
				presenter = injector.getDomainPresenter();
				break;
			case CTXS:
				presenter = injector.getManageContextPresenter();
				break;
			default:
				// TODO : Need to add default case to show error page.
			}
		}
		if (presenter != null) {
			presenter.setViewFactory(viewFactory);
			presenter.setPlace(place);
		}
		return presenter;
	}

	/**
	 * @param viewFactory
	 *            the viewFactory to set
	 */
	public void setViewFactory(ViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}
}
