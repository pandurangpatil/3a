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

import com.agnie.gwt.common.client.mvp.AppController;
import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.useradmin.login.client.Presenter;
import com.agnie.useradmin.login.client.injector.MVPInjector;
import com.agnie.useradmin.login.client.ui.ViewFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LoginAppController extends AppController<PlaceToken> {
	private ViewFactory	viewFactory;
	@Inject
	private MVPInjector	injector;

	public LoginAppController() {
		super(PlaceToken.class);
	}

	@Override
	protected HTMLPanel getMainContentRootPanel() {
		return viewFactory.getCenterContentPanel();
	}

	@Override
	protected PlaceToken getDefaultPlace() {
		return PlaceToken.LOGIN;
	}

	@Override
	protected Presenter getPresenterForPlace(Place<PlaceToken> place) {
		GWT.log("getPresenterForPlace==");
		Presenter presenter = null;
		if (place != null) {
			switch (place.getPlace()) {
			case LOGIN:
				GWT.log("AppController case Login place");
				presenter = injector.getLoginPresenter();
				break;
			case FGT_PWD:
				GWT.log("AppController case Forgot password place");
				presenter = injector.getForgotPasswordPresenter();
				break;
			case FGT_CPWD:
				GWT.log("AppController case Change password link sent after forgot password");
				presenter = injector.getFPChangePwdPresenter();
				break;
			case SIGN_UP:
				GWT.log("AppController case Signup place");
				presenter = injector.getSignupPresenter();
				break;
			case VERIFY:
				GWT.log("AppController case verify email place");
				presenter = injector.getVerifyPresenter();
				break;
			default:
				getPlaceManager().changePlace(new Place<PlaceToken>(getDefaultPlace()));
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
