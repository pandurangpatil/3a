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

import com.agnie.common.injector.PersistenceLifeCycleManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;

@Singleton
public class SessionPersistenceLifeCycleManager implements PersistenceLifeCycleManager {

	private final UnitOfWork		unitOfWork;
	private final PersistService	persistService;

	@Inject
	public SessionPersistenceLifeCycleManager(UnitOfWork unitOfWork, PersistService persistService) {
		this.unitOfWork = unitOfWork;
		this.persistService = persistService;
	}

	public void startService() {
		this.persistService.start();
	}

	public void stopService() {
		this.persistService.stop();
	}

	public void beginUnitOfWork() {
		this.unitOfWork.begin();
	}

	public void endUnitOfWork() {
		this.unitOfWork.end();
	}
}
