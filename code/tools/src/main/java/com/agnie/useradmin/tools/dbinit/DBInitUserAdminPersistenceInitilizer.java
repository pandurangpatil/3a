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
package com.agnie.useradmin.tools.dbinit;

import com.agnie.common.injector.PersistenceLifeCycleManager;
import com.agnie.useradmin.persistance.server.injector.UserAdminPersistService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DBInitUserAdminPersistenceInitilizer {

	@Inject
	public DBInitUserAdminPersistenceInitilizer(@UserAdminPersistService PersistenceLifeCycleManager uamanager) {
		uamanager.startService();
	}

}
