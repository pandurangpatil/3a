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

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.agnie.common.injector.CommonModule;
import com.agnie.common.test.injector.GuiceTestRunner;
import com.agnie.common.test.injector.WithModules;
import com.agnie.useradmin.persistance.client.enums.GeneralStatus;
import com.agnie.useradmin.persistance.server.BaseUserAdminTest;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminModule;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminPersistModule;
import com.google.inject.persist.Transactional;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestUserAdminModule.class, TestUserAdminPersistModule.class, CommonModule.class })
public class ContextTest extends BaseUserAdminTest {
	private static final String	CTX1_ID	= "sample-context-test-id1";

	private static final String	CTX2_ID	= "sample-context-test-id2";

	@Before
	@After
	@Transactional
	public void clean() throws Exception {
		try {
			em.get().createQuery("DELETE FROM Context", Context.class).executeUpdate();
			em.get().createQuery("DELETE FROM Application", Application.class).executeUpdate();
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
			User us = new User();
			us.setTitle("MR.");
			us.setLastName("LastName");
			us.setFirstName("Testfname");
			us.setUserName("testuname");
			us.setEmailId("test@email.com");
			us.setPassword("pwd");
			em.persist(us);

			Application app = new Application();
			app.setBusinessName("TestBusiness");
			app.setDomain("TEST_DOMAIN");
			app.setStatus(GeneralStatus.ACTIVE);
			app.setURL("TEST URL");
			app.setContactEmail("contact@email.com");
			em.persist(app);

			Context ctx = new Context();
			ctx.setId(CTX1_ID);
			ctx.setName("Name");
			ctx.setApplication(app);
			em.persist(ctx);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		try {
			EntityManager em = getEntityManager();
			Context ctx = em.find(Context.class, CTX1_ID);
			Assert.assertNotNull(ctx);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	@Transactional
	public void remove() {
		try {
			EntityManager em = getEntityManager();
			User us = new User();
			us.setTitle("MR.");
			us.setLastName("LastName");
			us.setFirstName("Testfname");
			us.setUserName("testuname");
			us.setEmailId("test@email.com");
			us.setPassword("pwd");
			em.persist(us);

			Application app = new Application();
			app.setBusinessName("TestBusiness");
			app.setDomain("TEST_DOMAIN");
			app.setStatus(GeneralStatus.ACTIVE);
			app.setURL("TEST URL");
			app.setContactEmail("contact@email.com");
			em.persist(app);

			Context ctx = new Context();
			ctx.setId(CTX2_ID);
			ctx.setName("Name");
			em.persist(ctx);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		try {
			EntityManager em = getEntityManager();
			Context ctx = em.find(Context.class, CTX2_ID);
			em.remove(ctx);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		try {
			EntityManager em = getEntityManager();
			Context ctx = em.find(Context.class, CTX2_ID);
			Assert.assertNull(ctx);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
