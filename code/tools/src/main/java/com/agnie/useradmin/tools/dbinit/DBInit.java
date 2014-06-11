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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.log4j.Logger;

import com.agnie.common.injector.AgnieDeploymentConfig;
import com.agnie.common.injector.PersistenceLifeCycleManager;
import com.agnie.common.tools.CommandProcessor;
import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.agnie.useradmin.persistance.server.common.digester.AuthLevelConvert;
import com.agnie.useradmin.persistance.server.injector.UserAdminPersistService;
import com.agnie.useradmin.persistance.server.injector.UserAdminPersistenceLifeCycleManager;
import com.beust.jcommander.Parameter;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.persist.jpa.JpaPersistModule;

public class DBInit extends CommandProcessor {

	private static Logger	logger	= Logger.getLogger(DBInit.class);

	@Parameter(names = { "-h", "--host" }, description = "DataBase host name")
	private String			host;

	@Parameter(names = { "-pt", "--port" }, description = "DataBase port number")
	private String			port;

	@Parameter(names = { "-u", "--username" }, description = "DataBase username")
	private String			username;

	@Parameter(names = { "-p", "--password" }, description = "DataBase password")
	private String			password;

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	private Properties readInitProperties() {
		Properties initProperties = new Properties();
		AgnieDeploymentConfig config = new AgnieDeploymentConfig("global");
		try {
			initProperties.load(new FileInputStream(new File(config.getConfig() + "/init-db.properties")));
		} catch (IOException e) {
			logger.error("Error while reading init-db.properties : ", e);
		}

		boolean notSet = false;
		for (Object key : initProperties.keySet()) {
			String value = initProperties.getProperty((String) key);
			if (value == null || value.isEmpty() || value.startsWith("<")) {
				logger.error("Property '" + key + "' is not set with value");
				System.out.println("Property '" + key + "' is not set with value");
				notSet = true;
			}
		}
		if (notSet) {
			System.out.println("Error init-db.properties is not set with required values");
			System.exit(1);
		}
		return initProperties;
	}

	@Override
	public void process() throws Exception {
		Properties initProperties = readInitProperties();
		System.out.println("DB is getting Initalized ....");
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				Properties props = new Properties();
				if (host != null && !host.isEmpty())
					props.put("javax.persistence.jdbc.url", "jdbc:mysql://" + host + (port != null && !port.isEmpty() ? ":" + port : "") + "/UserAdmin");
				if (username != null && !username.isEmpty()) {
					props.put("javax.persistence.jdbc.user", username);
					props.put("javax.persistence.jdbc.password", password);
				}
				props.put("eclipselink.ddl-generation", "drop-and-create-tables");
				install(new JpaPersistModule("user-admin").properties(props));
				bind(PersistenceLifeCycleManager.class).annotatedWith(UserAdminPersistService.class).to(UserAdminPersistenceLifeCycleManager.class);
				bind(DBInitUserAdminPersistenceInitilizer.class).asEagerSingleton();
			}
		});
		AuthLevelConvert converter = new AuthLevelConvert();
		ConvertUtils.register(converter, AuthLevel.class);
		DBInitializer init = injector.getInstance(DBInitializer.class);
		init.setInitProperties(initProperties);
		PersistenceLifeCycleManager uamanager = injector.getInstance(Key.get(PersistenceLifeCycleManager.class, UserAdminPersistService.class));
		uamanager.beginUnitOfWork();
		init.IntializeData();
		uamanager.endUnitOfWork();

		System.out.println("DB got Initalized succesfully....");
	}
}
