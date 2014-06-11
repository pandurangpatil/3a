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

import java.util.ArrayList;
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
import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.agnie.useradmin.persistance.client.enums.GeneralStatus;
import com.agnie.useradmin.persistance.client.enums.Language;
import com.agnie.useradmin.persistance.client.enums.RequestStatus;
import com.agnie.useradmin.persistance.client.enums.UserStatus;
import com.agnie.useradmin.persistance.client.exception.CriticalException;
import com.agnie.useradmin.persistance.client.exception.DomainAuthException;
import com.agnie.useradmin.persistance.client.exception.InvalidOperationException;
import com.agnie.useradmin.persistance.client.exception.UserAdminException;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.persistance.client.service.dto.Credential;
import com.agnie.useradmin.persistance.client.service.dto.UserInfo;
import com.agnie.useradmin.persistance.server.BaseUserAdminTest;
import com.agnie.useradmin.persistance.server.auth.DomainAuthorizer;
import com.agnie.useradmin.persistance.server.auth.DomainContextAuthorizer;
import com.agnie.useradmin.persistance.server.entity.Application;
import com.agnie.useradmin.persistance.server.entity.Role;
import com.agnie.useradmin.persistance.server.entity.User;
import com.agnie.useradmin.persistance.server.entity.UserApplicationRegistration;
import com.agnie.useradmin.persistance.server.helper.DBSetupHelper;
import com.agnie.useradmin.persistance.server.injector.TestSessionPersistModule;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminModule;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminPersistModule;
import com.agnie.useradmin.session.client.helper.UserNotAuthenticated;
import com.agnie.useradmin.session.client.helper.UserNotAutherisedException;
import com.agnie.useradmin.session.server.mybatis.SessionMyBatisModule;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.persist.Transactional;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestUserAdminModule.class, TestUserAdminPersistModule.class, TestSessionPersistModule.class, SessionMyBatisModule.class, CommonModule.class })
public class ApplicationManagerTest extends BaseUserAdminTest {
	private final static String	TEST_USER_NAME	= "test.user";
	@Inject
	@Named(DomainContextAuthorizer.APP_ACL_CTX)
	private ACLCtxManager		aclMgr;
	@Inject
	@Named(DomainAuthorizer.SELECTED_DOMAIN)
	private StringManager		domainMgr;
	@Inject
	@Named(DomainAuthorizer.SELECTED_DOMAIN_ACL)
	private ACLCtxManager		domACLMgr;

	@Inject
	private LoggedInUserManager	logInUserMgr;

	@Inject
	private ApplicationManager	appm;
	@Inject
	private AuthenticateManager	am;
	@Inject
	private RoleManager			rm;
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
		em.get().createQuery("DELETE FROM ApplicationRole apr").executeUpdate();
		em.get().createQuery("DELETE FROM UserApplicationRegistration upr").executeUpdate();
		em.get().createQuery("DELETE FROM Permission permission").executeUpdate();
		em.get().createQuery("DELETE FROM Role role").executeUpdate();
		em.get().createQuery("DELETE FROM Application app").executeUpdate();
		em.get().createQuery("DELETE FROM User u").executeUpdate();
	}

	@Test
	@Transactional
	public void createUpdate() {
		User us = new User();
		us.setTitle("MR.");
		us.setLastName("LastName");
		us.setEmailId("test@gmail.com");
		us.setFirstName("TestFname");
		us.setUserName("user3");
		us.setPassword("password");
		us.setStatus(UserStatus.ACTIVE);
		em.get().persist(us);

		AccessControlList acl = new AccessControlList();
		acl.addPermission(Permissions.CREATE_APPLICATION);

		UserAccount cu = new UserAccount();
		cu.setId(us.getId());
		logInUserMgr.setCurrentUser(cu);

		aclMgr.setContext(new ACLContext(acl));
		Application application = new Application();
		application.setBusinessName("Test Business");
		application.setDomain("testdom.com");
		application.setStatus(GeneralStatus.ACTIVE);
		application.setURL("http://www.test.com");
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

		retrieved.setIconURL("http://test.com/some.jpg");
		try {
			appm.createApplication(retrieved);
			Assert.assertTrue(false);
		} catch (UserNotAutherisedException e) {
			Assert.assertTrue(true);
		}

		acl = new AccessControlList();
		acl.addPermission(Permissions.APPLICATION);
		domACLMgr.setContext(new ACLContext(acl));
		try {
			appm.updateApplication(retrieved);
			Assert.assertTrue(false);
		} catch (UserNotAutherisedException e) {
			Assert.assertTrue(true);
		} catch (InvalidOperationException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		acl = new AccessControlList();
		acl.addPermission(Permissions.EDIT_APPLICATION_DETAILS);
		domACLMgr.setContext(new ACLContext(acl));
		domainMgr.setValue("testdom.com");
		try {
			appm.updateApplication(retrieved);
			Assert.assertTrue(true);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		} catch (InvalidOperationException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		try {
			Application nextRetrieved = appm.getApplicationById(application.getId());
			Assert.assertEquals("http://test.com/some.jpg", nextRetrieved.getIconURL());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}

	@Test
	public void multipleApplicationTest() {
		User us = new User();
		us.setTitle("MR.");
		us.setLastName("LastName");
		us.setEmailId("test@gmail.com");
		us.setFirstName("TestFname");
		us.setUserName("user3");
		us.setPassword("password");
		us.setStatus(UserStatus.ACTIVE);
		em.get().persist(us);

		AccessControlList acl = new AccessControlList();
		acl.addPermission(Permissions.CREATE_APPLICATION);

		UserAccount cu = new UserAccount();
		cu.setId(us.getId());
		logInUserMgr.setCurrentUser(cu);

		aclMgr.setContext(new ACLContext(acl));
		Application application = new Application();
		application.setBusinessName("Test Business");
		application.setDomain("testdom.com");
		application.setStatus(GeneralStatus.ACTIVE);
		application.setURL("http://www.testdom.com");
		application.setContactEmail("contact@email.com");
		try {
			appm.createApplication(application);
			Assert.assertNotNull(application.getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		application = new Application();
		application.setBusinessName("Test Business another domain");
		application.setDomain("testnewdomain.com");
		application.setStatus(GeneralStatus.ACTIVE);
		application.setURL("http://www.testnewdomain.com");
		application.setContactEmail("contact@email.com");
		try {
			appm.createApplication(application);
			Assert.assertNotNull(application.getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		application = new Application();
		application.setBusinessName("Test Delete Business another domain");
		application.setDomain("deleteddomain.com");
		application.setStatus(GeneralStatus.DELETED);
		application.setURL("http://www.deleteddomain.com");
		application.setContactEmail("contact@email.com");
		try {
			appm.createApplication(application);
			Assert.assertNotNull(application.getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		acl = new AccessControlList();
		acl.addPermission(Permissions.APPLICATION);
		domACLMgr.setContext(new ACLContext(acl));

		try {
			Application appFromDomain = appm.getApplicationByDomainName("testnewdomain.com");
			Assert.assertNotNull(appFromDomain);
			Assert.assertEquals("Test Business another domain", appFromDomain.getBusinessName());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		try {
			Application appFromDomain = appm.getApplicationByDomainName("deleteddomain.com");
			Assert.assertNull(appFromDomain);
		} catch (UserNotAutherisedException e) {
			Assert.assertTrue(true);
		}
		logInUserMgr.setCurrentUser(null);
		try {
			@SuppressWarnings("unused")
			List<Application> apps = appm.getRegisteredApps();
			Assert.assertTrue(false);
		} catch (UserNotAutherisedException e) {
			Assert.assertTrue(true);
		}

		cu = new UserAccount();
		cu.setId(us.getId());
		logInUserMgr.setCurrentUser(cu);
		try {
			List<Application> apps = appm.getRegisteredApps();
			Assert.assertNotNull(apps);
			Assert.assertEquals(2, apps.size());
			application = apps.get(0);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		try {
			appm.removeApplication(application);
			Assert.assertTrue(false);
		} catch (UserNotAutherisedException ex) {
			Assert.assertTrue(true);
		}

		acl = new AccessControlList(true);
		domACLMgr.setContext(new ACLContext(acl));
		try {
			appm.removeApplication(application);
			Assert.assertTrue(true);
		} catch (UserNotAutherisedException ex) {
			ex.printStackTrace();
			Assert.assertTrue(false);
		}

		try {
			List<Application> apps = appm.getRegisteredApps();
			Assert.assertNotNull(apps);
			Assert.assertEquals(1, apps.size());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}

	@Test
	public void multipleRegisteredApps() {
		String TEST_DOMAIN = "testdom.com";
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
		// "1" is Id of root user.
		cu.setId("1");
		logInUserMgr.setCurrentUser(cu);
		aclMgr.setContext(new ACLContext(acl));

		Application application = new Application();
		application.setBusinessName("Test Business");
		application.setDomain(TEST_DOMAIN);
		application.setStatus(GeneralStatus.ACTIVE);
		application.setURL("http://www.testdom.com");
		application.setContactEmail("contact@email.com");
		try {
			appm.createApplication(application);
			Assert.assertNotNull(application.getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		application = new Application();
		application.setBusinessName("Test Business");
		application.setDomain(TEST_DOMAIN + ".in");
		application.setStatus(GeneralStatus.ACTIVE);
		application.setURL("http://www.testdom.com.in");
		application.setContactEmail("contact@email.com");
		try {
			appm.createApplication(application);
			Assert.assertNotNull(application.getId());
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
			List<Application> apps = appm.getRegisteredApps();
			Assert.assertNotNull(apps);
			// Assert.assertEquals(3, apps.size());
			Assert.assertEquals(1, apps.size());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		cu = new UserAccount();
		cu.setId(id);
		logInUserMgr.setCurrentUser(cu);
		domainMgr.setValue(TEST_DOMAIN);
		acl = new AccessControlList();
		acl.addPermission(Permissions.APPLICATION_USER_MANAGER);
		domACLMgr.setContext(new ACLContext(acl));
		List<String> ids = new ArrayList<String>();
		try {
			List<UserApplicationRegistration> users = appm.getUsersByApplication(null, null, null);
			Assert.assertNotNull(users);
			Assert.assertEquals(2, users.size());
			ids.add(users.get(0).getId());
			ids.add(users.get(1).getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		try {
			String homeUrl = appm.checkIfUserIsRegistered(TEST_DOMAIN);
			Assert.assertNotNull(homeUrl);
			Assert.assertEquals("http://www.testdom.com", homeUrl);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		} catch (UserNotAuthenticated e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		} catch (UserAdminException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		try {
			appm.checkIfUserIsRegistered(TEST_DOMAIN + ".in");
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		} catch (UserNotAuthenticated e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		} catch (DomainAuthException e) {
			Assert.assertTrue(true);
		} catch (UserAdminException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		acl = new AccessControlList(true);
		cu = new UserAccount();
		// "1" is Id of root user.
		cu.setId("1");
		logInUserMgr.setCurrentUser(cu);
		domACLMgr.setContext(new ACLContext(acl));
		try {
			// NOTE: Even though we are trying to set request status of owner of the application. But it will not allow
			// you to update the status of the owner it will be still ACTIVE. Thats where below check will show only
			// single user with PROVISIONAL RequestStatus
			appm.updateStatusByUserAppRegId(ids, RequestStatus.PROVISIONAL);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		Role role1 = new Role();
		role1.setLevel(AuthLevel.APPLICATION);
		role1.setName("First Role");
		role1.setDescription("Tes role");
		try {
			role1 = rm.save(role1);
			Assert.assertNotNull(role1);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		Role role2 = new Role();
		role2.setLevel(AuthLevel.APPLICATION);
		role2.setName("SFirst Role");
		role2.setDescription("Tes role");
		try {
			role2 = rm.save(role2);
			Assert.assertNotNull(role2);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		List<Role> userAppRoles = new ArrayList<Role>();
		userAppRoles.add(role1);
		userAppRoles.add(role2);

		try {
			List<UserApplicationRegistration> users = appm.getUsersByApplication(null, RequestStatus.PROVISIONAL, null);
			Assert.assertNotNull(users);
			Assert.assertEquals(1, users.size());
			List<Role> roles = appm.getUserApplicationRoles(users.get(0));
			Assert.assertNull(roles);
			UserApplicationRegistration uar = appm.updateUserApplicationRoles(users.get(0), userAppRoles);
			Assert.assertNotNull(uar);
			users = appm.getUsersByApplication(null, RequestStatus.PROVISIONAL, null);
			roles = appm.getUserApplicationRoles(users.get(0));
			Assert.assertNotNull(roles);
			Assert.assertEquals(userAppRoles, roles);

		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		} catch (InvalidOperationException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}

	// @Test
	// TODO: Need to update this test case once respective changes are done.
	public void ownerShipTransferTest() {
		String TEST_DOMAIN = "testdom.com";
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
		AccessControlList acl = new AccessControlList(true);
		UserAccount cu = new UserAccount();
		// "1" is Id of root user.
		cu.setId("1");
		logInUserMgr.setCurrentUser(cu);
		aclMgr.setContext(new ACLContext(acl));

		Application application = new Application();
		application.setBusinessName("Test Business");
		application.setDomain(TEST_DOMAIN);
		application.setStatus(GeneralStatus.ACTIVE);
		application.setURL("http://www.testdom.com");
		application.setContactEmail("contact@email.com");
		try {
			appm.createApplication(application);
			Assert.assertNotNull(application.getId());
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
			List<Application> apps = appm.getRegisteredApps();
			Assert.assertNotNull(apps);
			// Assert.assertEquals(3, apps.size());
			Assert.assertEquals(1, apps.size());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		acl = new AccessControlList(true);
		cu = new UserAccount();
		// "1" is Id of root user.
		cu.setId("1");
		cu.setUserName("root");
		logInUserMgr.setCurrentUser(cu);
		domACLMgr.setContext(new ACLContext(acl));
		domainMgr.setValue(TEST_DOMAIN);
		try {
			appm.transferOwnerShip(TEST_USER_NAME);
			Assert.assertTrue(true);
		} catch (UserAdminException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		List<UserApplicationRegistration> users = appm.getUsersByApplication(null, null, null);
		Assert.assertNotNull(users);
		Assert.assertEquals(2, users.size());
		for (UserApplicationRegistration userApplicationRegistration : users) {
			if (userApplicationRegistration.getUser().getUserName().equals(TEST_USER_NAME)) {
				Assert.assertTrue(userApplicationRegistration.getOwner());
			}
		}
	}
}
