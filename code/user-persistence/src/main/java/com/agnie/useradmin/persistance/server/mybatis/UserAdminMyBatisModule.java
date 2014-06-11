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
package com.agnie.useradmin.persistance.server.mybatis;

import com.agnie.useradmin.persistance.server.mybatis.mapper.ApplicationMapper;
import com.agnie.useradmin.persistance.server.mybatis.mapper.UserPermissionsMapper;
import com.agnie.useradmin.session.server.mybatis.SessionMyBatisModule;

public class UserAdminMyBatisModule extends SessionMyBatisModule {
	@Override
	protected void initialize() {
		super.initialize();
		addMapperClass(UserPermissionsMapper.class);
		addMapperClass(ApplicationMapper.class);
	}
}
