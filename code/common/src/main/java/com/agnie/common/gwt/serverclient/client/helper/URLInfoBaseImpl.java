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
package com.agnie.common.gwt.serverclient.client.helper;

public abstract class URLInfoBaseImpl implements URLInfo {
	public static final String	AGNIE_BASE_DOMAIN	= "agnie.net";

	/**
	 * Request urls base url.
	 * 
	 * @return
	 */
	public String getRootHostURL() {
		String url = (getProtocol().contains("HTTPS") || getProtocol().contains("https") ? "https" : "http");
		url += "://" + getHost();
		return url;
	}

	/**
	 * Base url with root context
	 * 
	 * @return
	 */
	public String getRootContextURL() {
		String completeUrl = getHostBaseURL();
		String url = getRootHostURL();
		if (completeUrl.contains(PROD_ROOT_CONTEXT)) {
			return url + "/" + PROD_ROOT_CONTEXT;
		}
		return url;
	}
}
