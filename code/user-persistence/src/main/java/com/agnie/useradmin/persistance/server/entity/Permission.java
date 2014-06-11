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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import open.pandurang.gwt.helper.requestfactory.marker.RFEntityProxy;
import open.pandurang.gwt.helper.requestfactory.marker.RFProxyMethod;

import com.agnie.useradmin.persistance.client.enums.AuthLevel;

/**
 * to define permissions for a domain
 * 
 * 
 */
@Entity
@NamedQueries({

@NamedQuery(name = "Permission.delete", query = "DELETE FROM Permission pm WHERE pm.id IN :ids") })
@RFEntityProxy(value = UserAdminEntityLocator.class, generateEntityRequest = false)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "APPLICATION_ID", "CODE" }))
public class Permission extends BaseEntity {

	@Basic
	private String		name;
	@Column(nullable = false)
	private String		code;
	@Basic
	private String		description;
	@ManyToOne
	private Application	application;
	@Enumerated(EnumType.STRING)
	private AuthLevel	level;

	/**
	 * @return the name
	 */
	@RFProxyMethod
	@NotNull(message = "required")
	@Size(min = 3, message = "minLength3")
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	@RFProxyMethod
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the code
	 */
	@RFProxyMethod
	@NotNull(message = "required")
	@Size(min = 3, message = "minLength3")
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	@RFProxyMethod
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the description
	 */
	@RFProxyMethod
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	@RFProxyMethod
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the domain
	 */
	@RFProxyMethod
	public Application getApplication() {
		return application;
	}

	/**
	 * @param application
	 *            the domain to set
	 */
	public void setApplication(Application application) {
		this.application = application;
	}

	/**
	 * @return the level
	 */
	@RFProxyMethod
	public AuthLevel getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	@RFProxyMethod
	public void setLevel(AuthLevel level) {
		this.level = level;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Permission [name=" + name + ", code=" + code + ", description=" + description + ", application=" + application + ", level=" + level + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Permission other = (Permission) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (level != other.level)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
