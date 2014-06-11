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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;

public interface ViewFactory {

	HTMLPanel getCenterContentPanel();

	Image getHelpImage();

	HTMLPanel getHelpBreadCrumbPanel();

	BreadCrumbPanel getBreadCrumbPanel();

	NewAppView getCreateNewAppView();

	UpdateProfileView getUpdateProfileView();

	ChangePasswordView getChangePasswordView();

	LandingView getLandingView();

	ListMenu getListMenu();

	Menu getMenu();
}
