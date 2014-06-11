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
package com.agnie.useradmin.persistance.server.injector;

import com.agnie.useradmin.session.server.injector.SessionPersistModule;

public class TestSessionPersistModule extends SessionPersistModule {

	public TestSessionPersistModule() {
		super(true);
	}

	@Override
	protected void configure() {
		super.configure();
		bind(TestSessionPersistenceInitilizer.class).asEagerSingleton();
		expose(TestSessionPersistenceInitilizer.class);
	}
}
