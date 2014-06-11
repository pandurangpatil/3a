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

import com.agnie.common.email.MessageTemplate;
import com.agnie.common.injector.AgnieDeploymentConfig;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.persistance.server.auth.DomainAuthorizer;
import com.agnie.useradmin.persistance.server.auth.DomainContextAuthorizer;
import com.agnie.useradmin.persistance.server.auth.DomainContextRistrictedResource;
import com.agnie.useradmin.persistance.server.auth.DomainRistrictedResource;
import com.agnie.useradmin.persistance.server.auth.UserAuthorizer;
import com.agnie.useradmin.persistance.server.auth.UsersRistrictedResource;
import com.agnie.useradmin.session.server.auth.RequirePermissions;
import com.agnie.useradmin.session.server.injector.SessionServletModule;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;

/**
 * User admin common Guice module which can be shared between Junit test modules and servlet modules. Add bindings which
 * will be shared between Test modules and servlet modules.
 * 
 * @See TestUserAdminModule, UserAdminServletModule
 * 
 */
public class CommonUserAdminModule extends AbstractModule {
	private static Logger		logger			= Logger.getLogger(CommonUserAdminModule.class);
	public static final String	AGNIE_PROJECT	= "3A";
	public static final String	VERIFY_ACC_TPL	= "verify-acc";
	public static final String	FGT_PWD_TPL		= "fgt-pwd";
	public static final String	NEW_REG_REQ_TPL	= "reg-req";
	public static final String	REG_STATUS_TPL	= "reg-status";

	@Override
	protected void configure() {
		AgnieDeploymentConfig config = new AgnieDeploymentConfig(CommonUserAdminModule.AGNIE_PROJECT);
		Properties persistenceProperty = new Properties();
		try {
			persistenceProperty.load(new FileInputStream(new File(config.getConfig() + "/3a.properties")));
			Names.bindProperties(binder(), persistenceProperty);
		} catch (IOException e) {
			logger.error("Error while reading 3a.properties : ", e);
		}
		UserAuthorizer uauthorizer = new UserAuthorizer();
		requestInjection(uauthorizer);
		bindInterceptor(Matchers.annotatedWith(UsersRistrictedResource.class), Matchers.annotatedWith(RequirePermissions.class), uauthorizer);

		bind(String.class).annotatedWith(Names.named(SessionServletModule.AGNIE_APPLCATION)).toInstance(UserAdminURLGenerator.USERADMIN);

		DomainAuthorizer domAuthorizer = new DomainAuthorizer();
		requestInjection(domAuthorizer);
		bindInterceptor(Matchers.annotatedWith(DomainRistrictedResource.class), Matchers.annotatedWith(RequirePermissions.class), domAuthorizer);

		DomainContextAuthorizer domCtxAuthorizer = new DomainContextAuthorizer();
		requestInjection(domCtxAuthorizer);
		bindInterceptor(Matchers.annotatedWith(DomainContextRistrictedResource.class), Matchers.annotatedWith(RequirePermissions.class), domCtxAuthorizer);

		bind(MessageTemplate.class).annotatedWith(Names.named(VERIFY_ACC_TPL)).toInstance(new MessageTemplate("account_verification.tpl", config.getConfig()));
		bind(MessageTemplate.class).annotatedWith(Names.named(FGT_PWD_TPL)).toInstance(new MessageTemplate("forgot_password.tpl", config.getConfig()));
		bind(MessageTemplate.class).annotatedWith(Names.named(NEW_REG_REQ_TPL)).toInstance(new MessageTemplate("new-registration-request.tpl", config.getConfig()));
		bind(MessageTemplate.class).annotatedWith(Names.named(REG_STATUS_TPL)).toInstance(new MessageTemplate("registration-status.tpl", config.getConfig()));
	}

}
