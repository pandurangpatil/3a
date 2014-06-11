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
import com.agnie.useradmin.persistance.server.BaseUserAdminTest;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminModule;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminPersistModule;
import com.google.inject.persist.Transactional;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestUserAdminModule.class, TestUserAdminPersistModule.class, CommonModule.class })
public class RoleTest extends BaseUserAdminTest {

	private static final String	ROLE_ID1	= "Sample-Role-ID1";
	private static final String	ROLE_ID2	= "Sample-Role-ID2";
	private static final String	ROLE_ID3	= "Sample-Role-ID3";
	private static final String	PERM_ID1	= "sample-permission-id1";
	private static final String	PERM_ID2	= "sample-permission-id2";

	@Before
	@After
	@Transactional
	public void clean() throws Exception {
		try {
			List<String> ids = new ArrayList<String>();
			ids.add(ROLE_ID1);
			ids.add(ROLE_ID2);
			ids.add(ROLE_ID3);
			em.get().createNamedQuery("Role.delete").setParameter("ids", ids).executeUpdate();
			ids = new ArrayList<String>();
			ids.add(PERM_ID1);
			ids.add(PERM_ID2);
			em.get().createNamedQuery("Permission.delete").setParameter("ids", ids).executeUpdate();
			em.get().createNativeQuery("DELETE FROM ROLE_PERMISSION WHERE Role_ID ='" + ROLE_ID3 + "'").executeUpdate();
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
			Role rl = new Role();
			rl.setId(ROLE_ID1);
			rl.setName("Name");
			em.persist(rl);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		try {
			EntityManager em = getEntityManager();
			Role rl = em.find(Role.class, ROLE_ID1);
			Assert.assertNotNull(rl);
			Assert.assertEquals("Name", rl.getName());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	@Transactional
	public void createWithPermission() {
		try {
			EntityManager em = getEntityManager();
			Role rl = new Role();
			rl.setId(ROLE_ID3);
			rl.setName("Name3");
			Permission pm = new Permission();
			pm.setId(PERM_ID1);
			pm.setName("Name");
			pm.setCode("code");
			List<Permission> pms = new ArrayList<Permission>();
			em.persist(pm);
			pms.add(pm);
			pm = new Permission();
			pm.setId(PERM_ID2);
			pm.setName("Name");
			pm.setCode("code");
			em.persist(pm);
			pms.add(pm);
			rl.setPermissions(pms);
			em.persist(rl);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			EntityManager em = getEntityManager();
			Role rl = em.find(Role.class, ROLE_ID3);
			Assert.assertNotNull(rl);
			Assert.assertEquals("Name3", rl.getName());
			Assert.assertNotNull(rl.getPermissions());
			Assert.assertEquals(2, rl.getPermissions().size());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void remove() {
		try {
			EntityManager em = getEntityManager();
			Role rl = new Role();
			rl.setId(ROLE_ID2);
			rl.setName("Name1");
			em.persist(rl);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		try {
			EntityManager em = getEntityManager();
			Role rl = em.find(Role.class, ROLE_ID2);
			em.remove(rl);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		try {
			EntityManager em = getEntityManager();
			Role rl = em.find(Role.class, ROLE_ID2);
			Assert.assertNull(rl);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
