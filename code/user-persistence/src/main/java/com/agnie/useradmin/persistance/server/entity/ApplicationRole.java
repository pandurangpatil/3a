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
package com.agnie.useradmin.persistance.server.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class ApplicationRole extends BaseEntity {
	@OneToOne
	private UserApplicationRegistration	userAppRegistration;
	@ManyToMany
	private List<Role>					roles;

	/**
	 * @return the roles
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @return the userAppRegistration
	 */
	public UserApplicationRegistration getUserAppRegistration() {
		return userAppRegistration;
	}

	/**
	 * @param userAppRegistration
	 *            the userAppRegistration to set
	 */
	public void setUserAppRegistration(UserApplicationRegistration userAppRegistration) {
		this.userAppRegistration = userAppRegistration;
	}

}
