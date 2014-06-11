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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import open.pandurang.gwt.helper.requestfactory.marker.RFEntityProxy;
import open.pandurang.gwt.helper.requestfactory.marker.RFProxyMethod;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;

import com.agnie.useradmin.persistance.client.enums.GeneralStatus;
import com.agnie.useradmin.persistance.client.enums.RequestStatus;

/**
 * Domain information with domain home url register domain name and other information
 * 
 * 
 */
@Entity
@Cache(
// Cache everything until the JVM decides memory is low.
type = CacheType.SOFT, size = 64000, // Use 64,000 as the initial cache size.
expiry = 18000000, // 10 minutes
alwaysRefresh = true, refreshOnlyIfNewer = true,
// if cache coordination is used, only send invalidation messages.
coordinationType = CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS)
@NamedQueries({ @NamedQuery(name = "Application.getByDomain", query = "select dm from Application dm where dm.status = :status and  dm.domain = :domain"),
		@NamedQuery(name = "Application.getByDomainAnyStatus", query = "select dm from Application dm where dm.domain = :domain") })
@RFEntityProxy(value = UserAdminEntityLocator.class, generateEntityRequest = false)
public class Application extends BaseEntity {
	@Basic
	private String				businessName;
	@Column(unique = true, nullable = false)
	private String				domain;
	@Basic
	private String				URL;
	@Basic
	private String				iconURL;
	@Enumerated(EnumType.STRING)
	private RequestStatus		defaultAppStatus	= RequestStatus.ACTIVE;
	@Enumerated(EnumType.STRING)
	private RequestStatus		defaultCtxStatus	= RequestStatus.ACTIVE;
	@OneToMany(mappedBy = "application", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Role>			roles;
	@OneToMany(mappedBy = "application", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Permission>	permissions;
	@Enumerated(EnumType.STRING)
	private GeneralStatus		status				= GeneralStatus.ACTIVE;
	@Basic
	private String				apiAccessKey;
	@Basic
	private String				contactEmail;
	@Basic
	private Boolean				multipleContexts;
	transient private boolean	admin				= false;

	/**
	 * @return the buissnessName
	 */
	@RFProxyMethod
	@NotNull(message = "required")
	public String getBusinessName() {
		return businessName;
	}

	/**
	 * @param businessName
	 *            the buissnessName to set
	 */
	@RFProxyMethod
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	/**
	 * @return the uRL
	 */
	@RFProxyMethod
	@NotNull(message = "required")
	@Size(min = 3, message = "minLength3")
	public String getURL() {
		return URL;
	}

	/**
	 * @param uRL
	 *            the uRL to set
	 */
	@RFProxyMethod
	public void setURL(String uRL) {
		URL = uRL;
	}

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
	 * @return the permissions
	 */
	public List<Permission> getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions
	 *            the permissions to set
	 */
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return the domain
	 */
	@RFProxyMethod
	@NotNull(message = "required")
	@Size(min = 3, message = "minLength3")
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain
	 *            the domain to set
	 */
	@RFProxyMethod
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the status
	 */
	@RFProxyMethod
	public GeneralStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	@RFProxyMethod
	public void setStatus(GeneralStatus status) {
		this.status = status;
	}

	/**
	 * @return the iconURL
	 */
	@RFProxyMethod
	public String getIconURL() {
		return iconURL;
	}

	/**
	 * @param iconURL
	 *            the iconURL to set
	 */
	@RFProxyMethod
	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}

	/**
	 * @return the admin
	 */
	@RFProxyMethod
	public boolean isAdmin() {
		return admin;
	}

	/**
	 * @param admin
	 *            the admin to set
	 */
	@RFProxyMethod
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	/**
	 * @return the defaultAppStatus
	 */
	@RFProxyMethod
	@NotNull(message = "required")
	public RequestStatus getDefaultAppStatus() {
		return defaultAppStatus;
	}

	/**
	 * @param defaultAppStatus
	 *            the defaultAppStatus to set
	 */
	@RFProxyMethod
	public void setDefaultAppStatus(RequestStatus defaultAppStatus) {
		this.defaultAppStatus = defaultAppStatus;
	}

	/**
	 * @return the defaultCtxStatus
	 */
	@RFProxyMethod
	@NotNull(message = "required")
	public RequestStatus getDefaultCtxStatus() {
		return defaultCtxStatus;
	}

	/**
	 * @param defaultCtxStatus
	 *            the defaultCtxStatus to set
	 */
	@RFProxyMethod
	public void setDefaultCtxStatus(RequestStatus defaultCtxStatus) {
		this.defaultCtxStatus = defaultCtxStatus;
	}

	/**
	 * @return the apiAccessKey
	 */
	@RFProxyMethod
	public String getApiAccessKey() {
		return apiAccessKey;
	}

	/**
	 * @param apiAccessKey
	 *            the apiAccessKey to set
	 */
	public void setApiAccessKey(String apiAccessKey) {
		this.apiAccessKey = apiAccessKey;
	}

	/**
	 * @return the contactEmail
	 */
	@RFProxyMethod
	@NotNull(message = "required")
	public String getContactEmail() {
		return contactEmail;
	}

	/**
	 * @param contactEmail
	 *            the contactEmail to set
	 */
	@RFProxyMethod
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
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
		result = prime * result + ((URL == null) ? 0 : URL.hashCode());
		result = prime * result + ((businessName == null) ? 0 : businessName.hashCode());
		result = prime * result + ((defaultAppStatus == null) ? 0 : defaultAppStatus.hashCode());
		result = prime * result + ((defaultCtxStatus == null) ? 0 : defaultCtxStatus.hashCode());
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((iconURL == null) ? 0 : iconURL.hashCode());
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
		Application other = (Application) obj;
		if (URL == null) {
			if (other.URL != null)
				return false;
		} else if (!URL.equals(other.URL))
			return false;
		if (businessName == null) {
			if (other.businessName != null)
				return false;
		} else if (!businessName.equals(other.businessName))
			return false;
		if (defaultAppStatus != other.defaultAppStatus)
			return false;
		if (defaultCtxStatus != other.defaultCtxStatus)
			return false;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (iconURL == null) {
			if (other.iconURL != null)
				return false;
		} else if (!iconURL.equals(other.iconURL))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

}
