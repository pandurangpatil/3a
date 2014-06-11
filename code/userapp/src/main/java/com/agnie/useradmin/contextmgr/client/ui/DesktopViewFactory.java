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
package com.agnie.useradmin.contextmgr.client.ui;

import com.agnie.gwt.common.client.widget.BreadCrumbPanel;
import com.agnie.useradmin.contextmgr.client.presenter.shared.ui.ListMenu;
import com.agnie.useradmin.contextmgr.client.presenter.shared.ui.Menu;
import com.agnie.useradmin.login.client.injector.MVPModule;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class DesktopViewFactory implements ViewFactory {

	@Inject
	@Named(MVPModule.DESKTOP)
	private ContextSettingsView	ctxSettingsView;
	@Inject
	@Named(MVPModule.DESKTOP)
	private ContextUsersView	ctxUsersView;
	@Inject
	private ListMenu			listMenu;
	@Inject
	private Menu				menu;
	private HTMLPanel			centerContentPanel	= new HTMLPanel("");
	private BreadCrumbPanel		breadCrumbPanel;
	private Image				helpImage;
	private HTMLPanel			helpBreadCrumbPanel	= new HTMLPanel("");

	public DesktopViewFactory() {

		// applying float left assuming vertical panel will be always placed
		// inside split panel on left hand side.
		centerContentPanel.addStyleName("content-wrapper");
		centerContentPanel.addStyleName("clearfix");
		helpImage = new Image();
		helpImage.getElement().setAttribute("src", GWT.getModuleBaseURL() + "images/transparent.png");
		helpImage.addStyleName("help-image");
		helpImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open("http://www.agnie.net/#DEMO", "3a-help", "");
			}
		});
		helpBreadCrumbPanel.addStyleName("help-bread-crumb-pan");
	}

	/**
	 * @return the centerContentPanel
	 */
	public HTMLPanel getCenterContentPanel() {
		return centerContentPanel;
	}

	public ListMenu getListMenu() {
		return listMenu;
	}

	public Menu getMenu() {
		return menu;
	}

	/**
	 * 
	 * @return helpImage
	 */
	public Image getHelpImage() {
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

	public ContextSettingsView getCTXSettingsView() {
		return ctxSettingsView;
	}

	public ContextUsersView getCTXUsersView() {
		return ctxUsersView;
	}

}
