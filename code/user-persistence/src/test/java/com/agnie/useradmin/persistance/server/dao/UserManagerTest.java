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
import com.agnie.useradmin.persistance.client.enums.UserStatus;
import com.agnie.useradmin.persistance.client.exception.OldPasswordDontMatch;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.server.BaseUserAdminTest;
import com.agnie.useradmin.persistance.server.auth.DomainContextAuthorizer;
import com.agnie.useradmin.persistance.server.entity.User;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminModule;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminPersistModule;
import com.agnie.useradmin.persistance.server.listrequest.PageNSort;
import com.agnie.useradmin.session.client.helper.UserNotAutherisedException;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.persist.Transactional;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestUserAdminModule.class, TestUserAdminPersistModule.class, CommonModule.class })
public class UserManagerTest extends BaseUserAdminTest {

	@Inject
	@Named(DomainContextAuthorizer.APP_ACL_CTX)
	private ACLCtxManager		aclMgr;

	@Inject
	private LoggedInUserManager	logInUserMgr;

	@Inject
	private UserManager			um;

	@After
	@Transactional
	public void clean() {
		em.get().createQuery("DELETE FROM User u").executeUpdate();
	}

	@Test
	public void createAndRetrieve() {
		AccessControlList acl = new AccessControlList();
		acl.addPermission(Permissions.CREATE_USER);
		aclMgr.setContext(new ACLContext(acl));
		User us = new User();
		us.setTitle("Mr.");
		us.setLastName("LastName");
		us.setEmailId("test@gmail.com");
		us.setFirstName("TestFname");
		us.setUserName("user1");
		us.setPassword("password");
		us.setStatus(UserStatus.ACTIVE);
		try {
			um.saveUser(us);
			Assert.assertNotNull(us.getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		try {
			User result;
			result = um.getUserById(us.getId());
			Assert.assertEquals(us, result);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		try {
			User result;
			result = um.getUserByUserName(us.getUserName());
			Assert.assertEquals(us, result);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		try {
			User result;
			result = um.getUserByEmailId(us.getEmailId());
			Assert.assertEquals(us, result);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}

	@Test
	public void editTest() {
		AccessControlList acl = new AccessControlList();
		acl.addPermission(Permissions.CREATE_USER);
		aclMgr.setContext(new ACLContext(acl));
		User us = new User();
		us.setTitle("Mr.");
		us.setLastName("LastName");
		us.setEmailId("test@gmail.com");
		us.setFirstName("TestFname");
		us.setUserName("user2");
		us.setPassword("password");
		us.setStatus(UserStatus.ACTIVE);
		try {
			um.saveUser(us);
			Assert.assertNotNull(us.getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		try {
			User edit = um.getUserById(us.getId());
			edit.setLastName("Last Name");
			acl = new AccessControlList();
			acl.addPermission(Permissions.EDIT_USER_DETAIL);
			aclMgr.setContext(new ACLContext(acl));
			um.saveUser(edit);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		try {
			User edit = um.getUserById(us.getId());
			edit.setLastName("Last Name New");
			acl = new AccessControlList();
			acl.addPermission(Permissions.VIEW_USER);
			aclMgr.setContext(new ACLContext(acl));
			UserAccount cu = new UserAccount();
			cu.setId(us.getId());
			logInUserMgr.setCurrentUser(cu);
			um.saveUser(edit);
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		try {
			um.resetPassword(us.getId(), "passwordwrong", "newpassword");
			Assert.assertTrue(false);
		} catch (OldPasswordDontMatch e) {
			Assert.assertTrue(true);
		}

		try {
			um.resetPassword(us.getId(), "password", "newpassword");
			Assert.assertTrue(true);
		} catch (OldPasswordDontMatch e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		User us1 = um.getUserById(us.getId());
		Assert.assertEquals("newpassword", us1.getPassword());
		Assert.assertEquals("Last Name New", us1.getLastName());
	}

	@Test
	public void editNegativeTest() {
		AccessControlList acl = new AccessControlList();
		acl.addPermission(Permissions.CREATE_USER);
		aclMgr.setContext(new ACLContext(acl));
		User us = new User();
		us.setTitle("Mr.");
		us.setLastName("LastName");
		us.setEmailId("test@gmail.com");
		us.setFirstName("TestFname");
		us.setUserName("user2");
		us.setPassword("password");
		us.setStatus(UserStatus.ACTIVE);
		try {
			um.saveUser(us);
			Assert.assertNotNull(us.getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		try {
			User edit = um.getUserById(us.getId());
			edit.setLastName("Last Name");
			acl = new AccessControlList();
			acl.addPermission(Permissions.VIEW_USER);
			aclMgr.setContext(new ACLContext(acl));
			um.saveUser(edit);
			Assert.assertTrue(false);
		} catch (UserNotAutherisedException e) {
			Assert.assertTrue(true);
		}

		try {
			User edit = um.getUserById(us.getId());
			edit.setLastName("Last Name New");
			acl = new AccessControlList();
			acl.addPermission(Permissions.VIEW_USER);
			aclMgr.setContext(new ACLContext(acl));
			UserAccount cu = new UserAccount();
			cu.setId("6576dsfffj45645fgh");
			logInUserMgr.setCurrentUser(cu);
			um.saveUser(edit);
			Assert.assertTrue(false);
		} catch (UserNotAutherisedException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void removeTest() {
		AccessControlList acl = new AccessControlList();
		acl.addPermission(Permissions.CREATE_USER);
		aclMgr.setContext(new ACLContext(acl));
		User us = new User();
		us.setTitle("Mr.");
		us.setLastName("LastName");
		us.setEmailId("test@gmail.com");
		us.setFirstName("TestFname");
		us.setUserName("user2");
		us.setPassword("password");
		us.setStatus(UserStatus.ACTIVE);
		try {
			um.saveUser(us);
			Assert.assertNotNull(us.getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		try {
			acl = new AccessControlList();
			acl.addPermission(Permissions.DELETE_USER);
			aclMgr.setContext(new ACLContext(acl));
			User user = um.getUserById(us.getId());
			um.removeUser(user);

		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		try {
			User deleted = um.getUserById(us.getId());
			Assert.assertEquals(UserStatus.DELETED, deleted.getStatus());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		List<String> ids = new ArrayList<String>();
		acl = new AccessControlList();
		acl.addPermission(Permissions.CREATE_USER);
		aclMgr.setContext(new ACLContext(acl));
		us = new User();
		us.setTitle("Mr.");
		us.setLastName("LastName");
		us.setEmailId("test3@gmail.com");
		us.setFirstName("TestFname");
		us.setUserName("user3");
		us.setPassword("password");
		us.setStatus(UserStatus.ACTIVE);
		try {
			ids.add(um.saveUser(us));
			Assert.assertNotNull(us.getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		us = new User();
		us.setTitle("Mr.");
		us.setLastName("LastName");
		us.setEmailId("test4@gmail.com");
		us.setFirstName("TestFname");
		us.setUserName("user4");
		us.setPassword("password");
		us.setStatus(UserStatus.ACTIVE);
		try {
			ids.add(um.saveUser(us));
			Assert.assertNotNull(us.getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		us = new User();
		us.setTitle("Mr.");
		us.setLastName("LastName");
		us.setEmailId("test5@gmail.com");
		us.setFirstName("TestFname");
		us.setUserName("user5");
		us.setPassword("password");
		us.setStatus(UserStatus.ACTIVE);
		try {
			ids.add(um.saveUser(us));
			Assert.assertNotNull(us.getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		try {
			acl = new AccessControlList();
			acl.addPermission(Permissions.DELETE_USER);
			aclMgr.setContext(new ACLContext(acl));
			um.removeUserFromIds(ids);

		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		try {
			for (String id : ids) {
				User deleted = um.getUserById(id);
				Assert.assertEquals(UserStatus.DELETED, deleted.getStatus());

			}
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}

	@Test
	public void negativeRemoveTest() {
		AccessControlList acl = new AccessControlList();
		acl.addPermission(Permissions.CREATE_USER);
		aclMgr.setContext(new ACLContext(acl));
		User us = new User();
		us.setTitle("Mr.");
		us.setLastName("LastName");
		us.setEmailId("test@gmail.com");
		us.setFirstName("TestFname");
		us.setUserName("user2");
		us.setPassword("password");
		us.setStatus(UserStatus.ACTIVE);
		try {
			um.saveUser(us);
			Assert.assertNotNull(us.getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		try {
			acl = new AccessControlList();
			acl.addPermission(Permissions.EDIT_USER);
			aclMgr.setContext(new ACLContext(acl));
			User user = um.getUserById(us.getId());
			um.removeUser(user);
			Assert.assertTrue(false);
		} catch (UserNotAutherisedException e) {
			Assert.assertTrue(true);
		}

		List<String> ids = new ArrayList<String>();
		acl = new AccessControlList();
		acl.addPermission(Permissions.CREATE_USER);
		aclMgr.setContext(new ACLContext(acl));
		us = new User();
		us.setTitle("Mr.");
		us.setLastName("LastName");
		us.setEmailId("test3@gmail.com");
		us.setFirstName("TestFname");
		us.setUserName("user3");
		us.setPassword("password");
		us.setStatus(UserStatus.ACTIVE);
		try {
			ids.add(um.saveUser(us));
			Assert.assertNotNull(us.getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		us = new User();
		us.setTitle("Mr.");
		us.setLastName("LastName");
		us.setEmailId("test4@gmail.com");
		us.setFirstName("TestFname");
		us.setUserName("user4");
		us.setPassword("password");
		us.setStatus(UserStatus.ACTIVE);
		try {
			ids.add(um.saveUser(us));
			Assert.assertNotNull(us.getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		us = new User();
		us.setTitle("Mr.");
		us.setLastName("LastName");
		us.setEmailId("test5@gmail.com");
		us.setFirstName("TestFname");
		us.setUserName("user5");
		us.setPassword("password");
		us.setStatus(UserStatus.ACTIVE);
		try {
			ids.add(um.saveUser(us));
			Assert.assertNotNull(us.getId());
		} catch (UserNotAutherisedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		try {
			acl = new AccessControlList();
			acl.addPermission(Permissions.EDIT_USER);
			aclMgr.setContext(new ACLContext(acl));
			um.removeUserFromIds(ids);
			Assert.assertTrue(false);
		} catch (UserNotAutherisedException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	@Transactional
	public void listTest() {
		User us = new User();
		us.setTitle("Mr.");
		us.setLastName("LastName");
		us.setEmailId("test@gmail.com");
		us.setFirstName("TestFname");
		us.setUserName("user3");
		us.setPassword("password");
		us.setStatus(UserStatus.PENDING_FOR_VERIFICATION);
		em.get().persist(us);
		us = new User();
		us.setTitle("Mr.");
		us.setLastName("LastName");
		us.setEmailId("test2@gmail.com");
		us.setFirstName("TestFname2");
		us.setUserName("user4");
		us.setPassword("password2");
		us.setStatus(UserStatus.PENDING_FOR_VERIFICATION);
		em.get().persist(us);

		us = new User();
		us.setTitle("Mr.");
		us.setLastName("LastName");
		us.setEmailId("pranoti.patil@gmail.com");
		us.setFirstName("Pranoti");
		us.setLastName("Patil");
		us.setUserName("pranoti.patil");
		us.setPassword("password2");
		us.setStatus(UserStatus.ACTIVE);
		em.get().persist(us);

		AccessControlList acl = new AccessControlList();
		acl.addPermission(Permissions.VIEW_USER);
		aclMgr.setContext(new ACLContext(acl));
		List<User> list = um.getUsers(new PageNSort(), null, null);
		Assert.assertEquals(3, list.size());
		list = um.getUsers(new PageNSort(), UserStatus.ACTIVE, null);
		Assert.assertEquals(1, list.size());
		list = um.getUsers(new PageNSort(), null, "pranoti");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals("pranoti.patil", list.get(0).getUserName());
		list = um.getUsers(new PageNSort(), UserStatus.PENDING_FOR_VERIFICATION, "test");
		Assert.assertEquals(2, list.size());
	}
}
