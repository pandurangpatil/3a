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

import org.codehaus.jackson.map.ObjectMapper;

import com.agnie.common.email.GmailSender;
import com.agnie.common.email.MailSender;
import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.common.helper.TestURLInfoImpl;
import com.agnie.common.injector.CommonModule;
import com.agnie.common.server.auth.ACLContext;
import com.agnie.common.test.providers.ACLContextProvider;
import com.agnie.common.test.providers.ACLCtxManager;
import com.agnie.common.test.providers.LoggedInUserProvider;
import com.agnie.common.test.providers.StringManager;
import com.agnie.common.test.providers.StringProvider;
import com.agnie.useradmin.persistance.server.auth.DomainAuthorizer;
import com.agnie.useradmin.persistance.server.auth.DomainContextAuthorizer;
import com.agnie.useradmin.session.server.auth.Authorizer;
import com.agnie.useradmin.session.server.auth.ContextAuthorizer;
import com.agnie.useradmin.session.server.injector.SessionCommonModule;
import com.agnie.useradmin.session.server.injector.SessionServletModule;
import com.google.inject.name.Names;

/**
 * 
 * @See CommonUserAdminModule, UserAdminServletModule
 */
public class TestUserAdminModule extends CommonUserAdminModule {

	@Override
	public void configure() {
		super.configure();

		StringManager sessionIdstrMgr = new StringManager();
		bind(StringManager.class).annotatedWith(Names.named(SessionServletModule.SESSION_ID)).toInstance(sessionIdstrMgr);
		bind(String.class).annotatedWith(Names.named(SessionServletModule.SESSION_ID)).toProvider(new StringProvider(sessionIdstrMgr));

		StringManager strMgr = new StringManager();
		bind(StringManager.class).annotatedWith(Names.named(DomainAuthorizer.SELECTED_DOMAIN)).toInstance(strMgr);
		bind(String.class).annotatedWith(Names.named(DomainAuthorizer.SELECTED_DOMAIN)).toProvider(new StringProvider(strMgr));

		StringManager strCtxMgr = new StringManager();
		bind(StringManager.class).annotatedWith(Names.named(DomainContextAuthorizer.SELECTED_CONTEXT)).toInstance(strCtxMgr);
		bind(String.class).annotatedWith(Names.named(DomainContextAuthorizer.SELECTED_CONTEXT)).toProvider(new StringProvider(strCtxMgr));

		ACLCtxManager appAclMgr = new ACLCtxManager();
		bind(ACLCtxManager.class).annotatedWith(Names.named(Authorizer.APP_ACL_CTX)).toInstance(appAclMgr);
		bind(ACLContext.class).annotatedWith(Names.named(Authorizer.APP_ACL_CTX)).toProvider(new ACLContextProvider(appAclMgr));

		ACLCtxManager domAclMgr = new ACLCtxManager();
		bind(ACLCtxManager.class).annotatedWith(Names.named(DomainAuthorizer.SELECTED_DOMAIN_ACL)).toInstance(domAclMgr);
		bind(ACLContext.class).annotatedWith(Names.named(DomainAuthorizer.SELECTED_DOMAIN_ACL)).toProvider(new ACLContextProvider(domAclMgr));

		ACLCtxManager domCtxAclMgr = new ACLCtxManager();
		bind(ACLCtxManager.class).annotatedWith(Names.named(DomainContextAuthorizer.SELECTED_DOMAIN_CONTEXT_ACL)).toInstance(domCtxAclMgr);
		bind(ACLContext.class).annotatedWith(Names.named(DomainContextAuthorizer.SELECTED_DOMAIN_CONTEXT_ACL)).toProvider(new ACLContextProvider(domCtxAclMgr));

		StringManager ctxMgr = new StringManager();
		bind(StringManager.class).annotatedWith(Names.named(ContextAuthorizer.CONTEXT)).toInstance(ctxMgr);
		bind(String.class).annotatedWith(Names.named(ContextAuthorizer.CONTEXT)).toProvider(new StringProvider(ctxMgr));

		ACLCtxManager ctxAclMgr = new ACLCtxManager();
		bind(ACLCtxManager.class).annotatedWith(Names.named(ContextAuthorizer.CONTEXT_ACL_CTX)).toInstance(ctxAclMgr);
		bind(ACLContext.class).annotatedWith(Names.named(ContextAuthorizer.CONTEXT_ACL_CTX)).toProvider(new ACLContextProvider(ctxAclMgr));

		bind(UserAccount.class).annotatedWith(Names.named(SessionServletModule.CURRENT_USER)).toProvider(LoggedInUserProvider.class);

		bind(ObjectMapper.class).asEagerSingleton();
		bind(MailSender.class).annotatedWith(Names.named(CommonModule.GMAIL_SENDER)).to(GmailSender.class);

		StringManager urlMgr = new StringManager();
		urlMgr.setValue("http://localhost/userapp");
		bind(StringManager.class).annotatedWith(Names.named(TestURLInfoImpl.TEST_URL)).toInstance(urlMgr);
		bind(String.class).annotatedWith(Names.named(TestURLInfoImpl.TEST_URL)).toProvider(new StringProvider(urlMgr));

		bind(URLInfo.class).to(TestURLInfoImpl.class);
		install(new SessionCommonModule());
	}

}
