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
package com.agnie.useradmin.persistance.server.common.digester;

import java.util.ArrayList;
import java.util.List;

import com.agnie.useradmin.persistance.client.enums.AuthLevel;

/**
 * to define roles for a domain
 * 
 */
public class DummyRole {
	private String			name;
	private String			description;
	private AuthLevel		authLevel;
	private List<String>	permissions	= new ArrayList<String>();

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the permissions
	 */
	public List<String> getPermissions() {
		return permissions;
	}

	/**
	 * 
	 * @param perm
	 */
	public void addPermission(String perm) {
		permissions.add(perm);
	}

	/**
	 * @param permissions
	 *            the permissions to set
	 */
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return the authLevel
	 */
	public AuthLevel getAuthLevel() {
		return authLevel;
	}

	/**
	 * @param authLevel
	 *            the authLevel to set
	 */
	public void setAuthLevel(AuthLevel authLevel) {
		this.authLevel = authLevel;
	}

}
