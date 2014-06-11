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
package com.agnie.useradmin.persistance.client.service.dto;

import java.io.Serializable;

/**
 * 
 */
public class ForgotPasswordChallenge implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private String				customQuestion;
	private String				sessionid;

	public ForgotPasswordChallenge() {
	}

	/**
	 * @param question
	 * @param customQuestion
	 * @param sessionid
	 */
	public ForgotPasswordChallenge(String customQuestion, String sessionid) {
		super();
		this.customQuestion = customQuestion;
		this.sessionid = sessionid;
	}

	/**
	 * @return the customQuestion
	 */
	public String getCustomQuestion() {
		return customQuestion;
	}

	/**
	 * @param customQuestion
	 *            the customQuestion to set
	 */
	public void setCustomQuestion(String customQuestion) {
		this.customQuestion = customQuestion;
	}

	/**
	 * @return the sessionid
	 */
	public String getSessionid() {
		return sessionid;
	}

	/**
	 * @param sessionid
	 *            the sessionid to set
	 */
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

}
