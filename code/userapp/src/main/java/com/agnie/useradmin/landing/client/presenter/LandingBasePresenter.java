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
package com.agnie.useradmin.landing.client.presenter;

import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.gwt.common.client.widget.MessagePanel;
import com.agnie.useradmin.common.client.injector.UserACLProvider;
import com.agnie.useradmin.common.client.ui.HeaderView;
import com.agnie.useradmin.common.client.ui.UACommonViewFactory;
import com.agnie.useradmin.landing.client.Presenter;
import com.agnie.useradmin.landing.client.mvp.ClientFactory;
import com.agnie.useradmin.landing.client.mvp.LandingAppController;
import com.agnie.useradmin.landing.client.mvp.PlaceToken;
import com.agnie.useradmin.landing.client.ui.ViewFactory;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.session.client.helper.UserContext;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;

public abstract class LandingBasePresenter implements Presenter {
	@Inject
	protected UserACLProvider		usACLProvider;
	@Inject
	protected ClientFactory			clientFactory;
	@Inject
	protected UserAdminURLGenerator	uaUrlGenerator;
	@Inject
	protected UACommonViewFactory	commonViewFactory;
	@Inject
	protected LandingAppController	appController;
	@Inject
	protected UserContext			userContext;
	@Inject
	protected URLInfo				urlInfo;
	@Inject
	protected MessagePanel			messagePanel;
	protected ViewFactory			viewFactory;
	protected Place<PlaceToken>		place;

	public boolean go() {
		// Headers are getting pupulated here
		RootPanel headerPanel = clientFactory.getRootPanelFactory().getHeaderPanel();
		headerPanel.clear();
		HeaderView view = commonViewFactory.getHeaderView();
		view.setAccPanVisible(true);
		headerPanel.add(view);

		// Footer is getting populated here.
		RootPanel footerPanel = clientFactory.getRootPanelFactory().getFooterPanel();
		footerPanel.clear();
		footerPanel.add(commonViewFactory.getFooterView());
		return true;
	}

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

	public boolean checkPermission(String permission) {
		AccessControlList acl = usACLProvider.get();
		return (acl != null ? acl.check(permission) : false);
	}
}
