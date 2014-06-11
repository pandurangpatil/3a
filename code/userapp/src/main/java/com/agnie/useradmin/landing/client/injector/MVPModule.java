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
package com.agnie.useradmin.landing.client.injector;

import com.agnie.useradmin.common.client.injector.CommonModule;
import com.agnie.useradmin.landing.client.ui.ChangePasswordView;
import com.agnie.useradmin.landing.client.ui.DeskChangePasswordView;
import com.agnie.useradmin.landing.client.ui.DeskLandingView;
import com.agnie.useradmin.landing.client.ui.DeskNewAppView;
import com.agnie.useradmin.landing.client.ui.DeskUpdateProfileView;
import com.agnie.useradmin.landing.client.ui.LandingView;
import com.agnie.useradmin.landing.client.ui.NewAppView;
import com.agnie.useradmin.landing.client.ui.UpdateProfileView;
import com.google.inject.name.Names;

public class MVPModule extends CommonModule {

	@Override
	protected void configure() {
		super.configure();

		bind(NewAppView.class).annotatedWith(Names.named(DESKTOP)).to(DeskNewAppView.class);
		bind(NewAppView.class).annotatedWith(Names.named(MOBILE)).to(DeskNewAppView.class);
		bind(NewAppView.class).annotatedWith(Names.named(TABLET)).to(DeskNewAppView.class);

		bind(LandingView.class).annotatedWith(Names.named(DESKTOP)).to(DeskLandingView.class);
		bind(LandingView.class).annotatedWith(Names.named(MOBILE)).to(DeskLandingView.class);
		bind(LandingView.class).annotatedWith(Names.named(TABLET)).to(DeskLandingView.class);

		bind(UpdateProfileView.class).annotatedWith(Names.named(DESKTOP)).to(DeskUpdateProfileView.class);
		bind(UpdateProfileView.class).annotatedWith(Names.named(MOBILE)).to(DeskUpdateProfileView.class);
		bind(UpdateProfileView.class).annotatedWith(Names.named(TABLET)).to(DeskUpdateProfileView.class);

		bind(ChangePasswordView.class).annotatedWith(Names.named(DESKTOP)).to(DeskChangePasswordView.class);
		bind(ChangePasswordView.class).annotatedWith(Names.named(MOBILE)).to(DeskChangePasswordView.class);
		bind(ChangePasswordView.class).annotatedWith(Names.named(TABLET)).to(DeskChangePasswordView.class);
	}
}
