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

import com.agnie.gwt.common.client.widget.MessagePanel;
import com.agnie.useradmin.login.client.injector.MVPModule;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class DesktopViewFactory implements ViewFactory {

	/**
	 * Views will be instantiated only on its first time usage.
	 */
	@Inject
	@Named(MVPModule.DESKTOP)
	private LoginView				loginView;
	@Inject
	@Named(MVPModule.DESKTOP)
	private ForgotPasswordView		forgotPasswordView;
	@Inject
	@Named(MVPModule.DESKTOP)
	private FPChangePasswordView	fpChangePasswordView;
	@Inject
	@Named(MVPModule.DESKTOP)
	private SignupView				signupView;
	@Inject
	protected MessagePanel			messagePanel;
	private HTMLPanel				centerContentPanel	= new HTMLPanel("");

	public DesktopViewFactory() {

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
		return loginView;
	}

	/**
	 * @return the forgotPasswordView
	 */
	public ForgotPasswordView getForgotPasswordView() {
		return forgotPasswordView;
	}

	/**
	 * @return the changePasswordView
	 */
	public FPChangePasswordView getFpChangePasswordView() {
		return fpChangePasswordView;
	}

	/**
	 * @return the signupView
	 */
	public SignupView getSignupView() {
		return signupView;
	}

}
