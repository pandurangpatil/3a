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
package com.agnie.useradmin.common.client.injector;

import com.agnie.common.gwt.serverclient.client.helper.URLGenerator;
import com.agnie.common.gwt.serverclient.client.injector.CommonServerClientModule;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

public class CommonModule extends AbstractGinModule {

	public static final String	DESKTOP	= "desktop";
	public static final String	MOBILE	= "mobile";
	public static final String	TABLET	= "tablet";

	@Provides
	@Named(CommonServerClientModule.CURRENT_APP_DOMAIN)
	public String getAppDomain() {
		return URLGenerator.USERADMIN;
	}

	@Override
	protected void configure() {
	}
}
