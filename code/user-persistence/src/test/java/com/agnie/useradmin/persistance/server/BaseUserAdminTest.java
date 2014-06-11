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
package com.agnie.useradmin.persistance.server;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;

import com.agnie.common.injector.PersistenceLifeCycleManager;
import com.agnie.useradmin.persistance.server.injector.UserAdminPersistService;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class BaseUserAdminTest {
	@Inject
	protected Injector						injector;
	@Inject
	protected Provider<EntityManager>		em;
	@Inject
	@UserAdminPersistService
	protected PersistenceLifeCycleManager	manager;

	public EntityManager getEntityManager() {
		return em.get();
	}

	@Before
	public void before() {
		manager.beginUnitOfWork();
	}

	@After
	public void after() {
		manager.endUnitOfWork();
	}

}
