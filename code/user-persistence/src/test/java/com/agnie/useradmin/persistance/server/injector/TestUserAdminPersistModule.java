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
package com.agnie.useradmin.persistance.server.injector;

import com.agnie.useradmin.persistance.server.dao.ApplicationManagerTest;
import com.agnie.useradmin.persistance.server.dao.AuthenticateMangerTest;
import com.agnie.useradmin.persistance.server.dao.ContextManagerTest;
import com.agnie.useradmin.persistance.server.dao.PermissioManagerTest;
import com.agnie.useradmin.persistance.server.dao.RoleManagerTest;
import com.agnie.useradmin.persistance.server.dao.UserManagerTest;
import com.agnie.useradmin.persistance.server.entity.AdminContextRoleTest;
import com.agnie.useradmin.persistance.server.entity.AdminRoleTest;
import com.agnie.useradmin.persistance.server.entity.ApplicationRoleTest;
import com.agnie.useradmin.persistance.server.entity.ApplicationTest;
import com.agnie.useradmin.persistance.server.entity.ContextRoleTest;
import com.agnie.useradmin.persistance.server.entity.ContextTest;
import com.agnie.useradmin.persistance.server.entity.EntityIntegerationTest;
import com.agnie.useradmin.persistance.server.entity.ForgotPasswordSessionTest;
import com.agnie.useradmin.persistance.server.entity.InitTest;
import com.agnie.useradmin.persistance.server.entity.PermissionTest;
import com.agnie.useradmin.persistance.server.entity.RoleTest;
import com.agnie.useradmin.persistance.server.entity.UserApplicationCtxRegistrationTest;
import com.agnie.useradmin.persistance.server.entity.UserApplicationRegistrationTest;
import com.agnie.useradmin.persistance.server.entity.UserTest;

public class TestUserAdminPersistModule extends UserAdminPersistModule {

	public TestUserAdminPersistModule() {
		super(true);
	}

	@Override
	protected void configure() {
		super.configure();
		bind(TestUserAdminPersistenceInitilizer.class).asEagerSingleton();
		expose(TestUserAdminPersistenceInitilizer.class);
		bind(PermissionTest.class);
		expose(PermissionTest.class);
		bind(RoleTest.class);
		expose(RoleTest.class);
		bind(UserTest.class);
		expose(UserTest.class);
		bind(ApplicationTest.class);
		expose(ApplicationTest.class);
		bind(InitTest.class);
		expose(InitTest.class);
		bind(EntityIntegerationTest.class);
		expose(EntityIntegerationTest.class);
		bind(ContextTest.class);
		expose(ContextTest.class);
		bind(ForgotPasswordSessionTest.class);
		expose(ForgotPasswordSessionTest.class);
		bind(UserApplicationRegistrationTest.class);
		expose(UserApplicationRegistrationTest.class);
		bind(AdminRoleTest.class);
		expose(AdminRoleTest.class);
		bind(AdminContextRoleTest.class);
		expose(AdminContextRoleTest.class);
		bind(ContextRoleTest.class);
		expose(ContextRoleTest.class);
		bind(UserManagerTest.class);
		expose(UserManagerTest.class);
		bind(ApplicationManagerTest.class);
		expose(ApplicationManagerTest.class);
		bind(ContextManagerTest.class);
		expose(ContextManagerTest.class);
		bind(UserApplicationCtxRegistrationTest.class);
		expose(UserApplicationCtxRegistrationTest.class);
		bind(ApplicationRoleTest.class);
		expose(ApplicationRoleTest.class);
		bind(AuthenticateMangerTest.class);
		expose(AuthenticateMangerTest.class);
		bind(PermissioManagerTest.class);
		expose(PermissioManagerTest.class);
		bind(RoleManagerTest.class);
		expose(RoleManagerTest.class);
	}
}
