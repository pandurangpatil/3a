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

import com.agnie.gwt.common.client.mvp.AppController;
import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.useradmin.common.client.injector.ContextACLProvider;
import com.agnie.useradmin.contextmgr.client.Presenter;
import com.agnie.useradmin.contextmgr.client.injector.MVPInjector;
import com.agnie.useradmin.contextmgr.client.ui.ViewFactory;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CtxMgrAppController extends AppController<PlaceToken> {

	@Inject
	ContextACLProvider	aclProvider;
	@Inject
	MVPInjector			injector;

	ViewFactory			viewFactory;

	public CtxMgrAppController() {
		super(PlaceToken.class);
	}

	@Override
	protected HTMLPanel getMainContentRootPanel() {
		return viewFactory.getCenterContentPanel();
	}

	@Override
	public PlaceToken getDefaultPlace() {
		GWT.log("In CtxMgrAppController in getDefaultPlace() start");
		if (aclProvider.get().check(Permissions.CONTEXT_USER_MANAGER))
			return PlaceToken.USERS;
		if (aclProvider.get().check(Permissions.CONTEXT_SETTINGS))
			return PlaceToken.CTX_SETTINGS;
		GWT.log("In CtxMgrAppController User Not Authonticated for all tabs");
		return null;
	}

	@Override
	protected Presenter getPresenterForPlace(Place<PlaceToken> place) {
		GWT.log("getPresenterForPlace==" + place);
		Presenter presenter = null;
		if (place != null) {
			switch (place.getPlace()) {
			case USERS:
				presenter = injector.getContextUserPresenter();
				break;
			case CTX_SETTINGS:
				presenter = injector.getContextSettingsPresenter();
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
