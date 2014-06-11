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
package com.agnie.common.email;

/**
 * @author Pandurang Patil 15-Mar-2014
 * 
 */
public class Email {
	private String		senderKey;
	private Recipient	recipient;
	private String		subject;
	private String		message;

	/**
	 * @param senderKey
	 *            Number of accounts with their keys can be configured inside config files. Each account will be
	 *            identified by a unique key. Caller need to pass that key to identify which account details need to be
	 *            used to send given mail.
	 * @param recipient
	 *            recipients to whom email need to be sent.
	 * @param subject
	 *            Subject of the email.
	 * @param message
	 *            body of the email.
	 */
	public Email(String senderKey, Recipient recipient, String subject, String message) {
		super();
		this.senderKey = senderKey;
		this.recipient = recipient;
		this.subject = subject;
		this.message = message;
	}

	/**
	 * @return the senderKey
	 */
	public String getSenderKey() {
		return senderKey;
	}

	/**
	 * @return the recipient
	 */
	public Recipient getRecipient() {
		return recipient;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Email [senderKey=" + senderKey + ", recipient=" + recipient + ", subject=" + subject + ", message=" + message + "]";
	}

}
