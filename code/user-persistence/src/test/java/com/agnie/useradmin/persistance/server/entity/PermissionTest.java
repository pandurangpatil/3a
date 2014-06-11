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
public class PermissionTest extends BaseUserAdminTest {
	private static final String	PERM1_ID	= "sample-permission-test-id1";

	private static final String	PERM2_ID	= "sample-permission-test-id2";

	@Before
	@After
	@Transactional
	public void clean() throws Exception {
		try {
			List<String> ids = new ArrayList<String>();
			ids.add(PERM1_ID);
			ids.add(PERM2_ID);
			em.get().createNamedQuery("Permission.delete").setParameter("ids", ids).executeUpdate();
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
			Permission pm = new Permission();
			pm.setId(PERM1_ID);
			pm.setName("Name");
			pm.setCode("code");
			em.persist(pm);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		try {
			EntityManager em = getEntityManager();
			Permission pm = em.find(Permission.class, PERM1_ID);
			Assert.assertNotNull(pm);
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
			Permission pm = new Permission();
			pm.setId(PERM2_ID);
			pm.setName("Name1");
			pm.setCode("code1");
			em.persist(pm);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		try {
			EntityManager em = getEntityManager();
			Permission pm = em.find(Permission.class, PERM2_ID);
			em.remove(pm);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		try {
			EntityManager em = getEntityManager();
			Permission pm = em.find(Permission.class, PERM2_ID);
			Assert.assertNull(pm);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
