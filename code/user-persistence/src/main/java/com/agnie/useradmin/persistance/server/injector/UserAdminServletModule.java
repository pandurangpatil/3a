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

import java.util.HashMap;
import java.util.Map;

import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.common.helper.ServerURLInfo;
import com.agnie.common.injector.CommonModule;
import com.agnie.common.requestfactory.AgnieRFExceptionHandler;
import com.agnie.common.requestfactory.AgnieRFServiceLayerDecorator;
import com.agnie.common.requestfactory.AgnieRequestFactoryServlet;
import com.agnie.common.server.auth.ACLContext;
import com.agnie.useradmin.persistance.server.auth.DomainAuthorizer;
import com.agnie.useradmin.persistance.server.auth.DomainContextAuthorizer;
import com.agnie.useradmin.persistance.server.auth.UserAdminAuthorizerFilter;
import com.agnie.useradmin.persistance.server.auth.UserIdentityVerificationFilter;
import com.agnie.useradmin.persistance.server.mybatis.UserAdminMyBatisModule;
import com.agnie.useradmin.persistance.server.service.AuthenticateServiceImpl;
import com.agnie.useradmin.persistance.server.service.UserProfileServiceImpl;
import com.agnie.useradmin.session.server.injector.SessionServletModule;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletScopes;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;

/**
 * User admin specific servlet module add only those binding here in this class. Which are only specific required from
 * web context point of view. All bindings which can be shared between TestUserAdminModule and this module should be
 * placed inside CommonUserAdminModule.
 * 
 * 
 * @See TestUserAdminModule, CommonUserAdminModule
 */
public class UserAdminServletModule extends SessionServletModule {

	@Override
	protected void configureServlets() {
		super.configureServlets();
		install(new CommonModule());
		install(new UserAdminMyBatisModule());
		install(new CommonUserAdminModule());
		install(new UserAdminPersistModule());
		bind(URLInfo.class).to(ServerURLInfo.class);
		bind(ExceptionHandler.class).to(AgnieRFExceptionHandler.class);
		bind(ServiceLayerDecorator.class).to(AgnieRFServiceLayerDecorator.class);
		// Value will be set inside AgnieSessionFilter, which will be extracted as a cookie parameter. So once
		// application is selected to edit, selected applications domain should be set to cookie key "domain". Similarly
		// for domain context
		bind(String.class).annotatedWith(Names.named(DomainAuthorizer.SELECTED_DOMAIN)).to(String.class).in(ServletScopes.REQUEST);
		bind(String.class).annotatedWith(Names.named(DomainContextAuthorizer.SELECTED_CONTEXT)).to(String.class).in(ServletScopes.REQUEST);
		// Domain (Useradmin application entity) level ACL Context
		bind(ACLContext.class).annotatedWith(Names.named(DomainAuthorizer.SELECTED_DOMAIN_ACL)).to(ACLContext.class).in(ServletScopes.REQUEST);
		// Domain's context level ( Useradmin Context entity) level ACL Context
		bind(ACLContext.class).annotatedWith(Names.named(DomainContextAuthorizer.SELECTED_DOMAIN_CONTEXT_ACL)).to(ACLContext.class).in(ServletScopes.REQUEST);
		// All bindings which can be shared between TestUserAdminModule and this module should be placed inside
		// CommonUserAdminModule.
		// This will take care of starting unit of work for user admin persistent unit. And closing it at the end of the
		// response.
		filter("/*").through(UserAdminPersistFilter.class);
		filter("/*").through(UserIdentityVerificationFilter.class);
		filter("/*").through(UserAdminAuthorizerFilter.class);
		serveRegex("(.)*/authService").with(AuthenticateServiceImpl.class);
		serveRegex("(.)*/profileService").with(UserProfileServiceImpl.class);
		Map<String, String> params = new HashMap<String, String>();
		params.put("symbolMapsDirectory", "WEB-INF/classes/symbolMaps/");
		serve("/gwtRequest").with(AgnieRequestFactoryServlet.class, params);
	}
}
