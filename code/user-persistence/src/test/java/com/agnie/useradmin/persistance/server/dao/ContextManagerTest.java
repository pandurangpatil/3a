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
package com.agnie.useradmin.persistance.server.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.helper.SHA256;
import com.agnie.common.injector.CommonModule;
import com.agnie.common.server.auth.ACLContext;
import com.agnie.common.test.injector.GuiceTestRunner;
import com.agnie.common.test.injector.WithModules;
import com.agnie.common.test.providers.ACLCtxManager;
import com.agnie.common.test.providers.LoggedInUserManager;
import com.agnie.common.test.providers.StringManager;
import com.agnie.useradmin.persistance.client.enums.GeneralStatus;
import com.agnie.useradmin.persistance.client.enums.Language;
import com.agnie.useradmin.persistance.client.exception.CriticalException;
import com.agnie.useradmin.persistance.client.exception.UserAdminException;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.persistance.client.service.dto.Credential;
import com.agnie.useradmin.persistance.client.service.dto.UserInfo;
import com.agnie.useradmin.persistance.server.BaseUserAdminTest;
import com.agnie.useradmin.persistance.server.auth.DomainAuthorizer;
import com.agnie.useradmin.persistance.server.auth.DomainContextAuthorizer;
import com.agnie.useradmin.persistance.server.entity.Application;
import com.agnie.useradmin.persistance.server.entity.Context;
import com.agnie.useradmin.persistance.server.entity.UserApplicationCtxRegistration;
import com.agnie.useradmin.persistance.server.helper.DBSetupHelper;
import com.agnie.useradmin.persistance.server.injector.TestSessionPersistModule;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminModule;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminPersistModule;
import com.agnie.useradmin.session.client.helper.UserNotAutherisedException;
import com.agnie.useradmin.session.server.mybatis.SessionMyBatisModule;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.persist.Transactional;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestUserAdminModule.class, TestUserAdminPersistModule.class, TestSessionPersistModule.class, SessionMyBatisModule.class, CommonModule.class })
public class ContextManagerTest extends BaseUserAdminTest {

	@Inject
	@Named(DomainContextAuthorizer.APP_ACL_CTX)
	private ACLCtxManager		aclMgr;

	@Inject
	@Named(DomainAuthorizer.SELECTED_DOMAIN_ACL)
	private ACLCtxManager		domACLMgr;

	@Inject
	@Named(DomainContextAuthorizer.SELECTED_DOMAIN_CONTEXT_ACL)
	private ACLCtxManager		ctxACLMgr;

	@Inject
	@Named(DomainContextAuthorizer.SELECTED_CONTEXT)
	private StringManager		contextMgr;

	@Inject
	@Named(DomainAuthorizer.SELECTED_DOMAIN)
	private StringManager		domainMgr;

	@Inject
	private LoggedInUserManager	logInUserMgr;

	@Inject
	private ApplicationManager	appm;

	@Inject
	private ContextManager		ctxm;

	@Inject
	private UserManager			um;
	@Inject
	private AuthenticateManager	am;
	private static final String	TEST_DOMAIN		= "testdom.com";
	private static final String	TEST_CONTEXT	= "Test-context";
	private final static String	TEST_USER_NAME	= "test.user";

	@Inject
	private DBSetupHelper		helper;

	@Before
	@Transactional
	public void setup() {
		clean();
		helper.createDbSetup();
	}

	@After
	@Transactional
	public void clean() {
		em.get().createQuery("DELETE FROM UserApplicationCtxRegistration uacr").executeUpdate();
		em.get().createQuery("DELETE FROM UserApplicationRegistration uar").executeUpdate();
		em.get().createQuery("DELETE FROM Context ctx").executeUpdate();
		em.get().createQuery("DELETE FROM Application app").executeUpdate();
		em.get().createQuery("DELETE FROM User u").executeUpdate();
	}

	@Test
	@Transactional
	public void createUpdate() {
		AuthenticateMangerTest amt = injector.getInstance(AuthenticateMangerTest.class);
		UserInfo ui = new UserInfo();
		ui.setUserName(TEST_USER_NAME);
		ui.setFirstName("fname");
		ui.setLastName("lname");
		ui.setTitle("Ms");
		ui.setCurrentLogingDomain(UserAdminURLGenerator.USERADMIN);
		ui.setEmailId("test@email.com");
		ui.setPassword(SHA256.getHashedString("password"));
		ui.setDefaultLanguage(Language.ENGLISH);
		String id = amt.signupUser(ui);
		Assert.assertNotNull(id);

		AccessControlList acl = new AccessControlList();
		acl.addPermission(Permissions.CREATE_APPLICATION);

		UserAccount cu = new UserAccount();
		cu.setId("1");
		logInUserMgr.setCurrentUser(cu);

		aclMgr.setContext(new ACLContext(acl));
		Application application = new Application();
		application.setBusinessName("Test Business");
		application.setDomain(TEST_DOMAIN);
		application.setStatus(GeneralStatus.ACTIVE);
		application.setURL("http://www." + TEST_DOMAIN);
		application.setContactEmail("contact@email.com");
		try {
			appm.createApplication(application);
			Assert.assertNotNull(application.getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		Application retrieved = em.get().find(Application.class, application.getId());
		Assert.assertEquals(application, retrieved);

		acl = new AccessControlList(true);
		domACLMgr.setContext(new ACLContext(acl));
		domainMgr.setValue(TEST_DOMAIN);
		Context ctx = new Context();
		ctx.setName(TEST_CONTEXT);
		try {
			ctxm.create(ctx);
			Assert.assertNotNull(ctx.getId());
		} catch (UserAdminException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		Context retrievedCtx = em.get().find(Context.class, ctx.getId());
		Assert.assertEquals(ctx, retrievedCtx);

		try {
			ctxm.create(retrievedCtx);
			Assert.assertTrue(false);
		} catch (UserNotAutherisedException e) {
			Assert.assertTrue(true);
		} catch (UserAdminException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		acl = new AccessControlList(true);
		ctxACLMgr.setContext(new ACLContext(acl));
		contextMgr.setValue(TEST_CONTEXT);

		try {
			Context nextRetrieved = ctxm.getContextById(ctx.getId());
			Assert.assertEquals(ctx, nextRetrieved);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		List<Context> list = ctxm.list(null, null);
		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(ctx, list.get(0));

		try {
			Context context = ctxm.getContextByName(TEST_CONTEXT);
			Assert.assertNotNull(context);
			Assert.assertEquals(ctx, context);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		Credential cred = new Credential();
		cred.setUsername(TEST_USER_NAME);
		String salt = java.util.UUID.randomUUID().toString();
		cred.setPassword(SHA256.getHashedString(SHA256.getHashedString("password") + salt));
		cred.setDomain(TEST_DOMAIN);
		try {
			am.registerWithNewDomain(cred, salt);
			Assert.assertTrue(true);
		} catch (UserAdminException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		} catch (CriticalException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		cu = new UserAccount();
		cu.setId(id);
		logInUserMgr.setCurrentUser(cu);

		try {
			um.registerWithContext(TEST_CONTEXT, id);
			Assert.assertTrue(true);
		} catch (UserAdminException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		List<UserApplicationCtxRegistration> regList = ctxm.getUsersByContext(null, null, null);
		Assert.assertNotNull(regList);
		Assert.assertEquals(2, regList.size());

		cu = new UserAccount();
		cu.setId("1");
		cu.setUserName("root");
		logInUserMgr.setCurrentUser(cu);

		try {
			ctxm.transferOwnerShip(TEST_USER_NAME);
			Assert.assertTrue(true);
		} catch (UserAdminException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		regList = ctxm.getUsersByContext(null, null, null);
		Assert.assertNotNull(regList);
		Assert.assertEquals(2, regList.size());

		for (UserApplicationCtxRegistration userApplicationCtxRegistration : regList) {
			if (userApplicationCtxRegistration.getUser().getUserName().equals(TEST_USER_NAME)) {
				Assert.assertTrue(userApplicationCtxRegistration.getOwner());
			}
		}
	}
}
