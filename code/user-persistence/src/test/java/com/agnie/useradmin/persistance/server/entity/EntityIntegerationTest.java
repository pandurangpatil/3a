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

@RunWith(GuiceTestRunner.class)
@WithModules({ TestUserAdminModule.class, TestUserAdminPersistModule.class, CommonModule.class })
public class EntityIntegerationTest extends BaseUserAdminTest {

	/**
	 * TODO: Need to check
	 * 
	 * @throws Exception
	 */
	@Test
	public void dummy() {

	}

	public void signup() throws Exception {
		EntityManager em = getEntityManager();
		User us = new User();
		us.setTitle("Mrs.");
		us.setFirstName("Pranoti");
		us.setLastName("Patil");
		us.setEmailId("itspranoti@gmail.com");
		us.setUserName("pranoti");
		us.setPassword(SHA256.getHashedString("password"));
		us.setStatus(UserStatus.ACTIVE);
		em.persist(us);

		Application dm = new Application();
		dm.setBusinessName("XYz Buissness");
		dm.setDomain("first-domain");
		dm.setURL("Http://xyz.com/index.html");
		dm.setContactEmail("contact@email.com");
		em.persist(dm);

		List<Permission> pms = new ArrayList<Permission>();
		Permission pm = new Permission();
		pm.setName("perm1");
		pm.setCode("perm1.code");
		pm.setApplication(dm);
		em.persist(pm);
		pms.add(pm);

		pm = new Permission();
		pm.setName("perm2");
		pm.setCode("perm2.code");
		pm.setApplication(dm);
		em.persist(pm);
		pms.add(pm);
		dm.setPermissions(pms);

		List<Role> rls = new ArrayList<Role>();
		Role rl = new Role();
		rl.setApplication(dm);
		rl.setName("role1");
		rl.setPermissions(pms);
		em.persist(rl);
		rls.add(rl);
		dm.setRoles(rls);

		User usnew = new User();
		usnew.setTitle("Mr.");
		usnew.setFirstName("new");
		usnew.setLastName("user");
		usnew.setEmailId("new@gmail.com");
		usnew.setUserName("username-new");
		usnew.setPassword(SHA256.getHashedString("password"));
		usnew.setStatus(UserStatus.ACTIVE);
		em.persist(usnew);
	}
}
