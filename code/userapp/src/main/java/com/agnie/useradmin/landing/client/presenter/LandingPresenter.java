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

import java.util.List;

import com.agnie.gwt.common.client.widget.BreadCrumbPanel;
import com.agnie.useradmin.landing.client.I18;
import com.agnie.useradmin.landing.client.presenter.shared.ui.ListMenu;
import com.agnie.useradmin.landing.client.presenter.shared.ui.Menu;
import com.agnie.useradmin.landing.client.ui.LandingView;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.shared.proxy.ApplicationPx;
import com.agnie.useradmin.persistance.shared.service.ApplicationManagerRequest;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

@Singleton
public class LandingPresenter extends LandingBasePresenter {

	@Override
	public boolean go() {
		super.go();
		RootPanel contentPanel = clientFactory.getRootPanelFactory().getContentPanel();
		contentPanel.clear();
		// Menu is getting added here
		Menu menu = viewFactory.getMenu();
		ListMenu listMenu = viewFactory.getListMenu();
		if (checkPermission(Permissions.CREATE_APPLICATION)) {
			listMenu.getTabbarPan().add(menu);
		}

		// BreadCrumbPanel getting populated here
		BreadCrumbPanel breadCrumbPanel = viewFactory.getBreadCrumbPanel();
		breadCrumbPanel.clear();
		breadCrumbPanel.addBreadCrumb(I18.messages.userHome());

		Image help = viewFactory.getHelpImage();
		HTMLPanel helpBreadCrumbPanel = viewFactory.getHelpBreadCrumbPanel();
		helpBreadCrumbPanel.clear();
		helpBreadCrumbPanel.add(breadCrumbPanel);
		helpBreadCrumbPanel.add(help);

		HTMLPanel centerPanel = viewFactory.getCenterContentPanel();
		centerPanel.clear();
		centerPanel.add(listMenu);
		messagePanel.addStyleName("message-pan-landing");
		centerPanel.add(messagePanel);
		centerPanel.add(helpBreadCrumbPanel);
		final LandingView view = viewFactory.getLandingView();
		view.clearLandingView();
		centerPanel.add(view);
		contentPanel.add(centerPanel);

		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		amr.getRegisteredApps().fire(new Receiver<List<ApplicationPx>>() {

			@Override
			public void onSuccess(List<ApplicationPx> response) {
				if (response != null && response.size() > 0) {
					view.setAppList(response);
				}
			}

			@Override
			public void onFailure(ServerFailure error) {
				Window.alert("There is some error in GettingRegApp" + error.getExceptionType());
			}
		});
		return true;
	}

	@Override
	public void postRender() {

	}
}
