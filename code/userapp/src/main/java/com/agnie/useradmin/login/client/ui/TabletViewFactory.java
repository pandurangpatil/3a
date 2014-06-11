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
package com.agnie.useradmin.login.client.ui;

import com.agnie.useradmin.login.client.injector.MVPModule;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class TabletViewFactory implements ViewFactory {

	/**
	 * Views will be instantiated only on its first time usage.
	 */
	@Inject
	@Named(MVPModule.TABLET)
	private LoginView				loginView;
	@Inject
	@Named(MVPModule.TABLET)
	private ForgotPasswordView		forgotPasswordView;
	@Inject
	@Named(MVPModule.TABLET)
	private FPChangePasswordView	fpChangePasswordView;
	@Inject
	@Named(MVPModule.TABLET)
	private SignupView				signupView;
	private HTMLPanel				centerContentPanel	= new HTMLPanel("");

	public TabletViewFactory() {

		// applying float left assuming vertical panel will be always placed inside split panel on left hand side.
		centerContentPanel.addStyleName("content-wrapper");
		centerContentPanel.addStyleName("clearfix");
	}

	/**
	 * @return the centerContentPanel
	 */
	public HTMLPanel getCenterContentPanel() {
		return centerContentPanel;
	}

	public LoginView getLoginView() {
		if (loginView == null)
			loginView = new DeskLoginView();
		return loginView;
	}

	/**
	 * @return the forgotPasswordView
	 */
	public ForgotPasswordView getForgotPasswordView() {
		if (forgotPasswordView == null) {
			forgotPasswordView = new DeskForgotPasswordView();
		}
		return forgotPasswordView;
	}

	/**
	 * @return the changePasswordView
	 */
	public FPChangePasswordView getFpChangePasswordView() {
		if (fpChangePasswordView == null) {
			fpChangePasswordView = new DeskFPChangePasswordView();
		}
		return fpChangePasswordView;
	}

	/**
	 * @return the signupView
	 */
	public SignupView getSignupView() {
		if (signupView == null) {
			signupView = new DeskSignupView();
		}
		return signupView;
	}

}
