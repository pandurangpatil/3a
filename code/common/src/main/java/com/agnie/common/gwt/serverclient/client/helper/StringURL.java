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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.agnie.common.gwt.serverclient.client.enums.QueryString;

public class StringURL {
	private String						urlStr;

	private String						queryString;

	private String						baseUrl;

	private Map<String, List<String>>	parameterMap	= new HashMap<String, List<String>>();

	public StringURL(String path) {
		this.urlStr = path;
		if (path.contains(QueryString.HASH.getKey())) {
			baseUrl = path.substring(0, path.indexOf(QueryString.HASH.getKey()));
		} else {
			baseUrl = path;
		}
		if (baseUrl.contains(QueryString.QUESTION_MARK.getKey())) {
			// removed all query parameters including "?" so it removes "?gwt.server=127.0.0.1:9997"
			queryString = baseUrl.substring(baseUrl.indexOf(QueryString.QUESTION_MARK.getKey()) + 1, baseUrl.length());
			for (String paramPair : queryString.split(QueryString.AMPERSAND.getKey())) {
				String[] param = paramPair.split("=");
				List<String> values = parameterMap.get(param[0]);
				if (values == null) {
					values = new ArrayList<String>();
					parameterMap.put(param[0], values);
				}
				values.add(param[1]);
			}
			baseUrl = baseUrl.substring(0, baseUrl.indexOf(QueryString.QUESTION_MARK.getKey()));
		}

	}

	public String getParameter(String name) {
		List<String> values = parameterMap.get(name);
		return ((values != null && values.size() > 0) ? values.get(0) : null);
	}

	public String[] getAllValues(String name) {
		List<String> values = parameterMap.get(name);
		return ((values != null) ? values.toArray(new String[0]) : null);
	}

	public String getHostURL() {
		return urlStr;
	}

	public Set<String> getParameterKeySet() {
		return parameterMap.keySet();
	}

	public String getQueryString() {
		return queryString;
	}

	public String getHostBaseURL() {
		return baseUrl;
	}
}
