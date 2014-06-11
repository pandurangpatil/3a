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
public class AdminContextRoleTest extends BaseUserAdminTest {

	@Before
	@After
	@Transactional
	public void clean() throws Exception {
		try {
			em.get().createQuery("DELETE FROM AdminContextRole", AdminContextRole.class).executeUpdate();
			List<UserApplicationRegistration> aurs = em.get().createQuery("SELECT aur FROM UserApplicationRegistration aur", UserApplicationRegistration.class).getResultList();
			for (UserApplicationRegistration userRegs : aurs) {
				em.get().remove(userRegs);
			}
			em.get().createQuery("DELETE FROM Role", Role.class).executeUpdate();
			em.get().createQuery("DELETE FROM Permission", Permission.class).executeUpdate();
			em.get().createQuery("DELETE FROM Context", Context.class).executeUpdate();
			List<Application> apps = em.get().createQuery("SELECT app FROM Application app", Application.class).getResultList();
			for (Application application : apps) {
				em.get().remove(application);
			}
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

			Permission perm = new Permission();
			perm.setApplication(app);
			perm.setName("Test PErmission");
			perm.setCode("test code");
			em.persist(perm);

			Role role = new Role();
			role.setApplication(app);
			role.setName("test role");
			List<Permission> perms = new ArrayList<Permission>();
			perms.add(perm);
			role.setPermissions(perms);
			em.persist(role);

			Context ctx = new Context();
			ctx.setName("TestContext");
			ctx.setApplication(app);
			ctx.setDescription("test description");
			em.persist(ctx);

			User us1 = new User();
			us1.setTitle("MR.");
			us1.setLastName("LastName");
			us1.setFirstName("client");
			us1.setEmailId("test@email.com");
			us1.setPassword("pwd");
			us1.setUserName("testusername");
			em.persist(us1);

			UserApplicationRegistration apr = new UserApplicationRegistration();
			apr.setStatus(RequestStatus.ACTIVE);
			apr.setApp(app);
			apr.setUser(us1);
			em.persist(apr);

			AdminRole ar = new AdminRole();
			List<Role> roles = new ArrayList<Role>();
			roles.add(role);
			ar.setRoles(roles);
			em.persist(ar);

			apr.setAdminRole(ar);
			em.persist(apr);

			AdminContextRole acr = new AdminContextRole();
			acr.setRoles(roles);
			em.persist(acr);
			id = acr.getId();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			EntityManager em = getEntityManager();
			AdminContextRole acr = em.find(AdminContextRole.class, id);
			Assert.assertNotNull(acr);
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

			Permission perm = new Permission();
			perm.setApplication(app);
			perm.setName("Test PErmission");
			perm.setCode("test code");
			em.persist(perm);

			Role role = new Role();
			role.setApplication(app);
			role.setName("test role");
			List<Permission> perms = new ArrayList<Permission>();
			perms.add(perm);
			role.setPermissions(perms);
			em.persist(role);

			Context ctx = new Context();
			ctx.setName("TestContext");
			ctx.setApplication(app);
			ctx.setDescription("test description");
			em.persist(ctx);

			User us1 = new User();
			us1.setTitle("MR.");
			us1.setLastName("LastName");
			us1.setFirstName("client");
			us1.setEmailId("test@email.com");
			us1.setPassword("pwd");
			us1.setUserName("testusername");
			em.persist(us1);

			UserApplicationRegistration apr = new UserApplicationRegistration();
			apr.setStatus(RequestStatus.ACTIVE);
			apr.setApp(app);
			apr.setUser(us1);
			em.persist(apr);

			AdminRole ar = new AdminRole();
			List<Role> roles = new ArrayList<Role>();
			roles.add(role);
			ar.setRoles(roles);
			em.persist(ar);

			apr.setAdminRole(ar);
			em.persist(apr);

			AdminContextRole acr = new AdminContextRole();
			acr.setRoles(roles);
			em.persist(acr);
			id = acr.getId();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			EntityManager em = getEntityManager();
			AdminContextRole acr = em.find(AdminContextRole.class, id);
			Assert.assertNotNull(acr);
			em.remove(acr);
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail();
		}
		try {
			EntityManager em = getEntityManager();
			AdminContextRole acr = em.find(AdminContextRole.class, id);
			Assert.assertNull(acr);
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail();
		}
	}
}
