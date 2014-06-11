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
package com.agnie.useradmin.landing.client.mvp;

import com.agnie.gwt.common.client.mvp.AppController;
import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.useradmin.landing.client.Presenter;
import com.agnie.useradmin.landing.client.injector.MVPInjector;
import com.agnie.useradmin.landing.client.ui.ViewFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LandingAppController extends AppController<PlaceToken> {
	private ViewFactory	viewFactory;
	@Inject
	private MVPInjector	injector;

	public LandingAppController() {
		super(PlaceToken.class);
	}

	@Override
	protected HTMLPanel getMainContentRootPanel() {
		return viewFactory.getCenterContentPanel();
	}

	@Override
	protected PlaceToken getDefaultPlace() {
		return PlaceToken.LANDING;
	}

	@Override
	protected Presenter getPresenterForPlace(Place<PlaceToken> place) {
		GWT.log("getPresenterForPlace==");
		Presenter presenter = null;
		if (place != null) {
			switch (place.getPlace()) {
			case LANDING:
				presenter = injector.getLandingPresenter();
				break;
			case CREATE:
				presenter = injector.getNewAppPresenter();
				break;
			case UPDATE_PROFILE:
				presenter = injector.getUpdateProfilePresenter();
				break;
			case CHANGE_PASS:
				presenter = injector.getChangePasswordPresenter();
				break;
			default:
				// TODO: Need to add default case to show error page.
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
