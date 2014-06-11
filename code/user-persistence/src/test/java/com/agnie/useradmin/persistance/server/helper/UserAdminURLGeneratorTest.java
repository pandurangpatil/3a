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
package com.agnie.useradmin.persistance.server.helper;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

import junit.framework.Assert;

import org.junit.Test;

import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLInfo;
import com.agnie.useradmin.persistance.server.util.TestUserAdminURLInfo;

public class UserAdminURLGeneratorTest {

	@Test
	public void afterLoginRedirectUrlTest() throws MalformedURLException, UnsupportedEncodingException {
		String url = "http://localhost:8080/login.jsp?gwt.codesvr=127.0.0.1:9997&source=" + URLEncoder.encode("http://localhost:8080/housingsociety.html", "UTF-8");
		UserAdminURLInfo ui = new TestUserAdminURLInfo(url, "session1");
		String expectedUrl = "http://localhost:8080/housingsociety.html?sessionid=session1";
		UserAdminURLGenerator uaurg = new UserAdminURLGenerator();
		String changedUrl = uaurg.getAfterLoginRedirectUrl(ui, null, null);
		// System.out.println(changedUrl);
		Assert.assertEquals(expectedUrl, changedUrl);

		url = "http://localhost:8080/login.jsp?gwt.codesvr=127.0.0.1:9997";
		ui = new TestUserAdminURLInfo(url, "session1");
		expectedUrl = "http://localhost:8080/" + UserAdminURLGenerator.LANDING_PAGE + "?gwt.codesvr=127.0.0.1:9997&sessionid=session1";
		changedUrl = uaurg.getAfterLoginRedirectUrl(ui, null, null);
		// System.out.println(changedUrl);
		Assert.assertEquals(expectedUrl, changedUrl);

		url = "http://localhost:8080/login.jsp?gwt.codesvr=127.0.0.1:9997&locale=mr";
		ui = new TestUserAdminURLInfo(url, "session1");
		expectedUrl = "http://localhost:8080/" + UserAdminURLGenerator.LANDING_PAGE + "?gwt.codesvr=127.0.0.1:9997&locale=mr&sessionid=session1";
		changedUrl = uaurg.getAfterLoginRedirectUrl(ui, null, null);
		// System.out.println(changedUrl);
		Assert.assertEquals(expectedUrl, changedUrl);

		url = "http://localhost:8080/login.jsp?gwt.codesvr=127.0.0.1:9997&source=" + URLEncoder.encode("http://localhost:8080/housingsociety.html?gwt.codesvr=127.0.0.1:9997&locale=mr", "UTF-8");
		ui = new TestUserAdminURLInfo(url, "session1");
		expectedUrl = "http://localhost:8080/housingsociety.html?gwt.codesvr=127.0.0.1:9997&sessionid=session1";
		changedUrl = uaurg.getAfterLoginRedirectUrl(ui, null, null);
		// System.out.println(changedUrl);
		Assert.assertEquals(expectedUrl, changedUrl);

		url = "http://localhost:8080/login.jsp?gwt.codesvr=127.0.0.1:9997&locale=mr&source="
				+ URLEncoder.encode("http://localhost:8080/housingsociety.html?gwt.codesvr=127.0.0.1:9997&locale=mr", "UTF-8");
		ui = new TestUserAdminURLInfo(url, "session1");
		expectedUrl = "http://localhost:8080/housingsociety.html?gwt.codesvr=127.0.0.1:9997&locale=mr&sessionid=session1";
		changedUrl = uaurg.getAfterLoginRedirectUrl(ui, null, null);
		// System.out.println(changedUrl);
		Assert.assertEquals(expectedUrl, changedUrl);

		url = "http://localhost:8080/login.jsp?gwt.codesvr=127.0.0.1:9997&locale=mr";
		ui = new TestUserAdminURLInfo(url, "session1");
		expectedUrl = "http://localhost:8080/housingsociety.html?locale=mr&sessionid=session1";
		changedUrl = uaurg.getAfterLoginRedirectUrl(ui, "housing.co.in", "http://localhost:8080/housingsociety.html");
		// System.out.println(changedUrl);
		Assert.assertEquals(expectedUrl, changedUrl);

		url = "http://localhost:8080/login.jsp?gwt.codesvr=127.0.0.1:9997&locale=mr";
		ui = new TestUserAdminURLInfo(url, "session1");
		expectedUrl = "http://localhost:8080/useradmin.jsp?gwt.codesvr=127.0.0.1:9997&locale=mr&sessionid=session1";
		changedUrl = uaurg.getAfterLoginRedirectUrl(ui, null, "http://localhost:8080/useradmin.jsp");
		// System.out.println(changedUrl);
		Assert.assertEquals(expectedUrl, changedUrl);

		url = "http://localhost:8080/login.jsp?gwt.codesvr=127.0.0.1:9997&locale=mr";
		ui = new TestUserAdminURLInfo(url, "session1");
		try {
			changedUrl = uaurg.getAfterLoginRedirectUrl(ui, "housing.co.in", null);
			Assert.assertTrue(false);
		} catch (IllegalArgumentException ex) {
			Assert.assertTrue(true);
		}
		String referrer = "http://localhost:8080/housingsociety.html";
		url = "http://localhost:8080/login.jsp?gwt.codesvr=127.0.0.1:9997&source=" + URLEncoder.encode("http://localhost:8080/billing.html", "UTF-8") + "&Referer="
				+ URLEncoder.encode(referrer, "UTF-8");
		ui = new TestUserAdminURLInfo(url, "session1");
		expectedUrl = "http://localhost:8080/billing.html?sessionid=session1&source=" + URLEncoder.encode(referrer, "UTF-8");
		changedUrl = uaurg.getAfterLoginRedirectUrl(ui, null, null);
		// System.out.println(changedUrl);
		Assert.assertEquals(expectedUrl, changedUrl);
	}

	@Test
	public void appManageUrlTest() throws MalformedURLException {
		String url = "http://localhost:8080/userapp/landing.jsp#LANDING";
		UserAdminURLInfo ui = new TestUserAdminURLInfo(url, "session1");
		String expectedUrl = "http://localhost:8080/userapp/useradmin.jsp?sel-domain=3a.agnie.net&sessionid=session1";
		UserAdminURLGenerator uaurg = new UserAdminURLGenerator();
		String changedUrl = uaurg.getAppManageUrl("3a.agnie.net", ui);
		// System.out.println(changedUrl);
		Assert.assertEquals(expectedUrl, changedUrl);

		url = "http://localhost:8080/landing.jsp?gwt.codesvr=127.0.0.1:9997#LANDING";
		ui = new TestUserAdminURLInfo(url, "session1");
		expectedUrl = "http://localhost:8080/useradmin.jsp?sel-domain=3a.agnie.net&gwt.codesvr=127.0.0.1:9997&sessionid=session1";
		changedUrl = uaurg.getAppManageUrl("3a.agnie.net", ui);
		// System.out.println(changedUrl);
		Assert.assertEquals(expectedUrl, changedUrl);
	}

	@Test
	public void applicationUrlTest() throws MalformedURLException {
		String url = "http://housingsociety.co.in/landing.jsp?state=something#LANDING";
		UserAdminURLInfo ui = new TestUserAdminURLInfo(url, "session1");
		String expectedUrl = "http://housingsociety.co.in/landing.jsp?state=something&sessionid=session1#LANDING";
		UserAdminURLGenerator uaurg = new UserAdminURLGenerator();
		String changedUrl = uaurg.getApplicationUrl(url, ui);
		// System.out.println(changedUrl);
		Assert.assertEquals(expectedUrl, changedUrl);

		url = "http://housingsociety.co.in/landing.jsp#LANDING";
		ui = new TestUserAdminURLInfo(url, "session1");
		expectedUrl = "http://housingsociety.co.in/landing.jsp?sessionid=session1#LANDING";
		changedUrl = uaurg.getApplicationUrl(url, ui);
		// System.out.println(changedUrl);
		Assert.assertEquals(expectedUrl, changedUrl);
		
		url = "http://housingsociety.co.in/landing.jsp";
		ui = new TestUserAdminURLInfo(url, "session1");
		expectedUrl = "http://housingsociety.co.in/landing.jsp?sessionid=session1";
		changedUrl = uaurg.getApplicationUrl(url, ui);
		// System.out.println(changedUrl);
		Assert.assertEquals(expectedUrl, changedUrl);
	}
}
