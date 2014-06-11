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

import com.agnie.common.gwt.serverclient.client.injector.CommonServerClientModule;
import com.agnie.useradmin.contextmgr.client.AppLoader;
import com.agnie.useradmin.contextmgr.client.mvp.ClientFactory;
import com.agnie.useradmin.contextmgr.client.presenter.ContextSettingsPresenter;
import com.agnie.useradmin.contextmgr.client.presenter.ContextUserPresenter;
import com.agnie.useradmin.contextmgr.client.ui.DesktopViewFactory;
import com.agnie.useradmin.contextmgr.client.ui.MobileViewFactory;
import com.agnie.useradmin.contextmgr.client.ui.TabletViewFactory;
import com.agnie.useradmin.persistance.client.injector.UserPersistModule;
import com.agnie.useradmin.session.client.injector.UserSessionModule;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({ MVPModule.class, UserPersistModule.class, UserSessionModule.class, CommonServerClientModule.class })
public interface MVPInjector extends Ginjector {

	DesktopViewFactory getDesktopViewFactory();

	MobileViewFactory getMobileViewFactory();

	TabletViewFactory getTabletViewFactory();

	ContextUserPresenter getContextUserPresenter();

	ContextSettingsPresenter getContextSettingsPresenter();

	AppLoader getAppLoader();

	ClientFactory getClientFactory();
}
