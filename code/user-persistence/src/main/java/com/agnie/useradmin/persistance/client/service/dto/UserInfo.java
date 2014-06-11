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
package com.agnie.useradmin.persistance.client.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.agnie.useradmin.persistance.client.enums.Language;
import com.sfeir.captcha.shared.CaptchaResult;

/**
 * to pass logged in user's information from server to client
 * 
 */
public class UserInfo implements Serializable {

	private static final long	serialVersionUID	= 2L;
	@Size(max = 10, message = "maxlength10")
	private String				title;

	@Size(max = 20, message = "maxlength20")
	private String				firstName;

	@Size(max = 20, message = "maxlength20")
	private String				lastName;

	@NotNull
	@Size(max = 30, message = "maxlength30")
	// TODO: because of below regex build is failing as Thoughtworks library is giving some problem.
	// @Pattern(regexp = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message
	// = "Invalid Email Id")
	private String				emailId;

	@NotNull
	@Size(min = 4, max = 20, message = "min4max20")
	private String				password;

	@NotNull
	@Size(min = 4, max = 20, message = "min4max20")
	private String				userName;

	private String				sessionId;
	private CaptchaResult		captchaAns;
	private String				currentLogingDomain;
	private Language			defaultLanguage		= Language.ENGLISH;
	private AccessControlList	acl;

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId
	 *            the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the currentLogingDomain
	 */
	public String getCurrentLogingDomain() {
		return currentLogingDomain;
	}

	/**
	 * @param currentLogingDomain
	 *            the currentLogingDomain to set
	 */
	public void setCurrentLogingDomain(String currentLogingDomain) {
		this.currentLogingDomain = currentLogingDomain;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the defaultLanguage
	 */
	public Language getDefaultLanguage() {
		return defaultLanguage;
	}

	/**
	 * @param defaultLanguage
	 *            the defaultLanguage to set
	 */
	public void setDefaultLanguage(Language defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	/**
	 * @param acl
	 *            the acl to set
	 */
	public void setAcl(AccessControlList acl) {
		this.acl = acl;
	}

	/**
	 * @return the acl
	 */
	public AccessControlList getAcl() {
		return acl;
	}

	/**
	 * @return the captchaAns
	 */
	public CaptchaResult getCaptchaAns() {
		return captchaAns;
	}

	/**
	 * @param captchaAns
	 *            the captchaAns to set
	 */
	public void setCaptchaAns(CaptchaResult captchaAns) {
		this.captchaAns = captchaAns;
	}

}
