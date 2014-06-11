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
package com.agnie.useradmin.landing.client.ui;

import com.agnie.gwt.common.client.widget.BreadCrumbPanel;
import com.agnie.useradmin.landing.client.presenter.shared.ui.ListMenu;
import com.agnie.useradmin.landing.client.presenter.shared.ui.Menu;
import com.agnie.useradmin.login.client.injector.MVPModule;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class MobileViewFactory implements ViewFactory {

	private HTMLPanel			centerContentPanel	= new HTMLPanel("");
	@Inject
	@Named(MVPModule.DESKTOP)
	private NewAppView			createNewAppView;
	@Inject
	@Named(MVPModule.DESKTOP)
	private UpdateProfileView	updateProfileView;
	@Inject
	@Named(MVPModule.DESKTOP)
	private ChangePasswordView	changePasswordView;
	@Inject
	@Named(MVPModule.DESKTOP)
	private LandingView			landingView;
	@Inject
	private Menu				menu;
	@Inject
	private ListMenu			listMenu;
	private BreadCrumbPanel		breadCrumbPanel;
	private Image				helpImage			= new Image();
	private HTMLPanel			helpBreadCrumbPanel	= new HTMLPanel("");

	public MobileViewFactory() {

		// applying float left assuming vertical panel will be always placed inside split panel on left hand side.
		centerContentPanel.addStyleName("content-wrapper");
		centerContentPanel.addStyleName("clearfix");
		helpImage.addStyleName("help-image");
		helpBreadCrumbPanel.addStyleName("help-bread-crumb-pan");
	}

	/**
	 * @return the centerContentPanel
	 */
	public HTMLPanel getCenterContentPanel() {
		return centerContentPanel;
	}

	/**
	 * 
	 * @return helpImage
	 */
	public Image getHelpImage() {
		helpImage.getElement().setAttribute("src", GWT.getModuleBaseURL() + "images/transparent.png");
		return helpImage;
	}

	/**
	 * 
	 * @return helpBreadCrumbPanel
	 */
	public HTMLPanel getHelpBreadCrumbPanel() {
		return helpBreadCrumbPanel;
	}

	public BreadCrumbPanel getBreadCrumbPanel() {
		if (breadCrumbPanel == null) {
			breadCrumbPanel = new BreadCrumbPanel();
			breadCrumbPanel.addStyleName("float-left");
		}
		return breadCrumbPanel;
	}

	public NewAppView getCreateNewAppView() {
		return createNewAppView;
	}

	public UpdateProfileView getUpdateProfileView() {
		return updateProfileView;
	}

	public ChangePasswordView getChangePasswordView() {
		return changePasswordView;
	}

	public LandingView getLandingView() {
		return landingView;
	}

	public ListMenu getListMenu() {
		return listMenu;
	}

	public Menu getMenu() {
		return menu;
	}
}
