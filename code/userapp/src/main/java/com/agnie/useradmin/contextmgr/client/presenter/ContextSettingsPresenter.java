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

import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.gwt.common.client.widget.BreadCrumbPanel;
import com.agnie.useradmin.contextmgr.client.I18;
import com.agnie.useradmin.contextmgr.client.mvp.PlaceToken;
import com.agnie.useradmin.contextmgr.client.presenter.shared.ui.ListMenu;
import com.agnie.useradmin.contextmgr.client.ui.ContextSettingsView;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ContextSettingsPresenter extends BasePresenter {

	ContextSettingsView		view;
	@Inject
	URLInfo					urlInfo;
	@Inject
	UserAdminURLGenerator	uaug;

	@Override
	public boolean go() {
		if (checkPermission(Permissions.CONTEXT_SETTINGS)) {
			super.go();
			RootPanel contentPanel = clientFactory.getRootPanelFactory().getContentPanel();

			// BreadCrumbPanel getting populated here
			BreadCrumbPanel breadCrumbPanel = viewFactory.getBreadCrumbPanel();
			breadCrumbPanel.clear();
			breadCrumbPanel.addBreadCrumb(I18.messages.context_settings());
			viewFactory.getListMenu().selectTab(ListMenu.Tab.CONTEXT_SETTINGS.getIndex());
			HTMLPanel centerPanel = viewFactory.getCenterContentPanel();
			view = viewFactory.getCTXSettingsView();
			view.init(Window.Location.getParameter(QueryString.SELECTED_CONTEXT.getKey()));
			centerPanel.add(view);
			contentPanel.add(centerPanel);
			return true;
		} else {
			Scheduler.get().scheduleDeferred(new Command() {
				public void execute() {
					appController.getPlaceManager().changePlace(new Place<PlaceToken>(appController.getDefaultPlace()));
				}
			});
			return false;
		}
	}

	@Override
	public void postRender() {
	}

	// public void transferOwnerShip(String username) {
	// ContextManagerRequest cmr = clientFactory.getRequestFactory().contextManager();
	// cmr.transferOwnerShip(username).fire(new Receiver<Void>() {
	//
	// @Override
	// public void onSuccess(Void response) {
	// Window.alert(I18.messages.owner_transferred());
	// Window.Location.assign(uaug.getLandingPageUrl(urlInfo, UserAdminURLGenerator.USERADMIN,
	// urlInfo.getParameter(QueryString.GWT_DEV_MODE.getKey())));
	// }
	//
	// public void onFailure(ServerFailure error) {
	// messagePanel.show(true);
	// messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
	// messagePanel.setType(MessageType.ERROR);
	// }
	// });
	// }

}
