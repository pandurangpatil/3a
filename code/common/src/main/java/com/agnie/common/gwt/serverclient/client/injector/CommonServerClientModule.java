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
package com.agnie.common.gwt.serverclient.client.injector;

import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.common.gwt.serverclient.client.helper.URLInfoImpl;
import com.google.gwt.inject.client.AbstractGinModule;

public class CommonServerClientModule extends AbstractGinModule {

	public static final String	CURRENT_APP_DOMAIN	= "agnie-domain";

	@Override
	protected void configure() {
		bind(URLInfo.class).to(URLInfoImpl.class);
	}
}
