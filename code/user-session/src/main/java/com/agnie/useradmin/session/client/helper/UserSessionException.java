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
package com.agnie.useradmin.session.client.helper;

public class UserSessionException extends Exception {

	private static final long	serialVersionUID	= 1L;
	private String				code;

	public UserSessionException(String code) {
		super();
		this.code = code;
	}

	public UserSessionException(String code, String message) {
		super(message);
		this.code = code;
	}

	public UserSessionException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public UserSessionException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	public static final String	USER_NOT_AUTHENTICATED	= "uesr.not.authenticated";
}
