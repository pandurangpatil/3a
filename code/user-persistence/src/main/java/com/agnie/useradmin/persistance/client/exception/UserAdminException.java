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
package com.agnie.useradmin.persistance.client.exception;

public class UserAdminException extends Exception {

	private static final long	serialVersionUID	= 1L;
	private String				code;

	public UserAdminException(String code) {
		super();
		this.code = code;
	}

	public UserAdminException(String code, String message) {
		super(message);
		this.code = code;
	}

	public UserAdminException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public UserAdminException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	public static final String	USER_NAME_EXIST			= "uesr.name.exist";
	public static final String	USER_ALREADY_LOGGED_IN	= "uesr.already.logged.in";
	public static final String	USER_NOT_FOUND			= "uesr.not.found";
	public static final String	REQUEST_NOT_PROCESSED	= "request.not.processed";
	public static final String	INVALID_OLD_PASSWORD	= "invalid.old.password";
	public static final String	INVALID_USER_DETAILS	= "invalid.user.details";
	public static final String	INVALID_REQUEST			= "invalid.request";
	public static final String	INVALID_OPERATION		= "invalid.operation";
	public static final String	INVALID_DOMAIN			= "invalid.domain";
	public static final String	INVALID_CAPTCHA			= "invalid.captcha";
	public static final String	INVALID_CONTEXT			= "invalid.context";
	public static final String	INACTIVE_DOMAIN			= "inactive.context";
	public static final String	USER_NOT_REG			= "user.not.registered";
	public static final String	APP_REG_NOT_APPROVED	= "app.reg.not.approved";
	public static final String	MISSING_PARAMETER		= "missing.parameter";
	public static final String	INTERNAL_SERVER_ERROR	= "internal.server.error";
	public static final String	USER_ALREADY_REG		= "user.already.registered";
	public static final String	REG_DISABLED			= "regitration.disabled";
}
