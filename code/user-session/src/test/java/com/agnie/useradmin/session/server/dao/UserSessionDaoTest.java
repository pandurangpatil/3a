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
import java.util.Date;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.gwt.serverclient.client.enums.Language;
import com.agnie.common.injector.CommonModule;
import com.agnie.common.test.injector.GuiceTestRunner;
import com.agnie.common.test.injector.WithModules;
import com.agnie.useradmin.session.server.auth.TestPermissionModule;
import com.agnie.useradmin.session.server.injector.TestSessionModule;
import com.agnie.useradmin.session.server.injector.TestSessionMyBatisModule;
import com.agnie.useradmin.session.server.injector.TestSessionPersistModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

@RunWith(GuiceTestRunner.class)
@WithModules({ TestPermissionModule.class, TestSessionPersistModule.class, TestSessionModule.class, TestSessionMyBatisModule.class, CommonModule.class })
public class UserSessionDaoTest {

	@Inject
	private Injector				injector;
	@Inject
	private Provider<Connection>	connection;
	private final static String		EXISTING_USER_ID	= "2";

	@Before
	@After
	public void clear() {
		try {
			String deleteSessions = "DELETE FROM USER WHERE ID='" + EXISTING_USER_ID + "'";
			Connection con = connection.get();
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			stmt.execute(deleteSessions);
			Assert.assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	private String createSession() {
		try {
			String id = java.util.UUID.randomUUID().toString();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
			String date = formatter.format(new Date());
			Connection con = connection.get();
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			String insert = "INSERT INTO USERAUTHSESSION (ID, CREATETIMESTAMP, USERID, UPDATETIMESTAMP, VERSION) VALUES ( '" + id + "', '" + date + "', '" + EXISTING_USER_ID + "', '" + date
					+ "', 1 )";
			stmt.addBatch(insert);
			insert = "INSERT INTO  USER( ID, LASTNAME,  DEFAULTLANGUAGE, PASSWORD,  EMAILID,  TITLE,  USERNAME,  FIRSTNAME) VALUES ('" + EXISTING_USER_ID
					+ "', 'Patil', 'ENGLISH', 'password', 'root@agnie.co.in', 'Mr.', 'root.test', 'Pranoti')";
			stmt.addBatch(insert);
			stmt.executeBatch();

			Assert.assertTrue(true);
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		return null;
	}

	private void removeSession(String userId) {
		try {
			String deleteSessions = "DELETE FROM USERAUTHSESSION WHERE USERID='" + userId + "'";
			Connection con = connection.get();
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			stmt.execute(deleteSessions);
			Assert.assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void getAccountTest() {
		UserAccount expected = new UserAccount();
		expected.setFirstName("Pranoti");
		expected.setLastName("Patil");
		expected.setTitle("Mr.");
		expected.setUserName("root.test");
		expected.setLanguage(Language.ENGLISH);
		UserSessionDao usdao = injector.getInstance(UserSessionDao.class);
		UserAccount actual = usdao.getUserAccountBySessionId(createSession());
		Assert.assertEquals(expected, actual);
		removeSession(EXISTING_USER_ID);
	}

	@Test
	public void removeSessionTest() {
		UserSessionDao usdao = injector.getInstance(UserSessionDao.class);
		String sessionID = createSession();
		boolean resp = usdao.removeSession(sessionID);
		Assert.assertTrue(resp);
		UserAccount actual = usdao.getUserAccountBySessionId(sessionID);
		Assert.assertNull(actual);
	}
}
