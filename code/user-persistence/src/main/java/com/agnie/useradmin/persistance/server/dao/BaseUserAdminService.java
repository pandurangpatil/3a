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
package com.agnie.useradmin.persistance.server.dao;

import javax.persistence.EntityManager;

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.useradmin.persistance.server.entity.BaseEntity;
import com.agnie.useradmin.session.server.injector.SessionServletModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * 
 * 
 */
public abstract class BaseUserAdminService {

	@Inject
	Injector							injector;

	@Inject
	protected Provider<EntityManager>	uaem;

	@Inject
	@Named(SessionServletModule.CURRENT_USER)
	Provider<UserAccount>				curntUser;

	protected void inject(BaseEntity entity) {
		entity.setCurntUser(curntUser.get());
	}

}
