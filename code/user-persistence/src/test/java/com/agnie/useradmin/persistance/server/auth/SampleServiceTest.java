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
package com.agnie.useradmin.persistance.server.auth;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.server.auth.ACLContext;
import com.agnie.common.test.injector.GuiceTestRunner;
import com.agnie.common.test.injector.WithModules;
import com.agnie.common.test.providers.ACLCtxManager;
import com.agnie.common.test.providers.LoggedInUserManager;
import com.agnie.useradmin.persistance.server.entity.User;
import com.google.inject.Inject;
import com.google.inject.Injector;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestPermissionModule.class })
public class SampleServiceTest {

	public final static String	SAMPLE_USER	= "Sample-user";

	static final String			param		= "Pranoti";
	@Inject
	Injector					injector;

	@Inject
	ACLCtxManager				ctxMgr;

	@Inject
	LoggedInUserManager			logInUser;

	@Before
	public void setup() {
		AccessControlList acl = new AccessControlList();
		acl.addPermission("perm_yahoo_test");
		acl.addPermission("perm_google_test");
		ctxMgr.setContext(new ACLContext(acl));
		UserAccount us = new UserAccount();
		us.setId(SAMPLE_USER);
		logInUser.setCurrentUser(us);
	}

	@Test
	public void simpleTest() {
		SampleService ss = injector.getInstance(SampleService.class);
		Assert.assertEquals("Hello " + param, ss.testOne(param));
		Assert.assertEquals("Hello " + param, ss.testThree(param));
		User us = new User();
		us.setId(SAMPLE_USER);
	}

	@Test
	public void negativeTest() {
		try {
			SampleService ss = injector.getInstance(SampleService.class);
			User us = new User();
			us.setId(SAMPLE_USER + 1);
			ss.testTwo(us);
			Assert.assertTrue(false);
		} catch (Exception e) {
			Assert.assertTrue(true);
		}

	}
}
