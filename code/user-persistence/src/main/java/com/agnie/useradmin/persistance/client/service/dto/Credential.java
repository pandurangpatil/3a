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

/**
 * to pass on credential information from client to server
 * 
 * 
 */
public class Credential implements Serializable {
	/**
     * 
     */
	private static final long	serialVersionUID	= 1L;
	private String				username;
	private String				password;
	private String				domain;
	private String				sourceURL;

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
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain
	 *            the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the sourceURL
	 */
	public String getSourceURL() {
		return sourceURL;
	}

	/**
	 * @param sourceURL
	 *            the sourceURL to set
	 */
	public void setSourceURL(String sourceURL) {
		this.sourceURL = sourceURL;
	}

}
