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
import org.junit.Test;
import org.junit.runner.RunWith;

import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.injector.CommonModule;
import com.agnie.common.server.auth.ACLContext;
import com.agnie.common.test.injector.GuiceTestRunner;
import com.agnie.common.test.injector.WithModules;
import com.agnie.common.test.providers.ACLCtxManager;
import com.agnie.common.test.providers.LoggedInUserManager;
import com.agnie.common.test.providers.StringManager;
import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.agnie.useradmin.persistance.client.enums.GeneralStatus;
import com.agnie.useradmin.persistance.client.enums.UserStatus;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.server.BaseUserAdminTest;
import com.agnie.useradmin.persistance.server.auth.DomainAuthorizer;
import com.agnie.useradmin.persistance.server.auth.DomainContextAuthorizer;
import com.agnie.useradmin.persistance.server.entity.Application;
import com.agnie.useradmin.persistance.server.entity.Permission;
import com.agnie.useradmin.persistance.server.entity.User;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminModule;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminPersistModule;
import com.agnie.useradmin.session.client.helper.UserNotAutherisedException;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.persist.Transactional;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestUserAdminModule.class, TestUserAdminPersistModule.class, CommonModule.class })
public class PermissioManagerTest extends BaseUserAdminTest {

	@Inject
	@Named(DomainContextAuthorizer.APP_ACL_CTX)
	private ACLCtxManager		aclMgr;

	@Inject
	@Named(DomainAuthorizer.SELECTED_DOMAIN_ACL)
	private ACLCtxManager		domACLMgr;

	@Inject
	@Named(DomainAuthorizer.SELECTED_DOMAIN)
	private StringManager		domainMgr;

	@Inject
	private LoggedInUserManager	logInUserMgr;

	@Inject
	private PermissionManager	pm;
	@Inject
	private ApplicationManager	appm;
	private static final String	TEST_DOMAIN	= "testdom.com";

	@After
	@Transactional
	public void clean() {
		em.get().createQuery("DELETE FROM UserApplicationRegistration upr").executeUpdate();
		em.get().createQuery("DELETE FROM Permission perm").executeUpdate();
		em.get().createQuery("DELETE FROM Role role").executeUpdate();
		em.get().createQuery("DELETE FROM Application app").executeUpdate();
		em.get().createQuery("DELETE FROM User u").executeUpdate();
	}

	@Test
	@Transactional
	public void createUpdate() {
		User us = new User();
		us.setTitle("Mr");
		us.setEmailId("test@gmail.com");
		us.setFirstName("TestFname");
		us.setLastName("LastName");
		us.setUserName("user3");
		us.setPassword("password");
		us.setStatus(UserStatus.ACTIVE);
		em.get().persist(us);

		AccessControlList acl = new AccessControlList();
		acl.addPermission(Permissions.CREATE_APPLICATION);
		aclMgr.setContext(new ACLContext(acl));

		UserAccount cu = new UserAccount();
		cu.setId(us.getId());
		logInUserMgr.setCurrentUser(cu);

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

		acl = new AccessControlList();
		acl.addPermission(Permissions.CREATE_PERMISSION);
		domACLMgr.setContext(new ACLContext(acl));
		domainMgr.setValue(TEST_DOMAIN);
		Permission perm = new Permission();
		perm.setLevel(AuthLevel.APPLICATION);
		perm.setCode("PERM1.TEST");
		perm.setName("Some name");
		perm.setDescription("Some Description");
		try {
			perm = pm.save(perm);
			Assert.assertNotNull(perm);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		Permission actual = pm.getPermissionById(perm.getId());
		Assert.assertEquals(perm, actual);

		actual.setDescription("Description changed");
		try {
			perm = pm.save(actual);
			Assert.assertNotNull(perm);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		actual = pm.getPermissionById(perm.getId());
		Assert.assertEquals(perm, actual);

	}

	@Test
	@Transactional
	public void removelist() {
		User us = new User();
		us.setTitle("Mr.");
		us.setEmailId("test@gmail.com");
		us.setFirstName("TestFname");
		us.setLastName("LastName");
		us.setUserName("user3");
		us.setPassword("password");
		us.setStatus(UserStatus.ACTIVE);
		em.get().persist(us);

		AccessControlList acl = new AccessControlList();
		acl.addPermission(Permissions.CREATE_APPLICATION);
		aclMgr.setContext(new ACLContext(acl));

		UserAccount cu = new UserAccount();
		cu.setId(us.getId());
		logInUserMgr.setCurrentUser(cu);

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

		acl = new AccessControlList();
		acl.addPermission(Permissions.CREATE_PERMISSION);
		domACLMgr.setContext(new ACLContext(acl));
		domainMgr.setValue(TEST_DOMAIN);
		Permission perm = new Permission();
		perm.setLevel(AuthLevel.APPLICATION);
		perm.setCode("PERM1.TEST");
		perm.setName("Some name");
		perm.setDescription("Some Description");
		try {
			perm = pm.save(perm);
			Assert.assertNotNull(perm);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		Permission actual = pm.getPermissionById(perm.getId());
		Assert.assertEquals(perm, actual);
		List<String> permids = new ArrayList<String>();
		List<Permission> perms = new ArrayList<Permission>();
		perms.add(actual);

		permids.add(perm.getId());
		perm = new Permission();
		perm.setLevel(AuthLevel.APPLICATION);
		perm.setCode("PERM1.TEST1");
		perm.setName("Some name");
		perm.setDescription("Some Description");

		try {
			perm = pm.save(perm);
			Assert.assertNotNull(perm);
			permids.add(perm.getId());
			perms.add(perm);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		List<Permission> actualList = pm.getPermissionsByApplication(null, AuthLevel.APPLICATION, null);
		Assert.assertEquals(2, actualList.size());

		acl = new AccessControlList();
		acl.addPermission(Permissions.DELETE_PERMISSION);
		domACLMgr.setContext(new ACLContext(acl));

		pm.removePermissionsByIds(permids);

		actualList = pm.getPermissionsByApplication(null, AuthLevel.APPLICATION, null);
		Assert.assertEquals(0, actualList.size());

	}

}
