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
import com.agnie.useradmin.persistance.server.entity.Role;
import com.agnie.useradmin.persistance.server.entity.User;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminModule;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminPersistModule;
import com.agnie.useradmin.session.client.helper.UserNotAutherisedException;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.persist.Transactional;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestUserAdminModule.class, TestUserAdminPersistModule.class, CommonModule.class })
public class RoleManagerTest extends BaseUserAdminTest {

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
	@Inject
	private RoleManager			rm;

	private static final String	TEST_DOMAIN	= "testdom.com";

	@After
	@Transactional
	public void clean() {
		em.get().createQuery("DELETE FROM UserApplicationRegistration upr").executeUpdate();
		em.get().createQuery("DELETE FROM Role role").executeUpdate();
		em.get().createQuery("DELETE FROM Permission perm").executeUpdate();
		em.get().createQuery("DELETE FROM Application app").executeUpdate();
		em.get().createQuery("DELETE FROM User u").executeUpdate();
	}

	@Test
	@Transactional
	public void createUpdate() {
		User us = new User();
		us.setTitle("Mr.");
		us.setLastName("LastName");
		us.setEmailId("test@gmail.com");
		us.setFirstName("TestFname");
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
		Permission perm1 = new Permission();
		perm1.setLevel(AuthLevel.APPLICATION);
		perm1.setCode("PERM1.TEST");
		perm1.setName("First permission");
		perm1.setDescription("Some Description");
		try {
			perm1 = pm.save(perm1);
			Assert.assertNotNull(perm1);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		Permission actual = pm.getPermissionById(perm1.getId());
		Assert.assertEquals(perm1, actual);

		Permission perm2 = new Permission();
		perm2.setLevel(AuthLevel.APPLICATION);
		perm2.setCode("PERM2.TEST");
		perm2.setName("Second Permission");
		perm2.setDescription("Some Description");
		try {
			perm2 = pm.save(perm2);
			Assert.assertNotNull(perm2);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		actual = pm.getPermissionById(perm2.getId());
		Assert.assertEquals(perm2, actual);

		Permission perm3 = new Permission();
		perm3.setLevel(AuthLevel.APPLICATION);
		perm3.setCode("PERM3.TEST");
		perm3.setName("Second Permission");
		perm3.setDescription("Some Description");
		try {
			perm3 = pm.save(perm3);
			Assert.assertNotNull(perm3);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		actual = pm.getPermissionById(perm3.getId());
		Assert.assertEquals(perm3, actual);

		acl = new AccessControlList();
		acl.addPermission(Permissions.CREATE_ROLE);
		domACLMgr.setContext(new ACLContext(acl));
		domainMgr.setValue(TEST_DOMAIN);
		Role role = new Role();
		List<Permission> perms = new ArrayList<Permission>();
		perms.add(perm1);
		perms.add(perm2);
		role.setPermissions(perms);
		role.setLevel(AuthLevel.APPLICATION);
		role.setName("First Role");
		role.setDescription("Tes role");
		try {
			role = rm.save(role);
			Assert.assertNotNull(role);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		Role rlactual = rm.getRoleById(role.getId());
		Assert.assertEquals(role, rlactual);

		rlactual.setDescription("Description changed");
		perms = new ArrayList<Permission>();
		perms.add(perm1);
		perms.add(perm2);
		perms.add(perm3);
		rlactual.setPermissions(perms);
		try {
			role = rm.save(rlactual);
			Assert.assertNotNull(role);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		rlactual = rm.getRoleById(role.getId());
		Assert.assertEquals(role, rlactual);

		rlactual.setDescription("Description changed once again");
		perms = new ArrayList<Permission>();
		perms.add(perm2);
		perms.add(perm3);
		rlactual.setPermissions(perms);
		try {
			role = rm.save(rlactual);
			Assert.assertNotNull(role);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		rlactual = rm.getRoleById(role.getId());
		Assert.assertEquals(role, rlactual);

	}

	@Transactional
	public void removelist() {
		User us = new User();
		us.setTitle("Mr.");
		us.setLastName("LastName");
		us.setEmailId("test@gmail.com");
		us.setFirstName("TestFname");
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
		Permission perm1 = new Permission();
		perm1.setLevel(AuthLevel.APPLICATION);
		perm1.setCode("PERM1.TEST");
		perm1.setName("First permission");
		perm1.setDescription("Some Description");
		try {
			perm1 = pm.save(perm1);
			Assert.assertNotNull(perm1);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		Permission actual = pm.getPermissionById(perm1.getId());
		Assert.assertEquals(perm1, actual);

		Permission perm2 = new Permission();
		perm2.setLevel(AuthLevel.APPLICATION);
		perm2.setCode("PERM2.TEST");
		perm2.setName("Second Permission");
		perm2.setDescription("Some Description");
		try {
			perm2 = pm.save(perm2);
			Assert.assertNotNull(perm2);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		actual = pm.getPermissionById(perm2.getId());
		Assert.assertEquals(perm2, actual);

		acl = new AccessControlList();
		acl.addPermission(Permissions.CREATE_ROLE);
		domACLMgr.setContext(new ACLContext(acl));
		domainMgr.setValue(TEST_DOMAIN);
		Role role = new Role();
		List<Permission> perms = new ArrayList<Permission>();
		perms.add(perm1);
		perms.add(perm2);
		role.setPermissions(perms);
		role.setLevel(AuthLevel.APPLICATION);
		role.setName("First Role");
		role.setDescription("Tes role");
		try {
			role = rm.save(role);
			Assert.assertNotNull(role);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		Role rlactual = rm.getRoleById(role.getId());
		Assert.assertEquals(role, rlactual);
		List<String> roleIds = new ArrayList<String>();
		List<Role> roles = new ArrayList<Role>();
		roles.add(rlactual);
		roleIds.add(role.getId());

		role = new Role();
		perms = new ArrayList<Permission>();
		perms.add(perm1);
		role.setPermissions(perms);
		role.setLevel(AuthLevel.APPLICATION);
		role.setName("Second Role");
		role.setDescription("Test role");
		try {
			role = rm.save(role);
			Assert.assertNotNull(role);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		List<Role> actualList = rm.getRolesByApplication(null, AuthLevel.APPLICATION, null);
		Assert.assertEquals(roles, actualList);

		acl = new AccessControlList();
		acl.addPermission(Permissions.DELETE_ROLE);
		domACLMgr.setContext(new ACLContext(acl));

		rm.removeRolesByIds(roleIds);

		actualList = rm.getRolesByApplication(null, AuthLevel.APPLICATION, null);
		Assert.assertEquals(0, actualList.size());
	}

}
