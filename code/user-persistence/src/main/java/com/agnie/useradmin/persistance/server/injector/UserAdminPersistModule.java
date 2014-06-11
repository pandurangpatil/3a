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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.agnie.common.injector.AgnieDeploymentConfig;
import com.agnie.common.injector.PersistenceLifeCycleManager;
import com.agnie.useradmin.persistance.server.dao.ApplicationManager;
import com.agnie.useradmin.persistance.server.dao.AuthenticateManager;
import com.agnie.useradmin.persistance.server.dao.ContextManager;
import com.agnie.useradmin.persistance.server.dao.PermissionManager;
import com.agnie.useradmin.persistance.server.dao.RoleManager;
import com.agnie.useradmin.persistance.server.dao.UserManager;
import com.agnie.useradmin.persistance.server.entity.UserAdminEntityLocator;
import com.google.inject.PrivateModule;
import com.google.inject.persist.jpa.JpaPersistModule;

/**
 * Private Persistent Module to connect with User Session unit.
 */
public class UserAdminPersistModule extends PrivateModule {
	private static Logger		logger			= Logger.getLogger(UserAdminPersistModule.class);
	// This mode refers to Junit testing mode or not.
	private boolean				mode			= false;
	public static final String	PERSISTENT_UNIT	= "user-admin";

	public UserAdminPersistModule() {

	}

	public UserAdminPersistModule(boolean mode) {
		this.mode = mode;
	}

	@Override
	protected void configure() {
		AgnieDeploymentConfig config = new AgnieDeploymentConfig(CommonUserAdminModule.AGNIE_PROJECT);
		Properties persistenceProperty = new Properties();
		try {
			persistenceProperty.load(new FileInputStream(new File(config.getConfig() + "/3a-persistence.properties")));
		} catch (IOException e) {
			logger.error("Error while reading 3a-persistence.properties : ", e);
		}
		if (mode) {
			/*
			 * This will make sure while running junits those will be executed in separate schema.
			 */
			// NOTE: Don't change this code before consulting concerned person.
			persistenceProperty.setProperty("javax.persistence.jdbc.url", "jdbc:mysql://localhost/UserAdminTest");
			persistenceProperty.setProperty("eclipselink.ddl-generation", "drop-and-create-tables");
		}
		if (persistenceProperty.isEmpty())
			install(new JpaPersistModule(PERSISTENT_UNIT));
		else
			install(new JpaPersistModule(PERSISTENT_UNIT).properties(persistenceProperty));

		bind(PersistenceLifeCycleManager.class).annotatedWith(UserAdminPersistService.class).to(UserAdminPersistenceLifeCycleManager.class);
		expose(PersistenceLifeCycleManager.class).annotatedWith(UserAdminPersistService.class);
		bind(UserAdminEntityLocator.class);
		expose(UserAdminEntityLocator.class);
		bind(UserManager.class);
		expose(UserManager.class);
		bind(ContextManager.class);
		expose(ContextManager.class);
		bind(ApplicationManager.class);
		expose(ApplicationManager.class);
		bind(AuthenticateManager.class);
		expose(AuthenticateManager.class);
		bind(PermissionManager.class);
		expose(PermissionManager.class);
		bind(RoleManager.class);
		expose(RoleManager.class);

	}

}
