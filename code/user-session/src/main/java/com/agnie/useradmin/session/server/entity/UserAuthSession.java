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
package com.agnie.useradmin.session.server.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.agnie.common.time.DateService;

/**
 * to store logged in user's session information.
 * 
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "UserAuthSession.getAllExpiredSessions", query = "select usas from UserAuthSession usas where usas.expiryTimeStamp < :currentTime"),
		@NamedQuery(name = "UserAuthSession.getByUser", query = "select usas from UserAuthSession usas where usas.userId = :userId"),
		@NamedQuery(name = "UserAuthSession.removeExpired", query = "delete from UserAuthSession usas where usas.expiryTimeStamp < :currentTime") })
public class UserAuthSession implements Serializable {
	private static final long	serialVersionUID	= 1L;

	@Id
	private String				id;

	@Version
	private Integer				version;

	@Basic
	private String				userId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date				updateTimeStamp;

	@Temporal(TemporalType.TIMESTAMP)
	private Date				createTimeStamp;

	@Temporal(TemporalType.TIMESTAMP)
	private Date				expiryTimeStamp;

	@Override
	public int hashCode() {
		if (id != null)
			return id.hashCode();
		else
			return 0;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAuthSession other = (UserAuthSession) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else {
			if (hashCode() != other.hashCode())
				return false;
			else if (!id.equals(other.id))
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "UserAuthSession [id=" + id + ", version=" + version + ", userId=" + userId + ", updateTimeStamp=" + updateTimeStamp + ", createTimeStamp=" + createTimeStamp + ", expiryTimeStamp="
				+ expiryTimeStamp + "]";
	}

	/**
	 * @return the id
	 */
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
	 * @return the updateTimeStamp
	 */
	public Date getUpdateTimeStamp() {
		return updateTimeStamp;
	}

	/**
	 * @param updateTimeStamp
	 *            the updateTimeStamp to set
	 */
	public void setUpdateTimeStamp(Date updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}

	/**
	 * @return the createTimeStamp
	 */
	public Date getCreateTimeStamp() {
		return createTimeStamp;
	}

	/**
	 * @param createTimeStamp
	 *            the createTimeStamp to set
	 */
	public void setCreateTimeStamp(Date createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	/**
	 * @return the expiryTimeStamp
	 */
	public Date getExpiryTimeStamp() {
		return expiryTimeStamp;
	}

	/**
	 * @param expiryTimeStamp
	 *            the expiryTimeStamp to set
	 */
	public void setExpiryTimeStamp(Date expiryTimeStamp) {
		this.expiryTimeStamp = expiryTimeStamp;
	}

	/**
	 * @return the version
	 */
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
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.agnie.useradmin.persistance.server.entity.BaseEntity#prePersist()
	 * 
	 */
	@PrePersist
	protected void prePersist() {
		if (this.getId() == null || "".equals(this.getId().trim())) {
			this.setId(java.util.UUID.randomUUID().toString());
		}
		DateService dateSer = new DateService();
		Date dt = dateSer.getCurrentDate();
		if (this.getCreateTimeStamp() == null) {
			this.setCreateTimeStamp(dt);
		}
		this.setUpdateTimeStamp(dt);
	}

}
