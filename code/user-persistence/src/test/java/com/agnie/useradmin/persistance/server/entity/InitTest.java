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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.agnie.common.injector.CommonModule;
import com.agnie.common.test.injector.GuiceTestRunner;
import com.agnie.common.test.injector.WithModules;
import com.agnie.useradmin.persistance.server.BaseUserAdminTest;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminModule;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminPersistModule;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestUserAdminModule.class, TestUserAdminPersistModule.class, CommonModule.class })
public class InitTest extends BaseUserAdminTest {

	@SuppressWarnings("unused")
	@Test
	public void testInit() {
		EntityManager em = getEntityManager();
	}
}
