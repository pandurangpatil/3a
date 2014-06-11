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
package com.agnie.useradmin.session.server.entity;

import java.util.List;

public class UserPermissions {

	private boolean			superUser	= false;
	private List<String>	permissions;

	public UserPermissions() {
		super();
	}

	/**
	 * @param superUser
	 * @param permissions
	 */
	public UserPermissions(boolean superUser, List<String> permissions) {
		super();
		this.superUser = superUser;
		this.permissions = permissions;
	}

	/**
	 * @return the superUser
	 */
	public boolean isSuperUser() {
		return superUser;
	}

	/**
	 * @param superUser
	 *            the superUser to set
	 */
	public void setSuperUser(boolean superUser) {
		this.superUser = superUser;
	}

	/**
	 * @return the permissions
	 */
	public List<String> getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions
	 *            the permissions to set
	 */
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

}
