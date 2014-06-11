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

import java.util.List;

public class UserNotAutherisedException extends UserSessionRuntimeException {

	private static final long	serialVersionUID	= 1L;

	private List<String>		perms;

	public UserNotAutherisedException() {
		super(USER_NOT_AUTHORIZED);
	}

	/**
	 * 
	 * @param perms
	 */
	public UserNotAutherisedException(List<String> perms) {
		super(USER_NOT_AUTHORIZED);
		this.perms = perms;
	}

	/**
	 * @return the perms
	 */
	public List<String> getPerms() {
		return perms;
	}

}
