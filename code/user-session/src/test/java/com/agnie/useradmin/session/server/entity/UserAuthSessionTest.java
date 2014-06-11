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
package com.agnie.useradmin.session.server.entity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.agnie.common.injector.CommonModule;
import com.agnie.common.test.injector.GuiceTestRunner;
import com.agnie.common.test.injector.WithModules;
import com.agnie.useradmin.session.BaseSessionTest;
import com.agnie.useradmin.session.server.auth.TestPermissionModule;
import com.agnie.useradmin.session.server.injector.TestSessionModule;
import com.agnie.useradmin.session.server.injector.TestSessionPersistModule;
import com.agnie.useradmin.session.server.mybatis.SessionMyBatisModule;
import com.google.inject.persist.Transactional;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestPermissionModule.class, TestSessionModule.class, TestSessionPersistModule.class, SessionMyBatisModule.class, CommonModule.class })
public class UserAuthSessionTest extends BaseSessionTest {
	@Before
	public void before() {
		super.before();
		removeUserSessions("sample-user-id");
	}

	@Test
	@Transactional
	public void create() {
		try {
			UserAuthSession uas = new UserAuthSession();
			uas.setUserId("sample-user-id");
			getEntityManager().persist(uas);
			Assert.assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	@Transactional
	public void find() {
		try {
			UserAuthSession uas = new UserAuthSession();
			uas.setId(java.util.UUID.randomUUID().toString());
			uas.setUserId("sample-user-id");
			getEntityManager().persist(uas);
			UserAuthSession uas1 = getEntityManager().find(UserAuthSession.class, uas.getId());
			Assert.assertEquals(uas, uas1);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
