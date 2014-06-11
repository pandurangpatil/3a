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
/**
 * 
 */
package com.agnie.useradmin.persistance.server.helper;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import com.agnie.useradmin.persistance.client.enums.GeneralStatus;
import com.agnie.useradmin.persistance.client.enums.UserStatus;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.persistance.server.entity.Application;
import com.agnie.useradmin.persistance.server.entity.User;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

/**
 * @author Pandurang Patil 25-Feb-2014
 * 
 */
@Singleton
public class DBSetupHelper {

	@Inject
	protected Provider<EntityManager>	em;

	@Transactional
	public void createDbSetup() {
		try {

			User us = new User();
			us.setId("1");
			us.setTitle("MR.");
			us.setLastName("Patil");
			us.setEmailId("'root@pandurangpatil.com'");
			us.setFirstName("Pandurang");
			us.setUserName("root");
			us.setPassword("password");
			us.setStatus(UserStatus.ACTIVE);
			em.get().persist(us);

			Application application = new Application();
			application.setId("1");
			application.setBusinessName("3a4users");
			application.setDomain(UserAdminURLGenerator.USERADMIN);
			application.setStatus(GeneralStatus.ACTIVE);
			application.setURL("/landing.jsp");
			application.setIconURL("/userapp/images/logo.png");
			application.setContactEmail("contact@email.com");
			em.get().persist(application);

			Assert.assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
