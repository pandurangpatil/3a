package com.agnie.useradmin.service.client.entity;

import com.agnie.common.gwt.serverclient.client.enums.Language;

/**
 * 
 * @author Pandurang Patil 06-Feb-2014
 * 
 */
public class User {

	private String		title;
	private String		firstName;
	private String		lastName;
	private String		emailId;
	private String		userName;
	private Language	defaultLanguage	= Language.ENGLISH;
	private String		profileImage;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the emailId
	 */
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
	 * @return the defaultLanguage
	 */
	public Language getDefaultLanguage() {
		return defaultLanguage;
	}

	/**
	 * @param defaultLanguage
	 *            the defaultLanguage to set
	 */
	public void setDefaultLanguage(Language defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	/**
	 * @return the userName
	 */
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
	 * @return the profileImage
	 */
	public String getProfileImage() {
		return profileImage;
	}

	/**
	 * @param profileImage
	 *            the profileImage to set
	 */
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [title=" + title + ", firstName=" + firstName + ", lastName=" + lastName + ", emailId=" + emailId + ", userName=" + userName + ", defaultLanguage=" + defaultLanguage
				+ ", profileImage=" + profileImage + "]";
	}

}
