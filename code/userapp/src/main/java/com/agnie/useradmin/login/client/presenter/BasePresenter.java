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
package com.agnie.useradmin.login.client.presenter;

import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.gwt.common.client.widget.MessagePanel;
import com.agnie.useradmin.common.client.ui.UACommonViewFactory;
import com.agnie.useradmin.login.client.Presenter;
import com.agnie.useradmin.login.client.mvp.ClientFactory;
import com.agnie.useradmin.login.client.mvp.LoginAppController;
import com.agnie.useradmin.login.client.mvp.PlaceToken;
import com.agnie.useradmin.login.client.ui.ViewFactory;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLInfo;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;

public abstract class BasePresenter implements Presenter {
	@Inject
	protected UserAdminURLGenerator	uug;
	@Inject
	protected ClientFactory			clientFactory;
	@Inject
	protected UserAdminURLInfo		params;
	@Inject
	protected UACommonViewFactory	commonViewFactory;
	@Inject
	protected LoginAppController	appController;
	@Inject
	protected MessagePanel			messagePanel;
	protected ViewFactory			viewFactory;
	protected Place<PlaceToken>		place;

	/**
	 * @param viewFactory
	 *            the viewFactory to set
	 */
	public void setViewFactory(ViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}

	/**
	 * @param place
	 *            the place to set
	 */
	public void setPlace(Place<PlaceToken> place) {
		this.place = place;
	}

	public boolean go() {
		// Headers are getting pupulated here
		RootPanel headerPanel = clientFactory.getRootPanelFactory().getHeaderPanel();
		headerPanel.clear();
		headerPanel.add(commonViewFactory.getHeaderView());

		// Footer is getting populated here.
		RootPanel footerPanel = clientFactory.getRootPanelFactory().getFooterPanel();
		footerPanel.clear();
		footerPanel.add(commonViewFactory.getFooterView());
		return true;
	}

	public boolean checkPermission(String permission) {
		// As there is no permission check being done on login screens it will always return true
		return true;
	}

}
