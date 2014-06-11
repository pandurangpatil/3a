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
package com.agnie.useradmin.contextmgr.client.presenter;

import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.gwt.common.client.widget.BreadCrumbPanel;
import com.agnie.gwt.common.client.widget.MessagePanel;
import com.agnie.gwt.common.client.widget.TabBar;
import com.agnie.useradmin.common.client.injector.ContextACLProvider;
import com.agnie.useradmin.common.client.ui.HeaderView;
import com.agnie.useradmin.common.client.ui.UACommonViewFactory;
import com.agnie.useradmin.contextmgr.client.I18;
import com.agnie.useradmin.contextmgr.client.Presenter;
import com.agnie.useradmin.contextmgr.client.mvp.ClientFactory;
import com.agnie.useradmin.contextmgr.client.mvp.CtxMgrAppController;
import com.agnie.useradmin.contextmgr.client.mvp.PlaceToken;
import com.agnie.useradmin.contextmgr.client.presenter.shared.ui.Menu;
import com.agnie.useradmin.contextmgr.client.ui.ViewFactory;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;

public abstract class BasePresenter implements Presenter {
	@Inject
	UserAdminURLGenerator			uaUrlGenerator;
	@Inject
	protected ClientFactory			clientFactory;
	@Inject
	protected ContextACLProvider	ctxAclProvider;
	@Inject
	protected UACommonViewFactory	commonViewFactory;
	@Inject
	protected CtxMgrAppController	appController;
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

		// Menu is getting added here
		TabBar mainMenu = viewFactory.getListMenu().getTabBar();
		Menu menu = viewFactory.getMenu();
		menu.getTabbarPan().add(mainMenu);

		// BreadCrumbPanel getting populated here
		BreadCrumbPanel breadCrumbPanel = viewFactory.getBreadCrumbPanel();
		breadCrumbPanel.clear();
		breadCrumbPanel.addBreadCrumb(I18.messages.users());
		breadCrumbPanel.getBreadCrumb(0).addClickHandler(getBreadCrumbClickHandler());

		Image help = viewFactory.getHelpImage();
		HTMLPanel helpBreadCrumbPanel = viewFactory.getHelpBreadCrumbPanel();
		helpBreadCrumbPanel.clear();
		helpBreadCrumbPanel.add(breadCrumbPanel);
		helpBreadCrumbPanel.add(help);

		// CenterPanel getting populated here
		HTMLPanel centerPanel = viewFactory.getCenterContentPanel();
		centerPanel.clear();
		centerPanel.add(menu);
		centerPanel.add(messagePanel);
		centerPanel.add(helpBreadCrumbPanel);

		// Contents are getting populated here
		RootPanel contentPanel = clientFactory.getRootPanelFactory().getContentPanel();
		contentPanel.clear();
		contentPanel.add(centerPanel);

		// Footer is getting populated here.
		RootPanel footerPanel = clientFactory.getRootPanelFactory().getFooterPanel();
		footerPanel.clear();
		footerPanel.add(commonViewFactory.getFooterView());
		return true;
	}

	private ClickHandler getBreadCrumbClickHandler() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO:uaUrlGenerator.getContextManagerHomePageUrl
				/*
				 * Window.Location.assign(uaUrlGenerator.getLandingPageUrl(ClientFactory.getUrlparams(),
				 * UserAdminURLGenerator.USERADMIN,
				 * ClientFactory.getUrlparams().getParameter(QueryString.GWT_DEV_MODE.getKey())));
				 */
			}
		};
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
		AccessControlList acl = ctxAclProvider.get();
		if (acl != null)
			return acl.check(permission);
		else
			return false;
	}
}
