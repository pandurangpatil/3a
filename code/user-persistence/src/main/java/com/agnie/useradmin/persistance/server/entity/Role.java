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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import open.pandurang.gwt.helper.requestfactory.marker.RFEntityProxy;
import open.pandurang.gwt.helper.requestfactory.marker.RFProxyMethod;

import com.agnie.useradmin.persistance.client.enums.AuthLevel;

/**
 * to define roles for a domain
 * 
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Role.getByApplicationId", query = "select rl from Role rl where rl.application.id = :applicationId"),
		@NamedQuery(name = "Role.getByDomain", query = "select rl from Role rl where rl.application.domain = :domain"),
		@NamedQuery(name = "Role.getByDomainAndName", query = "select rl from Role rl where rl.name = :name and rl.application.domain = :domain"),
		@NamedQuery(name = "Role.getByApplicationAndName", query = "select rl from Role rl where rl.name = :name and rl.application.id = :applicationId"),
		@NamedQuery(name = "Role.getFromIds", query = "select rl from Role rl where rl.id IN :ids"), @NamedQuery(name = "Role.delete", query = "delete from Role rl where rl.id IN :ids") })
@RFEntityProxy(value = UserAdminEntityLocator.class, generateEntityRequest = false)
public class Role extends BaseEntity {
	@Column(nullable = false)
	private String				name;
	@Basic
	private String				description;
	@OneToMany
	private List<Permission>	permissions;
	@ManyToOne
	private Application			application;
	@Enumerated(EnumType.STRING)
	private AuthLevel			level;

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
	 * @return the permissions
	 */
	@RFProxyMethod
	@XmlTransient
	public List<Permission> getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions
	 *            the permissions to set
	 */
	@RFProxyMethod
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return the domain
	 */
	@RFProxyMethod
	@XmlTransient
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
	@XmlTransient
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
		return "Role [name=" + name + ", description=" + description + ", permissions=" + permissions + ", level=" + level + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((permissions == null) ? 0 : permissions.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
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
		if (permissions == null) {
			if (other.permissions != null)
				return false;
		} else if (!permissions.equals(other.permissions))
			return false;
		return true;
	}

}
