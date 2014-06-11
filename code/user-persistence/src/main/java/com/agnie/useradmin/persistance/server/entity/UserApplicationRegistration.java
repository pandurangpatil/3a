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
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlTransient;

import open.pandurang.gwt.helper.requestfactory.marker.RFEntityProxy;
import open.pandurang.gwt.helper.requestfactory.marker.RFProxyMethod;

import com.agnie.useradmin.persistance.client.enums.RequestStatus;

/**
 * to link domain user and number of roles to that user both domain roles and administrative roles for that domain
 * 
 * TODO: add junit's inside ContextRoleTest to connect UserApplicationRegistration with application role and Admin role.
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "UserApplicationRegistration.updateStatus", query = "UPDATE UserApplicationRegistration aur SET aur.status = :status WHERE aur.id IN :ids and aur.app.domain = :domain and aur.owner = 0"),
		@NamedQuery(name = "UserApplicationRegistration.getByUserAndApplication", query = "select aur from UserApplicationRegistration aur where aur.user.id = :userid and aur.app.domain = :domain"),
		@NamedQuery(name = "UserApplicationRegistration.getByUser", query = "select dmur from UserApplicationRegistration dmur where dmur.user.id = :userid "),
		@NamedQuery(name = "UserApplicationRegistration.getByUserName", query = "select dmur from UserApplicationRegistration dmur where dmur.user.userName = :username and dmur.app.domain = :domain") })
@RFEntityProxy(value = UserAdminEntityLocator.class, generateEntityRequest = false)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "APP_ID", "USER_ID" }) })
public class UserApplicationRegistration extends BaseEntity {
	@ManyToOne(fetch = FetchType.EAGER)
	private User			user;
	@ManyToOne
	private Application		app;
	@OneToOne(mappedBy = "userAppRegistration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private ApplicationRole	applicationRole;
	@OneToOne(mappedBy = "userAppRegistration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private AdminRole		adminRole;
	@Enumerated(EnumType.STRING)
	private RequestStatus	status;
	@Basic
	private Boolean			owner	= false;

	/**
	 * @return the user
	 */
	@RFProxyMethod
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the domain
	 */
	@RFProxyMethod
	@XmlTransient
	public Application getApp() {
		return app;
	}

	/**
	 * @param app
	 *            the domain to set
	 */
	public void setApp(Application app) {
		this.app = app;
	}

	/**
	 * @return the adminRole
	 */
	@XmlTransient
	public AdminRole getAdminRole() {
		return adminRole;
	}

	/**
	 * @param adminRole
	 *            the adminRole to set
	 */
	public void setAdminRole(AdminRole adminRole) {
		this.adminRole = adminRole;
	}

	/**
	 * @return the status
	 */
	@RFProxyMethod
	public RequestStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	@RFProxyMethod
	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	/**
	 * @return the applicationRole
	 */
	@XmlTransient
	public ApplicationRole getApplicationRole() {
		return applicationRole;
	}

	/**
	 * @param applicationRole
	 *            the applicationRole to set
	 */
	public void setApplicationRole(ApplicationRole applicationRole) {
		this.applicationRole = applicationRole;
	}

	/**
	 * @return the owner
	 */
	@RFProxyMethod
	public Boolean getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(Boolean owner) {
		this.owner = owner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserApplicationRegistration [user=" + user + ", app=" + app + ", applicationRole=" + applicationRole + ", adminRole=" + adminRole + ", status=" + status + "]";
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
		result = prime * result + ((adminRole == null) ? 0 : adminRole.hashCode());
		result = prime * result + ((app == null) ? 0 : app.hashCode());
		result = prime * result + ((applicationRole == null) ? 0 : applicationRole.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		UserApplicationRegistration other = (UserApplicationRegistration) obj;
		if (adminRole == null) {
			if (other.adminRole != null)
				return false;
		} else if (!adminRole.equals(other.adminRole))
			return false;
		if (app == null) {
			if (other.app != null)
				return false;
		} else if (!app.equals(other.app))
			return false;
		if (applicationRole == null) {
			if (other.applicationRole != null)
				return false;
		} else if (!applicationRole.equals(other.applicationRole))
			return false;
		if (status != other.status)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}
