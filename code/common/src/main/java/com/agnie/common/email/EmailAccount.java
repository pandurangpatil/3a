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
package com.agnie.common.email;

public class EmailAccount {

	// username to login to email account
	private String	username;
	// password to login to email account
	private String	password;
	// what from email address that need to be used while sending the email for
	// given account
	private String	from;
	// From Name that should be used while sending the email
	private String	fromName;

	/**
	 * @return the username
	 */
	public String getUsername() {

		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {

		this.username = username;
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
	 * @return the from
	 */
	public String getFrom() {

		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(String from) {

		this.from = from;
	}

	/**
	 * @return the fromName
	 */
	public String getFromName() {

		return fromName;
	}

	/**
	 * @param fromName
	 *            the fromName to set
	 */
	public void setFromName(String fromName) {

		this.fromName = fromName;
	}

}
