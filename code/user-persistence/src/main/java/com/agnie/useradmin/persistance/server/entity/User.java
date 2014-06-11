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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import open.pandurang.gwt.helper.requestfactory.marker.RFEntityProxy;
import open.pandurang.gwt.helper.requestfactory.marker.RFProxyMethod;

import org.hibernate.validator.constraints.Email;

import com.agnie.useradmin.persistance.client.enums.Language;
import com.agnie.useradmin.persistance.client.enums.UserStatus;
import com.agnie.useradmin.persistance.client.service.dto.UserInfo;

/**
 * Registered user information
 * 
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "User.getByEmailId", query = "select us from User us where us.emailId = :emailId and us.status = :status"),
		@NamedQuery(name = "User.getByUserName", query = "select us from User us where us.userName = :userName and us.status = :status"),
		@NamedQuery(name = "User.delete", query = "UPDATE User us SET us.status=:status WHERE us.id IN :ids") })
@RFEntityProxy(value = UserAdminEntityLocator.class, generateEntityRequest = false)
public class User extends BaseEntity {

	@Basic
	private String								title;
	@Basic
	private String								firstName;
	@Basic
	private String								lastName;
	@Column(nullable = false)
	@NotNull
	@Email
	@Size(max = 30, message = "maxlength30")
	private String								emailId;
	@Column(nullable = false)
	private String								password;
	@Column(unique = true, nullable = false)
	@NotNull
	@Size(min = 4, max = 20, message = "min4max20")
	private String								userName;
	@Enumerated(EnumType.STRING)
	private Language							defaultLanguage	= Language.ENGLISH;
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<UserApplicationRegistration>	userRegistration;
	@Enumerated(EnumType.STRING)
	private UserStatus							status			= UserStatus.PENDING_FOR_VERIFICATION;
	@Temporal(TemporalType.TIMESTAMP)
	private Date								lastLoginTimeStamp;
	@Basic
	private String								emailVarificationToken;
	@Basic
	private String								profileImage;

	@Transient
	transient private String					runtTimeToken;

	@Override
	@RFProxyMethod
	@XmlTransient
	public String getId() {
		return id;
	}

	/**
	 * @return the title
	 */
	@RFProxyMethod
	@Size(max = 10, message = "maxlength10")
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	@RFProxyMethod
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the firstName
	 */
	@RFProxyMethod
	@Size(max = 20, message = "maxlength20")
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	@RFProxyMethod
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	@RFProxyMethod
	@Size(max = 20, message = "maxlength20")
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	@RFProxyMethod
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the emailId
	 */
	@RFProxyMethod
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the password
	 */
	@XmlTransient
	@NotNull
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the defaultLanguage
	 */
	@RFProxyMethod
	public Language getDefaultLanguage() {
		return defaultLanguage;
	}

	/**
	 * @param defaultLanguage
	 *            the defaultLanguage to set
	 */
	@RFProxyMethod
	public void setDefaultLanguage(Language defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	/**
	 * @return the userRegistration
	 */
	@XmlTransient
	public List<UserApplicationRegistration> getUserRegistration() {
		return userRegistration;
	}

	/**
	 * @param userRegistration
	 *            the userRegistration to set
	 */
	public void setUserRegistration(List<UserApplicationRegistration> userRegistration) {
		this.userRegistration = userRegistration;
	}

	/**
	 * @return the status
	 */
	@RFProxyMethod
	@XmlTransient
	public UserStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	@RFProxyMethod
	public void setStatus(UserStatus status) {
		this.status = status;
	}

	/**
	 * @return the userName
	 */
	@RFProxyMethod
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the lastLoginTimeStamp
	 */
	@XmlTransient
	public Date getLastLoginTimeStamp() {
		return lastLoginTimeStamp;
	}

	/**
	 * @param lastLoginTimeStamp
	 *            the lastLoginTimeStamp to set
	 */
	public void setLastLoginTimeStamp(Date lastLoginTimeStamp) {
		this.lastLoginTimeStamp = lastLoginTimeStamp;
	}

	/**
	 * @return the emailVarificationToken
	 */
	@XmlTransient
	public String getEmailVarificationToken() {
		return emailVarificationToken;
	}

	/**
	 * @param emailVarificationToken
	 *            the emailVarificationToken to set
	 */
	public void setEmailVarificationToken(String emailVarificationToken) {
		this.emailVarificationToken = emailVarificationToken;
	}

	/**
	 * @return the profileImage
	 */
	@RFProxyMethod
	public String getProfileImage() {
		return profileImage;
	}

	/**
	 * @param profileImage
	 *            the profileImage to set
	 */
	@RFProxyMethod
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	/**
	 * @return the runtTimeToken
	 */
	@XmlTransient
	public String getRuntTimeToken() {
		return runtTimeToken;
	}

	/**
	 * @param runtTimeToken
	 *            the runtTimeToken to set
	 */
	public void setRuntTimeToken(String runtTimeToken) {
		this.runtTimeToken = runtTimeToken;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((defaultLanguage == null) ? 0 : defaultLanguage.hashCode());
		result = prime * result + ((emailId == null) ? 0 : emailId.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		User other = (User) obj;
		if (defaultLanguage != other.defaultLanguage)
			return false;
		if (emailId == null) {
			if (other.emailId != null)
				return false;
		} else if (!emailId.equals(other.emailId))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	public User importDTO(UserInfo us) {
		this.setTitle(us.getTitle());
		this.setFirstName(us.getFirstName());
		this.setLastName(us.getLastName());
		this.setUserName(us.getUserName());
		this.setPassword(us.getPassword());
		this.setEmailId(us.getEmailId());
		this.setDefaultLanguage(us.getDefaultLanguage());
		return this;
	}

}
