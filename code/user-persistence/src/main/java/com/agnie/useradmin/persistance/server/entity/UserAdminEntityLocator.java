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

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.Locator;

/**
 * Generic Entity @Locator for objects that extend BaseEntity from UserAdmin persistent unit.
 */
@Singleton
public class UserAdminEntityLocator extends Locator<BaseEntity, String> {
	private static org.apache.log4j.Logger	logger	= Logger.getLogger(UserAdminEntityLocator.class);
	@Inject
	Injector								injector;
	@Inject
	Provider<EntityManager>					entityManager;

	@Override
	public BaseEntity create(Class<? extends BaseEntity> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			logger.error(e);
		} catch (IllegalAccessException e) {
			logger.error(e);
		}
		return null;
	}

	@Override
	public BaseEntity find(Class<? extends BaseEntity> clazz, String id) {
		return entityManager.get().find(clazz, id);
	}

	/**
	 * it's never called
	 */
	@Override
	public Class<BaseEntity> getDomainType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getId(BaseEntity domainObject) {
		return domainObject.getId();
	}

	@Override
	public Class<String> getIdType() {
		return String.class;
	}

	@Override
	public Object getVersion(BaseEntity domainObject) {
		return domainObject.getVersion();
	}

}
