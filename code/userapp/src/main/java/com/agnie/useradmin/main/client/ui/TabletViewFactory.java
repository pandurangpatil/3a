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
package com.agnie.useradmin.main.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.agnie.common.gwt.serverclient.client.renderer.Title;
import com.agnie.gwt.common.client.widget.BreadCrumbPanel;
import com.agnie.gwt.common.client.widget.CustomListBox;
import com.agnie.useradmin.common.client.renderer.TitleCell;
import com.agnie.useradmin.login.client.injector.MVPModule;
import com.agnie.useradmin.main.client.presenter.sahered.ui.ListMenu;
import com.agnie.useradmin.main.client.presenter.sahered.ui.Menu;
import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class TabletViewFactory implements ViewFactory {
	@Inject
	@Named(MVPModule.DESKTOP)
	private ApplicationUsersView	usersView;
	@Inject
	@Named(MVPModule.DESKTOP)
	private RolesView				rolesView;
	@Inject
	@Named(MVPModule.DESKTOP)
	private DomainParentView		domainParentView;
	@Inject
	@Named(MVPModule.DESKTOP)
	private PermissionsView			permissionsView;
	@Inject
	@Named(MVPModule.DESKTOP)
	private ManageContextsView		manageContextsView;
	@Inject
	private DomainReadView			domainReadView;
	@Inject
	private DomainEditView			domainEditView;
	@Inject
	private ListMenu				listMenu;
	@Inject
	private Menu					menu;
	private HTMLPanel				centerContentPanel	= new HTMLPanel("");
	private BreadCrumbPanel			breadCrumbPanel;
	private Image					helpImage			= new Image();
	private HTMLPanel				helpBreadCrumbPanel	= new HTMLPanel("");
	private CustomListBox<Title>	levelFilter;
	private List<Title>				levelWrapList		= new ArrayList<Title>();

	public static class AuthLevelWrapper implements Title {
		private AuthLevel	level;

		public AuthLevelWrapper(AuthLevel level) {
			this.level = level;
		}

		/**
		 * @return the level
		 */
		public AuthLevel getLevel() {
			return level;
		}

		@Override
		public String getTitle() {
			return this.level.name();
		}

	}

	public TabletViewFactory() {

		// applying float left assuming vertical panel will be always placed
		// inside split panel on left hand side.
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

	public ApplicationUsersView getAppUsersView() {
		return usersView;
	}

	public DomainParentView getDomainParentView() {
		return domainParentView;
	}

	public RolesView getListRolesView() {
		return rolesView;
	}

	public PermissionsView getListPermissionsView() {
		return permissionsView;
	}

	public ManageContextsView getManageContextsView() {
		return manageContextsView;
	}

	public CustomListBox<Title> getLevelFilter() {
		if (levelFilter == null) {
			AuthLevelWrapper appWrapper = new AuthLevelWrapper(AuthLevel.APPLICATION);
			levelWrapList.add(appWrapper);
			levelWrapList.add(new AuthLevelWrapper(AuthLevel.CONTEXT));
			levelFilter = new CustomListBox<Title>(new TitleCell());
			levelFilter.setList(levelWrapList);
			levelFilter.setSelectedItem(appWrapper, true);
		}
		return levelFilter;
	}

	public DomainReadView getDomainReadView() {
		return domainReadView;
	}

	public DomainEditView getDomainEditView() {
		return domainEditView;
	}

}
