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

import java.util.ArrayList;
import java.util.List;

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
import com.agnie.useradmin.persistance.client.enums.GeneralStatus;
import com.agnie.useradmin.persistance.client.enums.UserStatus;
import com.agnie.useradmin.persistance.server.BaseUserAdminTest;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminModule;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminPersistModule;
import com.google.inject.persist.Transactional;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestUserAdminModule.class, TestUserAdminPersistModule.class, CommonModule.class })
public class ApplicationTest extends BaseUserAdminTest {
	private static final String	APP_ID1			= "Sample-Application-ID1";
	private static final String	APP_ID2			= "Sample-Application-ID2";
	private static final String	APP_USER_ID1	= "Sample-Application-User-ID1";

	@Before
	@After
	@Transactional
	public void clean() throws Exception {
		try {
			List<String> ids = new ArrayList<String>();
			ids.add(APP_ID1);
			ids.add(APP_ID2);
			em.get().createQuery("DELETE FROM Application a WHERE a.id IN :ids").setParameter("ids", ids).executeUpdate();
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
			user.setId(APP_USER_ID1);
			user.setFirstName("firstName");
			user.setLastName("lastName");
			user.setUserName("userName");
			user.setEmailId("emailId@email.com");
			user.setPassword(SHA256.getHashedString("passtoken"));
			user.setStatus(UserStatus.ACTIVE);
			user.setTitle("Mr");
			em.persist(user);

			Application dm = new Application();
			dm.setId(APP_ID1);
			dm.setBusinessName("BuissnesName");
			dm.setDomain("domain111");
			dm.setURL("http://localhost/useradmin.html");
			dm.setStatus(GeneralStatus.ACTIVE);
			dm.setContactEmail("contact@email.com");
			em.persist(dm);
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail();
		}
		EntityManager em = getEntityManager();
		Application dmf = em.find(Application.class, APP_ID1);
		Assert.assertNotNull(dmf);
	}

	@Test
	@Transactional
	public void remove() {
		try {
			EntityManager em = getEntityManager();
			Application dm = new Application();
			dm.setId(APP_ID2);
			dm.setBusinessName("BuissnesName");
			dm.setDomain("domain1112");
			dm.setURL("http://localhost/useradmin.html");
			dm.setContactEmail("contact@email.com");
			em.persist(dm);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			EntityManager em = getEntityManager();
			Application dmf = em.find(Application.class, APP_ID2);
			em.remove(dmf);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		EntityManager em = getEntityManager();
		Application dmf = em.find(Application.class, APP_ID2);
		Assert.assertNull(dmf);
	}

}
