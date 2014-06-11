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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrSubstitutor;

public class MessageTemplate {

	// template file name
	private String	template;
	// Location of configuration path
	private String	configPath;

	// contents of the template file
	private String	messageTemplate;

	/**
	 * @param template
	 * @param configPath
	 */
	public MessageTemplate(String template, String configPath) {
		this.template = template;
		this.configPath = configPath;
	}

	public MessageTemplate(String template) {

		this.template = template;
	}

	/**
	 * read the contents of the template on its first use.
	 * 
	 * @throws IOException
	 */
	private synchronized void init() throws IOException {

		if (messageTemplate == null) {
			if (configPath == null || configPath.isEmpty()) {
				messageTemplate = IOUtils.toString(getClass().getResource("/" + template));
			} else {
				messageTemplate = IOUtils.toString(new FileInputStream(new File(configPath + "/" + template)));
			}
		}
	}

	/**
	 * Generate the message from template by replacing variable values inside template.
	 * 
	 * @param valuesMap
	 * @return
	 * @throws IOException
	 */
	public String getMessage(Map<String, String> valuesMap) throws IOException {

		if (messageTemplate == null) {
			init();
		}
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		return sub.replace(messageTemplate);
	}

}
