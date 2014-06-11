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
import javax.persistence.FlushModeType;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.agnie.common.helper.SHA256;
import com.agnie.common.helper.TestURLInfoImpl;
import com.agnie.common.injector.CommonModule;
import com.agnie.common.test.injector.GuiceTestRunner;
import com.agnie.common.test.injector.WithModules;
import com.agnie.common.test.providers.StringManager;
import com.agnie.useradmin.persistance.client.enums.Language;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.persistance.client.service.dto.UserInfo;
import com.agnie.useradmin.persistance.server.dao.AuthenticateManager.FgtPwdSession;
import com.agnie.useradmin.persistance.server.entity.User;
import com.agnie.useradmin.persistance.server.helper.DBSetupHelper;
import com.agnie.useradmin.persistance.server.injector.TestSessionPersistModule;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminModule;
import com.agnie.useradmin.persistance.server.injector.TestUserAdminPersistModule;
import com.agnie.useradmin.session.server.mybatis.SessionMyBatisModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.persist.Transactional;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestUserAdminModule.class, TestUserAdminPersistModule.class, TestSessionPersistModule.class, SessionMyBatisModule.class, CommonModule.class })
public class AuthenticateMangerTest {
	private final static String			TEST_USER_NAME	= "test.user";
	@Inject
	private AuthenticateManager			am;
	@Inject
	protected Provider<EntityManager>	em;
	@Inject
	@Named(TestURLInfoImpl.TEST_URL)
	private StringManager				urlMgr;

	@Inject
	private DBSetupHelper				helper;

	@Before
	@Transactional
	public void setup() {
		clean();
		helper.createDbSetup();
	}

	@After
	@Transactional
	public void clean() {
		em.get().createQuery("DELETE FROM ForgotPasswordSession fps").executeUpdate();
		em.get().createQuery("DELETE FROM UserApplicationRegistration uar").executeUpdate();
		em.get().createQuery("DELETE FROM Application app").executeUpdate();
		em.get().createQuery("DELETE FROM User u").executeUpdate();
	}

	@Test
	public void signupTest() {
		urlMgr.setValue("http://localhost/userapp/signup");
		UserInfo ui = new UserInfo();
		ui.setUserName(TEST_USER_NAME);
		ui.setFirstName("fname");
		ui.setLastName("lname");
		ui.setTitle("Ms");
		ui.setCurrentLogingDomain(UserAdminURLGenerator.USERADMIN);
		ui.setEmailId("test@email.com");
		ui.setPassword(SHA256.getHashedString("password"));
		ui.setDefaultLanguage(Language.ENGLISH);

		try {
			User us = am.signupUser(ui);
			Assert.assertNotNull(us);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}

	@Test
	public void authenticateTest() {
		urlMgr.setValue("http://localhost/userapp/signup");
		em.get().setFlushMode(FlushModeType.AUTO);
		UserInfo ui = new UserInfo();
		ui.setUserName(TEST_USER_NAME);
		ui.setFirstName("fname");
		ui.setLastName("lname");
		ui.setTitle("Ms");
		ui.setCurrentLogingDomain(UserAdminURLGenerator.USERADMIN);
		ui.setEmailId("test@email.com");
		ui.setPassword(SHA256.getHashedString("password"));
		ui.setDefaultLanguage(Language.ENGLISH);

		try {
			User us = am.signupUser(ui);
			Assert.assertNotNull(us);
			am.verifyEmail(TEST_USER_NAME, us.getId(), us.getRuntTimeToken());
			String salt = java.util.UUID.randomUUID().toString();
			String sessionId = am.authenticate(TEST_USER_NAME, SHA256.getHashedString(SHA256.getHashedString("password") + salt), salt, false);
			Assert.assertNotNull(sessionId);

			FgtPwdSession token = am.forgotPassword(TEST_USER_NAME, "Http://testurl.com/");
			Assert.assertNotNull(token);
			boolean resp = am.changePassword(SHA256.getHashedString("newpassword"), TEST_USER_NAME, token.getId(), token.getRunTimeToken());
			Assert.assertTrue(resp);
			sessionId = am.authenticate(TEST_USER_NAME, SHA256.getHashedString(SHA256.getHashedString("newpassword") + salt), salt, false);
			Assert.assertNotNull(sessionId);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}

	public String signupUser(UserInfo ui) {
		urlMgr.setValue("http://localhost/userapp/signup");
		try {
			User us = am.signupUser(ui);
			Assert.assertNotNull(us);
			am.verifyEmail(TEST_USER_NAME, us.getId(), us.getRuntTimeToken());
			String salt = java.util.UUID.randomUUID().toString();
			String sessionId = am.authenticate(TEST_USER_NAME, SHA256.getHashedString(ui.getPassword() + salt), salt, false);
			Assert.assertNotNull(sessionId);
			return us.getId();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		return null;
	}

}
