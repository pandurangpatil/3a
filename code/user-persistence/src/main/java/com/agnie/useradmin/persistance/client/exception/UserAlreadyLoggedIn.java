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

public class UserAlreadyLoggedIn extends UserAdminException {

	private static final long	serialVersionUID	= 1L;

	private String				username;

	public UserAlreadyLoggedIn() {
		super(USER_ALREADY_LOGGED_IN);
	}

	public UserAlreadyLoggedIn(String username) {
		super(USER_ALREADY_LOGGED_IN);
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}
