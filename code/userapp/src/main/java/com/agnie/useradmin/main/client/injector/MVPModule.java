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
package com.agnie.useradmin.main.client.injector;

import com.agnie.useradmin.common.client.injector.CommonModule;
import com.agnie.useradmin.main.client.ui.ApplicationUsersView;
import com.agnie.useradmin.main.client.ui.DeskApplicationUsersView;
import com.agnie.useradmin.main.client.ui.DeskDomainParentView;
import com.agnie.useradmin.main.client.ui.DeskManageContextsView;
import com.agnie.useradmin.main.client.ui.DeskPermissionsView;
import com.agnie.useradmin.main.client.ui.DeskRolesView;
import com.agnie.useradmin.main.client.ui.DomainParentView;
import com.agnie.useradmin.main.client.ui.ManageContextsView;
import com.agnie.useradmin.main.client.ui.PermissionsView;
import com.agnie.useradmin.main.client.ui.RolesView;
import com.google.inject.name.Names;

public class MVPModule extends CommonModule {

	@Override
	protected void configure() {
		super.configure();
		bind(ApplicationUsersView.class).annotatedWith(Names.named(DESKTOP)).to(DeskApplicationUsersView.class);
		bind(ApplicationUsersView.class).annotatedWith(Names.named(MOBILE)).to(DeskApplicationUsersView.class);
		bind(ApplicationUsersView.class).annotatedWith(Names.named(TABLET)).to(DeskApplicationUsersView.class);

		bind(DomainParentView.class).annotatedWith(Names.named(DESKTOP)).to(DeskDomainParentView.class);
		bind(DomainParentView.class).annotatedWith(Names.named(MOBILE)).to(DeskDomainParentView.class);
		bind(DomainParentView.class).annotatedWith(Names.named(TABLET)).to(DeskDomainParentView.class);

		bind(RolesView.class).annotatedWith(Names.named(DESKTOP)).to(DeskRolesView.class);
		bind(RolesView.class).annotatedWith(Names.named(MOBILE)).to(DeskRolesView.class);
		bind(RolesView.class).annotatedWith(Names.named(TABLET)).to(DeskRolesView.class);

		bind(PermissionsView.class).annotatedWith(Names.named(DESKTOP)).to(DeskPermissionsView.class);
		bind(PermissionsView.class).annotatedWith(Names.named(MOBILE)).to(DeskPermissionsView.class);
		bind(PermissionsView.class).annotatedWith(Names.named(TABLET)).to(DeskPermissionsView.class);

		bind(ManageContextsView.class).annotatedWith(Names.named(DESKTOP)).to(DeskManageContextsView.class);
		bind(ManageContextsView.class).annotatedWith(Names.named(MOBILE)).to(DeskManageContextsView.class);
		bind(ManageContextsView.class).annotatedWith(Names.named(TABLET)).to(DeskManageContextsView.class);
	}
}
