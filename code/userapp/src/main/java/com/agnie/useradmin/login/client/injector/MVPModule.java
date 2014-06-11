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
package com.agnie.useradmin.login.client.injector;

import com.agnie.useradmin.common.client.injector.CommonModule;
import com.agnie.useradmin.login.client.ui.DeskFPChangePasswordView;
import com.agnie.useradmin.login.client.ui.DeskForgotPasswordView;
import com.agnie.useradmin.login.client.ui.DeskLoginView;
import com.agnie.useradmin.login.client.ui.DeskSignupView;
import com.agnie.useradmin.login.client.ui.FPChangePasswordView;
import com.agnie.useradmin.login.client.ui.ForgotPasswordView;
import com.agnie.useradmin.login.client.ui.LoginView;
import com.agnie.useradmin.login.client.ui.SignupView;
import com.google.inject.name.Names;

public class MVPModule extends CommonModule {

	@Override
	protected void configure() {
		super.configure();
		bind(LoginView.class).annotatedWith(Names.named(DESKTOP)).to(DeskLoginView.class);
		bind(LoginView.class).annotatedWith(Names.named(MOBILE)).to(DeskLoginView.class);
		bind(LoginView.class).annotatedWith(Names.named(TABLET)).to(DeskLoginView.class);

		bind(SignupView.class).annotatedWith(Names.named(DESKTOP)).to(DeskSignupView.class);
		bind(SignupView.class).annotatedWith(Names.named(MOBILE)).to(DeskSignupView.class);
		bind(SignupView.class).annotatedWith(Names.named(TABLET)).to(DeskSignupView.class);

		bind(ForgotPasswordView.class).annotatedWith(Names.named(DESKTOP)).to(DeskForgotPasswordView.class);
		bind(ForgotPasswordView.class).annotatedWith(Names.named(MOBILE)).to(DeskForgotPasswordView.class);
		bind(ForgotPasswordView.class).annotatedWith(Names.named(TABLET)).to(DeskForgotPasswordView.class);

		bind(FPChangePasswordView.class).annotatedWith(Names.named(DESKTOP)).to(DeskFPChangePasswordView.class);
		bind(FPChangePasswordView.class).annotatedWith(Names.named(MOBILE)).to(DeskFPChangePasswordView.class);
		bind(FPChangePasswordView.class).annotatedWith(Names.named(TABLET)).to(DeskFPChangePasswordView.class);
	}
}
