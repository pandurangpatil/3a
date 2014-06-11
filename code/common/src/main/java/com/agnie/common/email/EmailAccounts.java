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

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EmailAccounts {

	public static final String			EMAIL_ACCOUNTS_CONFIG	= "mail_accounts.json";

	// map of email accounts attached to a key
	private Map<String, EmailAccount>	mailAccounts;
	// jackson object mapper to read json contents from config file.
	private ObjectMapper				mapper;

	@Inject
	public EmailAccounts(ObjectMapper mapper) {

		this.mapper = mapper;
	}

	private synchronized void init() throws JsonParseException, JsonMappingException, IOException {

		if (mailAccounts == null) {
			mailAccounts = mapper.readValue(getClass().getResource("/" + EMAIL_ACCOUNTS_CONFIG), new TypeReference<Map<String, EmailAccount>>() {
			});

		}
	}

	public EmailAccount getEmailAccount(String key) throws JsonParseException, JsonMappingException, IOException {

		if (mailAccounts == null) {
			init();
		}
		return mailAccounts.get(key);
	}

}
