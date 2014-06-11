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

import java.util.Set;

public interface URLInfo {
	public String	PROD_ROOT_CONTEXT	= "userapp";

	/**
	 * 
	 * @param name
	 *            of query parameter
	 * @return corresponding value of the parameter in query string.
	 */
	String getParameter(String name);

	/**
	 * 
	 * @param name
	 *            of query parameter
	 * @return all values associated with given query parameter key. If you add multiple query parameters with same key
	 *         name then you can retrieve all list of values for given single key
	 */
	String[] getAllValues(String name);

	/**
	 * Complete HostURL including query string parameters
	 * 
	 * @return complete URL
	 */
	String getHostURL();

	/**
	 * 
	 * @return host and port number from URL.
	 */
	String getHost();

	/**
	 * 
	 * @return set of all key names in a query string
	 */
	Set<String> getParameterKeySet();

	/**
	 * 
	 * @param url
	 *            which need to be encoded
	 * @return UTF-8 encoded URL
	 */
	String getUTF8EncodedURL(String url);

	/**
	 * 
	 * @param encodedUrl
	 *            which is already encoded and need to be decoded.
	 * @return UTF-8 decoded url.
	 */
	String decodeUTF8URL(String encodedUrl);

	/**
	 * retrieve complete Query String from the URL
	 * 
	 * @return
	 */
	String getQueryString();

	/**
	 * Complete URL excluding Query Parameters.
	 * 
	 * @return
	 */
	String getHostBaseURL();

	/**
	 * Request URL protocol
	 * 
	 * @return
	 */
	String getProtocol();

	/**
	 * Request urls base url.
	 * 
	 * @return
	 */
	String getRootHostURL();

	/**
	 * Base url with root context
	 * 
	 * @return
	 */
	String getRootContextURL();

	/**
	 * Get referrer url from header.
	 * 
	 * @return
	 */
	String getReferrer();
}
