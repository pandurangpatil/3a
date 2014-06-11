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
package com.agnie.common.gwt.serverclient.client.enums;

public enum QueryString {
	SOURCE("source"), SESSION("sessionid"), GWT_DEV_MODE("gwt.codesvr"), LOCALE("locale"), QUESTION_MARK("?"), AMPERSAND("&"), HASH("#"), USER_NAME("user"), DOMAIN("domain"), SELECTED_DOMAIN(
			"sel-domain"), HISTORY_HASH("history-token"), SELECTED_CONTEXT("sel-context"), CONTEXT("context"), API_ACCESS_KEY("access-key"), REFERER("Referer");

	private String	key;

	private QueryString(String key) {
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

}
