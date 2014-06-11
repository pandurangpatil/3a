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
package com.agnie.useradmin.service.injector;

import com.agnie.common.requestfactory.AgnieServletContextListener;
import com.agnie.useradmin.persistance.server.injector.UserAdminServletModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class UserAdminServletContextListner extends AgnieServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new UserAdminServletModule(), new UserAdminJerseyServletModule());
	}

}
