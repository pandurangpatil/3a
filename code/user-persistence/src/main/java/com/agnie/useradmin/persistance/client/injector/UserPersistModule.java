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
package com.agnie.useradmin.persistance.client.injector;

import com.agnie.useradmin.persistance.client.helper.UserAdminClientURLInfo;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLInfo;
import com.google.gwt.inject.client.AbstractGinModule;

public class UserPersistModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(UserAdminURLInfo.class).to(UserAdminClientURLInfo.class);
	}
}
