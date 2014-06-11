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
package com.agnie.useradmin.persistance.client.helper;

import java.util.Set;

import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.common.gwt.serverclient.client.helper.StringURL;
import com.agnie.common.gwt.serverclient.client.helper.URLGenerator;
import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.google.gwt.core.client.GWT;
import com.google.inject.Singleton;

@Singleton
public class UserAdminURLGenerator extends URLGenerator {

	public UserAdminURLGenerator() {

	}

	/*
	 * TODO Initialize through maven build variables.
	 */

	public static final String	USERADMIN_JSP	= "useradmin.jsp";

	/**
	 * Retrieve login URL to use in 3a4user module.
	 * 
	 * @param params
	 * @return Login URL
	 */
	public String getClientSideLoginURL(URLInfo params) {
		return getClientSideLoginURL(params, USERADMIN, params.getParameter(QueryString.GWT_DEV_MODE.getKey()));
	}

	/**
	 * Get redirect url after user is logged in. Source of base url will be dependent upon "source" and domain query
	 * parameters.
	 * 
	 * @param params
	 * @return
	 */
	public String getAfterLoginRedirectUrl(UserAdminURLInfo params) {
		return getAfterLoginRedirectUrl(params, null, null);
	}

	/**
	 * Get redirect url after it is confirmed user is logged in. Source of base url will be dependent upon "source" and
	 * domain query parameters, and homeURL.
	 * 
	 * @param params
	 * @param domain
	 *            domain of the application to which user is trying to login
	 * @param homeURL
	 *            home url of the application.
	 * @return
	 */

	public String getAfterLoginRedirectUrl(UserAdminURLInfo params, String domain, String homeURL) {
		StringBuffer url = new StringBuffer();
		String location = params.getHostURL();
		if (location.contains(QueryString.QUESTION_MARK.getKey())) {
			location = location.substring(0, location.indexOf(QueryString.QUESTION_MARK.getKey()));
		}
		location = location.substring(0, location.lastIndexOf("/"));
		boolean qpexists = false;
		String param = params.getParameter(QueryString.SOURCE.getKey());
		// source url not present then check if homeURL is passed on. If homeURL is not passed (which can happen only in
		// case this method is invoked from client code) without having any source url. And it can happen only if user
		// hit to login page directly. In such a case we assume that user wants to login to 3a4users module. So it will
		// redirected to landing page of 3a4users module from login page of 3a4users module.
		if (param == null || param.isEmpty()) {
			if ((homeURL == null || homeURL.isEmpty()) && (domain == null || USERADMIN.equals(domain))) {
				param = location + "/" + LANDING_PAGE;
			} else if ((homeURL == null || homeURL.isEmpty()) && domain != null && !(USERADMIN.equals(domain))) {
				throw new IllegalArgumentException("If source url is not present and domain is other than useradmin.in then home url is required paramter");
			} else {
				param = homeURL;
			}
			// This part of code will add GWT dev mode and locale parameters to home url if domain is useradmin.in or it
			// is null
			if (domain == null || USERADMIN.equals(domain)) {
				qpexists = param.contains(QueryString.QUESTION_MARK.getKey());
				String param1 = params.getParameter(QueryString.GWT_DEV_MODE.getKey());
				if (param1 != null && !("".equals(param1.trim()))) {
					if (!qpexists) {
						param += QueryString.QUESTION_MARK.getKey();
						qpexists = true;
					} else {
						param += QueryString.AMPERSAND.getKey();
					}
					param += QueryString.GWT_DEV_MODE.getKey() + "=" + param1;
				}
			}
		} else {
			param = params.decodeUTF8URL(param);
			param = getSourceUrl(param);
		}
		// Here its assumed that source parameter has encoded url value. So before using it we are decoding it
		url.append(param);
		qpexists = param.contains(QueryString.QUESTION_MARK.getKey());
		String param1 = params.getParameter(QueryString.LOCALE.getKey());
		if (param1 != null && !(param1.isEmpty())) {
			if (!qpexists) {
				url.append(QueryString.QUESTION_MARK.getKey());
				qpexists = true;
			} else {
				url.append(QueryString.AMPERSAND.getKey());
			}
			url.append(QueryString.LOCALE.getKey() + "=" + param1);
		}
		String sessionid = params.getSessionId();
		if (sessionid != null && !(sessionid.isEmpty())) {
			if (!qpexists) {
				url.append(QueryString.QUESTION_MARK.getKey());
				qpexists = true;
			} else {
				url.append(QueryString.AMPERSAND.getKey());
			}
			url.append(QueryString.SESSION.getKey() + "=" + sessionid);
		}
		// referrer url returned by getParmeter will be the value of query parameter added by getLoginURL method. This
		// one may different from referrer header.
		String referrer = params.getParameter(QueryString.REFERER.getKey());
		if (referrer != null && !(referrer.isEmpty())) {
			if (!qpexists) {
				url.append(QueryString.QUESTION_MARK.getKey());
				qpexists = true;
			} else {
				url.append(QueryString.AMPERSAND.getKey());
			}
			url.append(QueryString.SOURCE.getKey() + "=" + referrer);
		}
		String hash = params.getParameter(QueryString.HISTORY_HASH.getKey());
		if (hash != null && !hash.isEmpty()) {
			url.append(QueryString.HASH.getKey() + params.decodeUTF8URL(hash));
		}
		return url.toString();
	}

	/**
	 * Remove locale and session id query parameters from source url.
	 * 
	 * @param src
	 * @return
	 */
	private String getSourceUrl(String src) {
		StringURL sourceUrl = new StringURL(src);
		StringBuffer source = new StringBuffer();
		source.append(sourceUrl.getHostBaseURL());
		boolean first = true;
		for (String key : sourceUrl.getParameterKeySet()) {
			// skip the query parameters 1.locale 2.sessionid
			if (!key.equals(QueryString.LOCALE.getKey()) && !key.equals(QueryString.SESSION.getKey())) {
				if (first) {
					first = false;
					source.append(QueryString.QUESTION_MARK.getKey());
				} else {
					source.append(QueryString.AMPERSAND.getKey());
				}
				source.append(key + "=" + sourceUrl.getParameter(key));
			}
		}
		return source.toString();
	}

	/**
	 * To change Sel-Domain from qString with new value.
	 * 
	 * @param newDomain
	 * @return
	 */
	public String editSelDomain(String newDomain, UserAdminURLInfo params) {
		String location = params.getHostURL();
		String QPHashUrl = params.getQueryString();
		String baseUrl;
		StringBuffer newUrl = new StringBuffer();

		if (location != null) {
			StringBuffer hashlink = new StringBuffer();
			StringBuffer qString = new StringBuffer();
			boolean fex = true;

			if (location.contains(QueryString.QUESTION_MARK.getKey())) {
				baseUrl = location.substring(0, location.indexOf(QueryString.QUESTION_MARK.getKey()));
			}
			baseUrl = location.substring(0, location.lastIndexOf("/"));

			if (QPHashUrl != null) {
				String QPHashUrlStr = QPHashUrl.toString();
				if (QPHashUrlStr.contains(QueryString.HASH.getKey())) {
					String[] qpHashSplit = QPHashUrlStr.split("\\#");
					hashlink.append(QueryString.HASH.getKey() + qpHashSplit[1]);
				}
			}

			Set<String> qParmKeys = params.getParameterKeySet();
			for (String qPara : qParmKeys) {
				if (qPara.equals(QueryString.SELECTED_DOMAIN.getKey())) {
					if (fex) {
						qString.append(QueryString.SELECTED_DOMAIN.getKey() + "=" + newDomain);
						fex = false;
					} else {
						qString.append(QueryString.AMPERSAND.getKey());
						qString.append(QueryString.SELECTED_DOMAIN.getKey() + "=" + newDomain);
					}
				} else {
					if (fex) {
						qString.append(qPara + "=" + params.getParameter(qPara));
						fex = false;
					} else {
						qString.append(QueryString.AMPERSAND.getKey());
						qString.append(qPara + "=" + params.getParameter(qPara));
					}
				}
			}
			newUrl.append(baseUrl);
			newUrl.append("/");
			newUrl.append(USERADMIN_JSP);
			if (QPHashUrl != null) {
				newUrl.append(QueryString.QUESTION_MARK.getKey() + qString);
			}
			if ((QPHashUrl.toString()).contains(QueryString.HASH.getKey())) {
				newUrl.append(hashlink);
			}

		} else {
			GWT.log("In UserAdminUrlGenerator editSelDomain() url==null");
		}
		return newUrl.toString();
	}

	/**
	 * Get ApplicationManageUrl for ApplicationWidget 'manage'command.
	 * 
	 * @param domain
	 * @return
	 */
	public String getAppManageUrl(String domain, UserAdminURLInfo params) {
		String location = params.getHostURL();
		StringBuffer newUrl = new StringBuffer();

		if (location != null) {
			StringBuffer qString = new StringBuffer();
			String baseUrl;
			if (location.contains(QueryString.QUESTION_MARK.getKey())) {
				baseUrl = location.substring(0, location.indexOf(QueryString.QUESTION_MARK.getKey()));
			}
			baseUrl = location.substring(0, location.lastIndexOf("/"));
			boolean fex = true;
			if (domain != null && !(domain.isEmpty())) {// if sel-domain absent in url
				if (fex) {
					qString.append(QueryString.SELECTED_DOMAIN.getKey() + "=" + domain);
					fex = false;
				} else {
					qString.append(QueryString.AMPERSAND.getKey());
					qString.append(QueryString.SELECTED_DOMAIN.getKey() + "=" + domain);
				}
			}
			String param = params.getParameter(QueryString.LOCALE.getKey());
			if (param != null && !param.isEmpty()) {
				if (fex) {
					qString.append(QueryString.LOCALE.getKey() + "=" + param);
					fex = false;
				} else {
					qString.append(QueryString.AMPERSAND.getKey());
					qString.append(QueryString.LOCALE.getKey() + "=" + param);
				}
			}
			param = params.getParameter(QueryString.GWT_DEV_MODE.getKey());
			if (param != null && !param.isEmpty()) {
				if (fex) {
					qString.append(QueryString.GWT_DEV_MODE.getKey() + "=" + param);
					fex = false;
				} else {
					qString.append(QueryString.AMPERSAND.getKey());
					qString.append(QueryString.GWT_DEV_MODE.getKey() + "=" + param);
				}
			}
			String sessionId = params.getSessionId();
			if (sessionId != null && !sessionId.isEmpty()) {
				if (fex) {
					qString.append(QueryString.SESSION.getKey() + "=" + sessionId);
					fex = false;
				} else {
					qString.append(QueryString.AMPERSAND.getKey());
					qString.append(QueryString.SESSION.getKey() + "=" + sessionId);
				}
			}
			newUrl.append(baseUrl);
			newUrl.append("/");
			newUrl.append(USERADMIN_JSP);
			if (!qString.toString().isEmpty()) {
				newUrl.append(QueryString.QUESTION_MARK.getKey() + qString);
			}
		} else {
			GWT.log("In UserAdminUrlGenerator getAppManageUrl() url==null");
		}
		return newUrl.toString();
	}

	/**
	 * Retrieve application url by adding seesionId as query parameter value to the url.
	 * 
	 * @param domUrl
	 * @param params
	 * @return
	 */
	public String getApplicationUrl(String domUrl, UserAdminURLInfo params) {
		String sessionId = params.getSessionId();
		StringBuffer location = new StringBuffer(domUrl);
		String hashLink = "";
		if (domUrl.contains(QueryString.HASH.getKey())) {
			String[] qpHashSplit = domUrl.split("\\#");
			location = new StringBuffer(qpHashSplit[0]);
			hashLink = QueryString.HASH.getKey() + qpHashSplit[1];
		}
		if (location.toString().contains(QueryString.QUESTION_MARK.getKey())) {
			location.append(QueryString.AMPERSAND.getKey());
		} else {
			location.append(QueryString.QUESTION_MARK.getKey());
		}
		location.append(QueryString.SESSION.getKey() + "=" + sessionId + hashLink);
		return location.toString();
	}
}
