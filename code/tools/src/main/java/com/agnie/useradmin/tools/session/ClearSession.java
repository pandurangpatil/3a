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
package com.agnie.useradmin.tools.session;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.agnie.common.injector.AgnieDeploymentConfig;
import com.agnie.common.injector.PersistenceLifeCycleManager;
import com.agnie.common.tools.CommandProcessor;
import com.agnie.useradmin.session.server.injector.SessionCommonModule;
import com.agnie.useradmin.session.server.injector.SessionPersistModule;
import com.agnie.useradmin.session.server.injector.SessionPersistService;
import com.agnie.useradmin.session.server.injector.SessionPersistenceLifeCycleManager;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.persist.jpa.JpaPersistModule;

/**
 * @author Pandurang Patil 04-Mar-2014
 * 
 */
public class ClearSession extends CommandProcessor {
	private static Logger	logger	= Logger.getLogger(ClearSession.class);

	@Override
	public void process() throws Exception {
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				AgnieDeploymentConfig config = new AgnieDeploymentConfig(SessionCommonModule.AGNIE_PROJECT);
				Properties persistenceProperty = new Properties();
				try {
					persistenceProperty.load(new FileInputStream(new File(config.getConfig() + "/ag-session-persistence.properties")));
				} catch (IOException e) {
					logger.error("Error while reading ag-session-persistence : ", e);
				}
				if (persistenceProperty.isEmpty())
					install(new JpaPersistModule(SessionPersistModule.PERSISTENT_UNIT));
				else
					install(new JpaPersistModule(SessionPersistModule.PERSISTENT_UNIT).properties(persistenceProperty));
				bind(PersistenceLifeCycleManager.class).annotatedWith(SessionPersistService.class).to(SessionPersistenceLifeCycleManager.class);

			}
		});
		SessionCleanser uasm = injector.getInstance(SessionCleanser.class);
		PersistenceLifeCycleManager uamanager = injector.getInstance(Key.get(PersistenceLifeCycleManager.class, SessionPersistService.class));
		uamanager.startService();
		uamanager.beginUnitOfWork();
		uasm.removeExpiredSessions();
		uamanager.endUnitOfWork();
		uamanager.stopService();
	}

	public static void main(String[] args) throws Exception {
		ClearSession cs = new ClearSession();
		cs.process();
	}
}
