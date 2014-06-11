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

import com.google.gwt.user.client.ui.HTMLPanel;

public interface ViewFactory {

	public HTMLPanel getCenterContentPanel();

	public LoginView getLoginView();

	public ForgotPasswordView getForgotPasswordView();

	public FPChangePasswordView getFpChangePasswordView();

	public SignupView getSignupView();

}
