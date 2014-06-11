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
package com.agnie.useradmin.persistance.server.entity;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.agnie.common.helper.SHA256;
import com.agnie.common.injector.CommonModule;
import com.agnie.common.test.injector.GuiceTestRunner;
import com.agnie.common.test.injector.WithModules;
import com.agnie.useradmin.persistance.client.enums.UserStatus;
import com.agnie.useradmin.persistance.server.BaseUserAdminTest;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminModule;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminPersistModule;
import com.google.inject.persist.Transactional;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestUserAdminModule.class, TestUserAdminPersistModule.class, CommonModule.class })
public class UserTest extends BaseUserAdminTest {

	private static final String	USER_ID1	= "Sample-User-ID1";
	private static final String	USER_ID2	= "Sample-User-ID2";

	@Before
	@After
	@Transactional
	public void clean() throws Exception {
		try {
			em.get().createQuery("DELETE FROM User", User.class).executeUpdate();
			Assert.assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	@Transactional
	public void create() {
		try {
			EntityManager em = getEntityManager();
			User user = new User();
			user.setId(USER_ID1);
			user.setFirstName("firstName");
			user.setLastName("lastName");
			user.setUserName("userName");
			user.setEmailId("emailId@email.com");
			user.setPassword(SHA256.getHashedString("passtoken"));
			user.setStatus(UserStatus.ACTIVE);
			user.setTitle("Mr");
			em.persist(user);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void remove() {
		try {
			EntityManager em = getEntityManager();
			User user = new User();
			user.setId(USER_ID2);
			user.setTitle("Mr.");
			user.setLastName("LastName");
			user.setFirstName("firstname");
			user.setUserName("userName2");
			user.setEmailId("emailId2@email.com");
			user.setPassword(SHA256.getHashedString("passtoken"));
			user.setStatus(UserStatus.ACTIVE);
			em.persist(user);

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		EntityManager em = getEntityManager();
		User us = em.find(User.class, USER_ID2);
		em.remove(us);
		us = em.find(User.class, USER_ID2);
		Assert.assertNull(us);
	}
}
