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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Recipient {

	private Map<RecipientType, Set<Address>>	recipients	= new HashMap<RecipientType, Set<Address>>();

	public Recipient(String recipients) throws AddressException {

		put(RecipientType.TO, recipients);
	}

	/**
	 * Add the comma separated list of recipients list converted into InternetAddress attached TO, CC, BCC
	 * 
	 * @param type
	 *            TO,CC, BCC
	 * @param recipients
	 *            comma separated list of email ids
	 * @throws AddressException
	 */

	public void put(RecipientType type, String recipients) throws AddressException {

		Address[] adds = InternetAddress.parse(recipients);
		if (this.recipients.get(type) == null) {
			this.recipients.put(type, new HashSet<Address>());
		}
		Set<Address> recipientSet = this.recipients.get(type);
		for (Address address : adds) {
			recipientSet.add(address);
		}
	}

	/**
	 * Get array of InternetAddress for Recipients for given type (TO,CC,BCC)
	 * 
	 * @param type
	 *            TO,CC, BCC
	 * @return
	 */
	public Address[] get(RecipientType type) {

		Set<Address> rec = recipients.get(type);
		Address[] resp = null;
		if (rec != null) {
			resp = new Address[rec.size()];
			int index = 0;
			for (Address address : rec) {
				resp[index] = address;
			}
		}
		return resp;
	}
}
