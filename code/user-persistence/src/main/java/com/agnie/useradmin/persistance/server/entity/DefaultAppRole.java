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
package com.agnie.useradmin.persistance.server.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.agnie.useradmin.persistance.client.enums.AuthLevel;

@Entity
@NamedQueries({ @NamedQuery(name = "DefaultAppRole.getByDomainAndLevel", query = "select dm from DefaultAppRole dm where dm.application.domain = :domain and dm.level = :level") })
public class DefaultAppRole extends BaseEntity {
	/*
	 * TODO: Need to add junits.
	 */
	@Enumerated(EnumType.STRING)
	private AuthLevel	level;
	@ManyToOne
	private Application	application;
	@ManyToMany
	private List<Role>	roles;

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
	 * @return the application
	 */
	public Application getApplication() {
		return application;
	}

	/**
	 * @param application
	 *            the application to set
	 */
	public void setApplication(Application application) {
		this.application = application;
	}

	/**
	 * @return the level
	 */
	public AuthLevel getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(AuthLevel level) {
		this.level = level;
	}

}
