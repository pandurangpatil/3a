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
package com.agnie.useradmin.session.client.injector;

import com.agnie.common.gwt.serverclient.client.helper.ClientURLConfiguration;
import com.agnie.common.gwt.serverclient.client.helper.URLConfiguration;
import com.google.gwt.inject.client.AbstractGinModule;

public class UserSessionModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(URLConfiguration.class).to(ClientURLConfiguration.class);
	}
}
