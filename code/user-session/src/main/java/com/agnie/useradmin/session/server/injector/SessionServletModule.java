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

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.server.auth.ACLContext;
import com.agnie.useradmin.session.client.helper.RequestType;
import com.agnie.useradmin.session.client.helper.RequestTypeProvider;
import com.agnie.useradmin.session.server.auth.Authorizer;
import com.agnie.useradmin.session.server.auth.ContextAuthorizer;
import com.agnie.useradmin.session.server.service.UserSessionServiceImpl;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletScopes;

/**
 * Session servlet module will serve the purpose of binding required dependencies for session module with that it will
 * also serve the purpose to bind some commonly used dependencies. As in most of the cases this module would be the
 * parent module of all module.
 * 
 */

public abstract class SessionServletModule extends ServletModule {

	public final static String	SESSION_ID			= "session-id";
	public final static String	CURRENT_USER		= "current-user";
	// This domain would recognise under which agnie application/domain this module is running. Its value is supposed to
	// be added by individual application module.
	public final static String	AGNIE_APPLCATION	= "agnie-app";
	public final static String	DEV_MODE			= "gwt.codesvr";
	public final static String	REQUEST_TYPE		= "request-type";
	public final static String	REQUEST_TYPE_STR	= "request-type-str";

	@Override
	protected void configureServlets() {
		install(new SessionCommonModule());
		install(new SessionPersistModule());
		// SessionId will be set inside AgnieSessionFilter by extracting sessiond from cookie.
		bind(String.class).annotatedWith(Names.named(SESSION_ID)).to(String.class).in(ServletScopes.REQUEST);
		// UserAccount is set inside AgnieAuthCheckFilter. By extracting loggedin user information from session id.
		bind(UserAccount.class).annotatedWith(Names.named(CURRENT_USER)).to(UserAccount.class).in(ServletScopes.REQUEST);
		// Application level ACL Context. This ACL context will be used with all projects/modules which is including
		// user-session module. The value is set inside AgnieAuthCheckFilter.
		bind(ACLContext.class).annotatedWith(Names.named(Authorizer.APP_ACL_CTX)).to(ACLContext.class).in(ServletScopes.REQUEST);
		// Context level ACL Context. This ACL context will be used with all projects/modules which is including
		// user-session module. The value is set inside AgnieAuthCheckFilter.
		bind(ACLContext.class).annotatedWith(Names.named(ContextAuthorizer.CONTEXT_ACL_CTX)).to(ACLContext.class).in(ServletScopes.REQUEST);
		// This value is expected to be set by corresponding application in their respective filters.
		bind(String.class).annotatedWith(Names.named(DEV_MODE)).to(String.class).in(ServletScopes.REQUEST);
		// This value is expected to be set by corresponding application in their respective filters.
		bind(String.class).annotatedWith(Names.named(REQUEST_TYPE_STR)).to(String.class).in(ServletScopes.REQUEST);
		RequestTypeProvider rtp = new RequestTypeProvider();
		requestInjection(rtp);
		// REQUEST_TYPE dependency is dependent on REQUEST_TYPE_STR so as REQUEST_TYPE_STR is REQUEST scoped dependency
		// similarly REQUEST_TYPE dependency also becomes REQUEST scoped dependency. Even if we don't mark it as REQUEST
		// scoped dependency explicitly. That in turn means we need to make use of provider to inject rather than
		// injecting the entity directly.
		bind(RequestType.class).annotatedWith(Names.named(REQUEST_TYPE)).toProvider(rtp);
		// This filter will take care of extracting request cookies and make them available through guice.
		filter("/*").through(AgnieSessionFilter.class);
		// This will take care of starting unit of work initially for session persistent unit and ending it at the end
		// of the respones.
		filter("/*").through(SessionPersistFilter.class);
		serveRegex("(.)*/authUserService").with(UserSessionServiceImpl.class);
	}
}
