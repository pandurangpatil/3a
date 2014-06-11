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

public class Application implements Serializable {
	private static final long	serialVersionUID	= 1L;
	private String				domain;
	private String				URL;
	private String				iconURL;

	public Application() {
	}

	/**
	 * @param domain
	 * @param uRL
	 * @param iconURL
	 */
	public Application(String domain, String uRL, String iconURL) {
		this.domain = domain;
		URL = uRL;
		this.iconURL = iconURL;
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
	 * @return the uRL
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * @param uRL
	 *            the uRL to set
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * @return the iconURL
	 */
	public String getIconURL() {
		return iconURL;
	}

	/**
	 * @param iconURL
	 *            the iconURL to set
	 */
	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}

}
