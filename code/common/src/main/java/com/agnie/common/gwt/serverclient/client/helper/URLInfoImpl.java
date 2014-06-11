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

import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.google.gwt.dom.client.Document;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window.Location;

public class URLInfoImpl extends URLInfoBaseImpl {

	@Override
	public String getParameter(String name) {
		return Location.getParameter(name);
	}

	@Override
	public String getHostURL() {
		return Location.getHref();
	}

	@Override
	public Set<String> getParameterKeySet() {
		return (Location.getParameterMap() != null ? Location.getParameterMap().keySet() : null);
	}

	@Override
	public String[] getAllValues(String name) {
		return (Location.getParameterMap() != null ? (Location.getParameterMap().get(name) != null ? Location.getParameterMap().get(name).toArray(new String[0]) : null) : null);
	}

	@Override
	public String getHost() {
		return Location.getHost();
	}

	@Override
	public String getUTF8EncodedURL(String url) {
		return URL.encodeQueryString(url);
	}

	@Override
	public String decodeUTF8URL(String encodedUrl) {
		return URL.decodeQueryString(encodedUrl);
	}

	@Override
	public String getQueryString() {
		String queryString = Location.getQueryString();
		if (queryString != null && !(queryString.isEmpty())) {
			return Location.getQueryString().substring(queryString.indexOf(QueryString.QUESTION_MARK.getKey()) + 1, queryString.length());
		}
		return null;
	}

	@Override
	public String getHostBaseURL() {
		String location = Location.getHref();
		if (location.contains(QueryString.QUESTION_MARK.getKey())) {
			location = location.substring(0, location.indexOf(QueryString.QUESTION_MARK.getKey()));
		} else if (location.contains(QueryString.HASH.getKey())) {
			location = location.substring(0, location.indexOf(QueryString.HASH.getKey()));
		}
		return location;
	}

	@Override
	public String getProtocol() {
		return Location.getProtocol();
	}

	@Override
	public String getReferrer() {
		return Document.get().getReferrer();
	}

}
