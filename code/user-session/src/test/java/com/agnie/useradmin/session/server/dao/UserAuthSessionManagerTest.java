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
package com.agnie.useradmin.session.server.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.injector.CommonModule;
import com.agnie.common.test.injector.GuiceTestRunner;
import com.agnie.common.test.injector.WithModules;
import com.agnie.useradmin.session.BaseSessionTest;
import com.agnie.useradmin.session.server.auth.TestPermissionModule;
import com.agnie.useradmin.session.server.entity.UserAuthSession;
import com.agnie.useradmin.session.server.injector.SessionCommonModule;
import com.agnie.useradmin.session.server.injector.TestSessionModule;
import com.agnie.useradmin.session.server.injector.TestSessionMyBatisModule;
import com.agnie.useradmin.session.server.injector.TestSessionPersistModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestSessionPersistModule.class, TestSessionModule.class, TestSessionMyBatisModule.class, TestPermissionModule.class, CommonModule.class })
public class UserAuthSessionManagerTest extends BaseSessionTest {

	@Inject
	private Provider<Connection>	connection;
	@Inject
	@Named(SessionCommonModule.DEFAULT_EXP_TIME)
	private int						defaultExpTime;

	@Before
	public void before() {
		super.before();
		removeUserSessions(SAMPLE_USER_ID);
	}

	@Test
	public void createSessionTest() {
		UserAuthSessionManager uasm = injector.getInstance(UserAuthSessionManager.class);
		String sessionID = uasm.createSession(SAMPLE_USER_ID, false);
		UserAuthSession actual = uasm.getUserAuthBySessionId(sessionID);
		Assert.assertEquals(SAMPLE_USER_ID, actual.getUserId());
	}

	@Test
	public void getUserAuthBySessionIdTest() {
		UserAuthSession uas = new UserAuthSession();
		uas.setId(java.util.UUID.randomUUID().toString());
		uas.setUserId(SAMPLE_USER_ID);
		getEntityManager().persist(uas);
		UserAuthSession actual = injector.getInstance(UserAuthSessionManager.class).getUserAuthBySessionId(uas.getId());
		Assert.assertEquals(uas, actual);
	}

	@Test
	public void getExpiredSessionsTest() {
		try {
			String id1 = java.util.UUID.randomUUID().toString();
			String id2 = java.util.UUID.randomUUID().toString();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -3);
			String date = formatter.format(cal.getTime());
			Connection con = connection.get();
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			stmt.addBatch("INSERT INTO USERAUTHSESSION (ID, CREATETIMESTAMP, USERID, UPDATETIMESTAMP, EXPIRYTIMESTAMP, VERSION) VALUES ( '" + id1 + "', '" + date + "', '" + SAMPLE_USER_ID + "', '"
					+ date + "', '" + date + "', 1 )");
			stmt.addBatch("INSERT INTO USERAUTHSESSION (ID, CREATETIMESTAMP, USERID, UPDATETIMESTAMP, EXPIRYTIMESTAMP, VERSION) VALUES ( '" + id2 + "', '" + date + "', '" + SAMPLE_USER_ID + "', '"
					+ date + "', '" + date + "', 1 )");
			stmt.executeBatch();
			Assert.assertTrue(true);
			UserAuthSessionManager uasm = injector.getInstance(UserAuthSessionManager.class);
			List<UserAuthSession> expList = uasm.getExpiredUserSessions();
			UserAuthSession uas1 = new UserAuthSession();
			uas1.setId(id1);
			Assert.assertTrue(expList.contains(uas1));
			UserAuthSession uas2 = new UserAuthSession();
			uas2.setId(id2);
			Assert.assertTrue(expList.contains(uas2));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void getSessionsByUserTest() {
		String sessionId1 = createSession();
		String sessionId2 = createSession();
		UserAuthSessionManager uasm = injector.getInstance(UserAuthSessionManager.class);
		List<UserAuthSession> usrSessions = uasm.getUserSessionsByUser(SAMPLE_USER_ID);
		UserAuthSession uas1 = new UserAuthSession();
		uas1.setId(sessionId1);
		Assert.assertTrue(usrSessions.contains(uas1));
		UserAuthSession uas2 = new UserAuthSession();
		uas2.setId(sessionId2);
		Assert.assertTrue(usrSessions.contains(uas2));
	}

	@Test
	public void updateSessionTest() {
		String sessionId = createSession();
		UserAuthSessionManager uasm = injector.getInstance(UserAuthSessionManager.class);
		UserAuthSession uas = uasm.getUserAuthBySessionId(sessionId);
		uasm.updateSession(uas, new UserAccount());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, defaultExpTime - 120);
		Assert.assertTrue(cal.getTime().before(uas.getExpiryTimeStamp()));
	}
}
