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

public enum Param {

	RANGE_START("start-index"), RANGE_END("end-index"), SEARCH_KEYWORD("search-key"), APP_USER_STATUS_FILTER("app-user-status-filter");

	private String	key;

	private Param(String key) {
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

}
