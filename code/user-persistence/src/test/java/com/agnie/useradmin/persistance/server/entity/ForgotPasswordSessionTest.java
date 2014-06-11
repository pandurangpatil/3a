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
import com.agnie.useradmin.persistance.server.BaseUserAdminTest;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminModule;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminPersistModule;
import com.google.inject.persist.Transactional;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestUserAdminModule.class, TestUserAdminPersistModule.class, CommonModule.class })
public class ForgotPasswordSessionTest extends BaseUserAdminTest {

	@Before
	@After
	@Transactional
	public void clean() throws Exception {
		try {
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
			us.setTitle("Mr.");
			us.setFirstName("Testfname");
			us.setUserName("testuname");
			us.setLastName("LastName");
			us.setEmailId("test@email.com");
			us.setPassword("pwd");
			em.persist(us);
			ForgotPasswordSession fps = new ForgotPasswordSession();
			fps.setUser(us);
			em.persist(fps);
			id = fps.getId();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		try {
			EntityManager em = getEntityManager();
			ForgotPasswordSession fps = em.find(ForgotPasswordSession.class, id);
			Assert.assertNotNull(fps);
		} catch (Exception e) {
			e.printStackTrace();
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
			us.setTitle("Mr.");
			us.setFirstName("Testfname");
			us.setUserName("testuname");
			us.setLastName("LastName");
			us.setEmailId("test@email.com");
			us.setPassword("pwd");
			em.persist(us);
			ForgotPasswordSession fps = new ForgotPasswordSession();
			fps.setUser(us);
			em.persist(fps);
			id = fps.getId();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		try {
			EntityManager em = getEntityManager();
			ForgotPasswordSession fps = em.find(ForgotPasswordSession.class, id);
			em.remove(fps);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		try {
			EntityManager em = getEntityManager();
			ForgotPasswordSession fps = em.find(ForgotPasswordSession.class, id);
			Assert.assertNull(fps);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
