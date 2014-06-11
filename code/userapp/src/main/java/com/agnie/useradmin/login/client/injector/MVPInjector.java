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

import com.agnie.common.gwt.serverclient.client.injector.CommonServerClientModule;
import com.agnie.useradmin.common.client.ui.UACommonViewFactory;
import com.agnie.useradmin.login.client.mvp.ClientFactory;
import com.agnie.useradmin.login.client.mvp.LoginAppController;
import com.agnie.useradmin.login.client.presenter.FPChangePwdPresenter;
import com.agnie.useradmin.login.client.presenter.ForgotPasswordPresenter;
import com.agnie.useradmin.login.client.presenter.LoginPresenter;
import com.agnie.useradmin.login.client.presenter.SignupPresenter;
import com.agnie.useradmin.login.client.presenter.VerifyPresenter;
import com.agnie.useradmin.login.client.ui.DesktopViewFactory;
import com.agnie.useradmin.login.client.ui.MobileViewFactory;
import com.agnie.useradmin.login.client.ui.TabletViewFactory;
import com.agnie.useradmin.persistance.client.injector.UserPersistModule;
import com.agnie.useradmin.session.client.injector.UserSessionModule;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({ MVPModule.class, UserPersistModule.class, CommonServerClientModule.class, UserSessionModule.class })
public interface MVPInjector extends Ginjector {

	DesktopViewFactory getDesktopViewFactory();

	MobileViewFactory getMobileViewFactory();

	TabletViewFactory getTabletViewFactory();

	LoginAppController getAppController();

	ClientFactory getClientFactory();

	LoginPresenter getLoginPresenter();

	ForgotPasswordPresenter getForgotPasswordPresenter();

	FPChangePwdPresenter getFPChangePwdPresenter();

	SignupPresenter getSignupPresenter();

	VerifyPresenter getVerifyPresenter();

	ApplicationProvider getApplicationProvider();

	UACommonViewFactory getCommonViewFactory();
}
