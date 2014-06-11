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
package com.agnie.useradmin.session.server.injector;

import com.agnie.useradmin.session.server.dao.UserAuthSessionManagerTest;
import com.agnie.useradmin.session.server.dao.UserSessionDaoTest;
import com.agnie.useradmin.session.server.entity.UserAuthSessionTest;

public class TestSessionPersistModule extends SessionPersistModule {

	public TestSessionPersistModule() {
		super(true);
	}

	@Override
	protected void configure() {
		super.configure();
		bind(UserAuthSessionTest.class).asEagerSingleton();
		expose(UserAuthSessionTest.class);
		bind(TestSessionPersistenceInitilizer.class).asEagerSingleton();
		expose(TestSessionPersistenceInitilizer.class);
		bind(UserSessionDaoTest.class).asEagerSingleton();
		expose(UserSessionDaoTest.class);
		bind(UserAuthSessionManagerTest.class).asEagerSingleton();
		expose(UserAuthSessionManagerTest.class);
	}
}
