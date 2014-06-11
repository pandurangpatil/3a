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
import com.agnie.useradmin.persistance.client.enums.RequestStatus;
import com.agnie.useradmin.persistance.server.BaseUserAdminTest;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminModule;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminPersistModule;
import com.google.inject.persist.Transactional;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestUserAdminModule.class, TestUserAdminPersistModule.class, CommonModule.class })
public class UserApplicationCtxRegistrationTest extends BaseUserAdminTest {

	@Before
	@After
	@Transactional
	public void clean() throws Exception {
		try {
			em.get().createQuery("DELETE FROM UserApplicationCtxRegistration", UserApplicationCtxRegistration.class).executeUpdate();
			em.get().createQuery("DELETE FROM UserApplicationRegistration", UserApplicationCtxRegistration.class).executeUpdate();
			em.get().createQuery("DELETE FROM Context", Context.class).executeUpdate();
			em.get().createQuery("DELETE FROM Application", Application.class).executeUpdate();
			em.get().createQuery("DELETE FROM ForgotPasswordSession", ForgotPasswordSession.class).executeUpdate();
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
		String id = "";
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
			ctx.setName("Some Context");
			ctx.setApplication(app);
			em.persist(ctx);

			UserApplicationRegistration apr = new UserApplicationRegistration();
			apr.setStatus(RequestStatus.ACTIVE);
			apr.setApp(app);
			apr.setUser(us);
			em.persist(apr);

			UserApplicationCtxRegistration apcr = new UserApplicationCtxRegistration();
			apcr.setStatus(RequestStatus.ACTIVE);
			apcr.setUsApplicationRegistration(apr);
			apcr.setContext(ctx);
			em.persist(apcr);
			id = apcr.getId();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			EntityManager em = getEntityManager();
			UserApplicationCtxRegistration apr = em.find(UserApplicationCtxRegistration.class, id);
			Assert.assertNotNull(apr);
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	@Transactional
	public void remove() {
		String id = "";
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
			ctx.setName("Some Context");
			ctx.setApplication(app);
			em.persist(ctx);

			UserApplicationRegistration apr = new UserApplicationRegistration();
			apr.setStatus(RequestStatus.ACTIVE);
			apr.setApp(app);
			apr.setUser(us);
			em.persist(apr);

			UserApplicationCtxRegistration apcr = new UserApplicationCtxRegistration();
			apcr.setStatus(RequestStatus.ACTIVE);
			apcr.setUsApplicationRegistration(apr);
			apcr.setContext(ctx);
			em.persist(apcr);
			id = apcr.getId();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			EntityManager em = getEntityManager();
			UserApplicationCtxRegistration apr = em.find(UserApplicationCtxRegistration.class, id);
			em.remove(apr);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			EntityManager em = getEntityManager();
			UserApplicationCtxRegistration apr = em.find(UserApplicationCtxRegistration.class, id);
			Assert.assertNull(apr);
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail();
		}
	}
}
