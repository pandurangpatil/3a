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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import open.pandurang.gwt.helper.requestfactory.marker.RFEntityProxy;
import open.pandurang.gwt.helper.requestfactory.marker.RFProxyMethod;

import com.agnie.useradmin.persistance.client.enums.GeneralStatus;

/**
 * to define permissions for a domain
 * 
 */
@NamedQueries({ @NamedQuery(name = "Context.getByDomain", query = "select ctx from Context ctx where ctx.status = :status and ctx.application.domain = :domain"),
		@NamedQuery(name = "Context.getByName", query = "select ctx from Context ctx where ctx.status = :status and ctx.name = :name and ctx.application.domain = :domain") })
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "APPLICATION_ID", "NAME" }))
@Entity
@RFEntityProxy(value = UserAdminEntityLocator.class, generateEntityRequest = false)
public class Context extends BaseEntity {

	@Column(nullable = false)
	@Basic
	private String			name;
	@Basic
	private String			description;
	@JoinColumn(nullable = false)
	@ManyToOne
	private Application		application;
	@JoinColumn(nullable = false)
	@Enumerated(EnumType.STRING)
	private GeneralStatus	status	= GeneralStatus.ACTIVE;

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
	 * @return the domain
	 */
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
	 * @return the status
	 */
	@XmlTransient
	public GeneralStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(GeneralStatus status) {
		this.status = status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Context [name=" + name + ", description=" + description + ", application=" + application + ",status=" + status + "]";
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
		result = prime * result + ((application == null) ? 0 : application.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		Context other = (Context) obj;
		if (application == null) {
			if (other.application != null)
				return false;
		} else if (!application.equals(other.application))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

}
