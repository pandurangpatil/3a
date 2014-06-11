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
package com.agnie.useradmin.landing.client;

import com.agnie.gwt.common.client.base.BasePageResourceLoader;
import com.agnie.useradmin.common.client.base.UserAdminBasePageResourceLoader;
import com.agnie.useradmin.landing.client.injector.MVPInjector;
import com.agnie.useradmin.landing.client.mvp.PlatformFactory;
import com.agnie.useradmin.landing.client.ui.LandingPageResourceLoader;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class Landing implements EntryPoint {
	private PlatformFactory			platformFactory		= GWT.create(PlatformFactory.class);
	BasePageResourceLoader			baseResourceLoader	= new BasePageResourceLoader();
	UserAdminBasePageResourceLoader	uab					= new UserAdminBasePageResourceLoader();
	LandingPageResourceLoader		lprl				= new LandingPageResourceLoader();

	@Override
	public void onModuleLoad() {
		final MVPInjector injector = platformFactory.getInjector();
		RootPanel pageLoader = injector.getClientFactory().getRootPanelFactory().getPageLoader();
		pageLoader.setVisible(false);

		AppLoader loader = injector.getAppLoader();
		loader.setViewFactory(platformFactory.getViewFactory());
		loader.start();
	}
}
