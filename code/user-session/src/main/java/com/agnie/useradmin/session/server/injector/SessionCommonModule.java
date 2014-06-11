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

import com.agnie.common.cache.CacheService;
import com.agnie.common.cache.MemCacheService;
import com.agnie.common.gwt.serverclient.client.helper.URLConfiguration;
import com.agnie.common.helper.ServerURLConfiguration;
import com.agnie.common.injector.AgnieDeploymentConfig;
import com.agnie.useradmin.session.server.auth.Authorizer;
import com.agnie.useradmin.session.server.auth.ContextAuthorizer;
import com.agnie.useradmin.session.server.auth.ContextRistrictedResource;
import com.agnie.useradmin.session.server.auth.RequirePermissions;
import com.agnie.useradmin.session.server.auth.RistrictedResource;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;

/**
 * Guice bindings which are common will be configured here in this module. This separate modules will make it easier to
 * manage the change to be shared between main application and test side.
 */
public class SessionCommonModule extends AbstractModule {
	private static Logger		logger				= Logger.getLogger(SessionCommonModule.class);
	public static final String	AGNIE_PROJECT		= "Session";
	public static final String	REMEMBER_EXP_TIME	= "session.remember.me.expiry";
	public static final String	DEFAULT_EXP_TIME	= "session.default.expiry";
	public static final String	DNT_REMB_COK_TOKEN	= "dnrmb";

	@Override
	protected void configure() {
		loadProperties(binder());
		bind(CacheService.class).to(MemCacheService.class);
		bind(URLConfiguration.class).to(ServerURLConfiguration.class);
		Authorizer authorizer = new Authorizer();
		requestInjection(authorizer);
		bindInterceptor(Matchers.annotatedWith(RistrictedResource.class), Matchers.annotatedWith(RequirePermissions.class), authorizer);

		ContextAuthorizer ctxAuthorizer = new ContextAuthorizer();
		requestInjection(ctxAuthorizer);
		bindInterceptor(Matchers.annotatedWith(ContextRistrictedResource.class), Matchers.annotatedWith(RequirePermissions.class), ctxAuthorizer);
	}

	private void loadProperties(Binder binder) {
		AgnieDeploymentConfig config = new AgnieDeploymentConfig(AGNIE_PROJECT);
		Properties persistenceProperty = new Properties();
		try {
			persistenceProperty.load(new FileInputStream(new File(config.getConfig() + "/session.properties")));
			Names.bindProperties(binder(), persistenceProperty);
		} catch (IOException e) {
			logger.error("Error while reading session.properties : ", e);
			// This is the preferred way to tell Guice something went wrong
			binder.addError(e);
		}
	}
}
