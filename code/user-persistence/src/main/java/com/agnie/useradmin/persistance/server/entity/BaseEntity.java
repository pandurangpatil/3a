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

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;

import open.pandurang.gwt.helper.requestfactory.marker.RFProxyMethod;

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.time.DateService;

/**
 * Common generic entity methods are defined in BaseEntity
 * 
 * 
 */
@MappedSuperclass
public abstract class BaseEntity {
	@Id
	protected String		id;
	@Version
	protected Integer		version;
	@Temporal(TemporalType.TIMESTAMP)
	protected Date			createdOn;
	@Temporal(TemporalType.TIMESTAMP)
	protected Date			updateOn;
	@Basic
	private String			createdBy;
	@Basic
	private String			updatedBy;
	transient UserAccount	curntUser;

	/**
	 * @return the id
	 */
	@RFProxyMethod
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	@XmlTransient
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @return the createdOn
	 */
	@XmlTransient
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn
	 *            the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the updateOn
	 */
	@XmlTransient
	public Date getUpdateOn() {
		return updateOn;
	}

	/**
	 * @param updateOn
	 *            the updateOn to set
	 */
	public void setUpdateOn(Date updateOn) {
		this.updateOn = updateOn;
	}

	/**
	 * @return the createdBy
	 */
	@XmlTransient
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the updatedBy
	 */
	@XmlTransient
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy
	 *            the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the curntUser
	 */
	@XmlTransient
	public UserAccount getCurntUser() {
		return curntUser;
	}

	/**
	 * @param curntUser
	 *            the curntUser to set
	 */
	public void setCurntUser(UserAccount curntUser) {
		this.curntUser = curntUser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BaseEntity [id=" + id + ", version=" + version + "]";
	}

	@PrePersist
	protected void prePersist() {
		/*
		 * NOTE: One need to call inject(BaseEntity entity) method defined inside BaseUserAdminService before calling
		 * persist method. Which will inject currently logged in user details inside entity. This could have been
		 * automated by using guice interceptors, but its not good idea to intercept all the calls to inject the data
		 * which is required at the time of saving.
		 */
		Date dt = new DateService().getCurrentDate();
		if (this.getId() == null || this.getId().isEmpty()) {
			this.setId(java.util.UUID.randomUUID().toString());
		}
		if (this.getCreatedOn() == null) {
			this.setCreatedOn(dt);
			if (curntUser != null)
				this.setCreatedBy(curntUser.getId());
		}
		this.setUpdateOn(dt);
		if (curntUser != null)
			this.setUpdatedBy(curntUser.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

}
