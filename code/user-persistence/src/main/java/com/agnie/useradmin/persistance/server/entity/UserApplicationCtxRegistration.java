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
		@NamedQuery(name = "UserApplicationCtxRegistration.updateStatus", query = "UPDATE UserApplicationCtxRegistration aur SET aur.status = :status WHERE aur.id IN :ids and aur.usApplicationRegistration.app.domain = :domain and aur.owner = 0"),
		@NamedQuery(name = "UserApplicationCtxRegistration.getByUserApplicationAndContext", query = "select uacr from UserApplicationCtxRegistration uacr where uacr.user.id = :userid and uacr.usApplicationRegistration.app.domain = :domain and uacr.context.name = :context"),
		@NamedQuery(name = "UserApplicationCtxRegistration.getByUser", query = "select uacr from UserApplicationCtxRegistration uacr where uacr.user.id = :userid and uacr.usApplicationRegistration.app.domain = :domain"),
		@NamedQuery(name = "UserApplicationCtxRegistration.getByUserName", query = "select uacr from UserApplicationCtxRegistration uacr where uacr.user.userName = :username and uacr.usApplicationRegistration.app.domain = :domain and uacr.context.name = :context") })
@RFEntityProxy(value = UserAdminEntityLocator.class, generateEntityRequest = false)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "USAPPLICATIONREGISTRATION_ID", "USER_ID", "CONTEXT_ID" }))
public class UserApplicationCtxRegistration extends BaseEntity {
	@ManyToOne(fetch = FetchType.EAGER)
	private User						user;
	@ManyToOne
	private UserApplicationRegistration	usApplicationRegistration;
	@ManyToOne
	private Context						context;
	@OneToOne(mappedBy = "usApplicationCtxRegistration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private ContextRole					contextRole;
	@OneToOne(mappedBy = "usApplicationCtxRegistration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private AdminContextRole			adminContextRole;
	@Enumerated(EnumType.STRING)
	private RequestStatus				status;
	@Basic
	private Boolean						owner	= false;

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
	 * @return the usApplicationRegistration
	 */
	@RFProxyMethod
	@XmlTransient
	public UserApplicationRegistration getUsApplicationRegistration() {
		return usApplicationRegistration;
	}

	/**
	 * @param usApplicationRegistration
	 *            the usApplicationRegistration to set
	 */
	public void setUsApplicationRegistration(UserApplicationRegistration usApplicationRegistration) {
		this.usApplicationRegistration = usApplicationRegistration;
	}

	/**
	 * @return the context
	 */
	@RFProxyMethod
	@XmlTransient
	public Context getContext() {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * @return the contextRole
	 */
	@XmlTransient
	public ContextRole getContextRole() {
		return contextRole;
	}

	/**
	 * @param contextRole
	 *            the contextRole to set
	 */
	public void setContextRole(ContextRole contextRole) {
		this.contextRole = contextRole;
	}

	/**
	 * @return the adminContextRole
	 */
	@XmlTransient
	public AdminContextRole getAdminContextRole() {
		return adminContextRole;
	}

	/**
	 * @param adminContextRole
	 *            the adminContextRole to set
	 */
	public void setAdminContextRole(AdminContextRole adminContextRole) {
		this.adminContextRole = adminContextRole;
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
		return "UserApplicationCtxRegistration [usApplicationRegistration=" + usApplicationRegistration + ", context=" + context + ", contextRole=" + contextRole + ", adminContextRole="
				+ adminContextRole + ", status=" + status + "]";
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
		result = prime * result + ((adminContextRole == null) ? 0 : adminContextRole.hashCode());
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((contextRole == null) ? 0 : contextRole.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((usApplicationRegistration == null) ? 0 : usApplicationRegistration.hashCode());
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
		UserApplicationCtxRegistration other = (UserApplicationCtxRegistration) obj;
		if (adminContextRole == null) {
			if (other.adminContextRole != null)
				return false;
		} else if (!adminContextRole.equals(other.adminContextRole))
			return false;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (contextRole == null) {
			if (other.contextRole != null)
				return false;
		} else if (!contextRole.equals(other.contextRole))
			return false;
		if (status != other.status)
			return false;
		if (usApplicationRegistration == null) {
			if (other.usApplicationRegistration != null)
				return false;
		} else if (!usApplicationRegistration.equals(other.usApplicationRegistration))
			return false;
		return true;
	}

}
