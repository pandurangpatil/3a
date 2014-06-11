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
package com.agnie.useradmin.persistance.server.auth;

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.server.auth.ACLContext;
import com.agnie.common.test.providers.ACLContextProvider;
import com.agnie.common.test.providers.ACLCtxManager;
import com.agnie.common.test.providers.LoggedInUserProvider;
import com.agnie.useradmin.persistance.server.injector.CommonUserAdminModule;
import com.agnie.useradmin.session.server.auth.Authorizer;
import com.agnie.useradmin.session.server.auth.ContextAuthorizer;
import com.agnie.useradmin.session.server.injector.SessionServletModule;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class TestPermissionModule extends AbstractModule {

	@Override
	protected void configure() {
		ACLCtxManager appAclMgr = new ACLCtxManager();
		bind(ACLCtxManager.class).toInstance(appAclMgr);
		bind(ACLContext.class).annotatedWith(Names.named(Authorizer.APP_ACL_CTX)).toProvider(new ACLContextProvider(appAclMgr));

		bind(UserAccount.class).annotatedWith(Names.named(SessionServletModule.CURRENT_USER)).toProvider(LoggedInUserProvider.class);

		ACLCtxManager domAclMgr = new ACLCtxManager();
		bind(ACLCtxManager.class).annotatedWith(Names.named(DomainAuthorizer.SELECTED_DOMAIN_ACL)).toInstance(domAclMgr);
		bind(ACLContext.class).annotatedWith(Names.named(DomainAuthorizer.SELECTED_DOMAIN_ACL)).toProvider(new ACLContextProvider(domAclMgr));

		ACLCtxManager domCtxAclMgr = new ACLCtxManager();
		bind(ACLCtxManager.class).annotatedWith(Names.named(DomainContextAuthorizer.SELECTED_DOMAIN_CONTEXT_ACL)).toInstance(domCtxAclMgr);
		bind(ACLContext.class).annotatedWith(Names.named(DomainContextAuthorizer.SELECTED_DOMAIN_CONTEXT_ACL)).toProvider(new ACLContextProvider(domCtxAclMgr));

		ACLCtxManager ctxAclMgr = new ACLCtxManager();
		bind(ACLCtxManager.class).annotatedWith(Names.named(ContextAuthorizer.CONTEXT_ACL_CTX)).toInstance(ctxAclMgr);
		bind(ACLContext.class).annotatedWith(Names.named(ContextAuthorizer.CONTEXT_ACL_CTX)).toProvider(new ACLContextProvider(ctxAclMgr));

		install(new CommonUserAdminModule());
	}

}
