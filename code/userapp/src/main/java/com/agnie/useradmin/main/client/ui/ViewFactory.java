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

import com.agnie.common.gwt.serverclient.client.renderer.Title;
import com.agnie.gwt.common.client.widget.BreadCrumbPanel;
import com.agnie.gwt.common.client.widget.CustomListBox;
import com.agnie.useradmin.main.client.presenter.sahered.ui.ListMenu;
import com.agnie.useradmin.main.client.presenter.sahered.ui.Menu;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;

public interface ViewFactory {

	HTMLPanel getCenterContentPanel();

	ListMenu getListMenu();

	Menu getMenu();

	Image getHelpImage();

	HTMLPanel getHelpBreadCrumbPanel();

	BreadCrumbPanel getBreadCrumbPanel();

	ApplicationUsersView getAppUsersView();

	DomainParentView getDomainParentView();

	RolesView getListRolesView();

	PermissionsView getListPermissionsView();

	ManageContextsView getManageContextsView();

	CustomListBox<Title> getLevelFilter();

}
