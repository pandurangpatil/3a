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
package com.agnie.useradmin.contextmgr.client.injector;

import com.agnie.useradmin.common.client.injector.CommonModule;
import com.agnie.useradmin.contextmgr.client.ui.ContextSettingsView;
import com.agnie.useradmin.contextmgr.client.ui.ContextUsersView;
import com.agnie.useradmin.contextmgr.client.ui.DeskContextSettingsView;
import com.agnie.useradmin.contextmgr.client.ui.DeskContextUsersView;
import com.google.inject.name.Names;

public class MVPModule extends CommonModule {

	@Override
	protected void configure() {
		super.configure();
		bind(ContextUsersView.class).annotatedWith(Names.named(DESKTOP)).to(DeskContextUsersView.class);
		bind(ContextUsersView.class).annotatedWith(Names.named(MOBILE)).to(DeskContextUsersView.class);
		bind(ContextUsersView.class).annotatedWith(Names.named(TABLET)).to(DeskContextUsersView.class);

		bind(ContextSettingsView.class).annotatedWith(Names.named(DESKTOP)).to(DeskContextSettingsView.class);
		bind(ContextSettingsView.class).annotatedWith(Names.named(MOBILE)).to(DeskContextSettingsView.class);
		bind(ContextSettingsView.class).annotatedWith(Names.named(TABLET)).to(DeskContextSettingsView.class);

	}
}
