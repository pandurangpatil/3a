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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.agnie.common.injector.AgnieDeploymentConfig;
import com.agnie.common.injector.PersistenceLifeCycleManager;
import com.agnie.useradmin.session.server.dao.UserAuthSessionManager;
import com.agnie.useradmin.session.server.dao.UserAuthSessionManagerImpl;
import com.agnie.useradmin.session.server.dao.UserSessionDao;
import com.google.inject.PrivateModule;
import com.google.inject.persist.jpa.JpaPersistModule;

/**
 * Private Persistent Module to connect with User Session unit.
 */
public class SessionPersistModule extends PrivateModule {
	private static Logger		logger			= Logger.getLogger(SessionPersistModule.class);
	public static final String	PERSISTENT_UNIT	= "user-session";
	// This mode refers to Junit testing mode or not.
	private boolean				mode			= false;

	public SessionPersistModule() {

	}

	public SessionPersistModule(boolean mode) {
		this.mode = mode;
	}

	@Override
	protected void configure() {
		AgnieDeploymentConfig config = new AgnieDeploymentConfig(SessionCommonModule.AGNIE_PROJECT);
		Properties persistenceProperty = new Properties();
		try {
			persistenceProperty.load(new FileInputStream(new File(config.getConfig() + "/ag-session-persistence.properties")));
		} catch (IOException e) {
			logger.error("Error while reading ag-session-persistence : ", e);
		}
		if (mode) {
			/*
			 * This will make sure while running junits those will be executed in separate schema.
			 */
			// NOTE: Don't change this code before consulting concerned person.
			persistenceProperty.setProperty("javax.persistence.jdbc.url", "jdbc:mysql://localhost/UserAdminTest");
		}
		if (persistenceProperty.isEmpty())
			install(new JpaPersistModule(PERSISTENT_UNIT));
		else
			install(new JpaPersistModule(PERSISTENT_UNIT).properties(persistenceProperty));
		bind(PersistenceLifeCycleManager.class).annotatedWith(SessionPersistService.class).to(SessionPersistenceLifeCycleManager.class);
		expose(PersistenceLifeCycleManager.class).annotatedWith(SessionPersistService.class);
		bind(UserAuthSessionManager.class).to(UserAuthSessionManagerImpl.class);
		expose(UserAuthSessionManager.class);
		bind(UserSessionDao.class).asEagerSingleton();
		expose(UserSessionDao.class);
	}

}
