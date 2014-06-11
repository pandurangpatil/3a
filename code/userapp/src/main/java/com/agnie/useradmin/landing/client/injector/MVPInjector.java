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

import com.agnie.common.gwt.serverclient.client.injector.CommonServerClientModule;
import com.agnie.useradmin.landing.client.AppLoader;
import com.agnie.useradmin.landing.client.mvp.ClientFactory;
import com.agnie.useradmin.landing.client.mvp.LandingAppController;
import com.agnie.useradmin.landing.client.presenter.ChangePasswordPresenter;
import com.agnie.useradmin.landing.client.presenter.LandingPresenter;
import com.agnie.useradmin.landing.client.presenter.NewAppPresenter;
import com.agnie.useradmin.landing.client.presenter.UpdateProfilePresenter;
import com.agnie.useradmin.landing.client.ui.DesktopViewFactory;
import com.agnie.useradmin.landing.client.ui.MobileViewFactory;
import com.agnie.useradmin.landing.client.ui.TabletViewFactory;
import com.agnie.useradmin.persistance.client.injector.UserPersistModule;
import com.agnie.useradmin.session.client.injector.UserSessionModule;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({ MVPModule.class, UserPersistModule.class, UserSessionModule.class, CommonServerClientModule.class })
public interface MVPInjector extends Ginjector {

	DesktopViewFactory getDesktopViewFactory();

	MobileViewFactory getMobileViewFactory();

	TabletViewFactory getTabletViewFactory();

	LandingAppController getAppController();

	ClientFactory getClientFactory();

	ChangePasswordPresenter getChangePasswordPresenter();

	NewAppPresenter getNewAppPresenter();

	LandingPresenter getLandingPresenter();

	UpdateProfilePresenter getUpdateProfilePresenter();

	AppLoader getAppLoader();
}
