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
package com.agnie.useradmin.common.client.helper;

import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.common.gwt.serverclient.client.helper.QueryStringProcessor;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window.Location;

public class LoginQSProcessor extends QueryStringProcessor {
	public static String	DEFAULT_SOURCE	= GWT.getHostPageBaseURL() + "useradmin.jsp";
	private static String	crntsrc			= null;
	private static String	crntdm			= null;

	public static String getSource() {
		if (crntsrc == null) {
			String src = Location.getParameter(QueryString.SOURCE.getKey());
			if (src != null && !("".equals(src))) {
				crntsrc = src;
			} else {
				crntsrc = DEFAULT_SOURCE;
			}
		}
		return crntsrc;
	}

	public static String getDomain() {
		if (crntdm == null) {
			String selectedDomain = Location.getParameter(QueryString.DOMAIN.getKey());
			if (selectedDomain != null && !("".equals(selectedDomain))) {
				crntdm = selectedDomain;
			} else {
				crntdm = UserAdminURLGenerator.USERADMIN;
			}
		}
		return crntdm;
	}

	public static String getUser() {
		return Location.getParameter(QueryString.USER_NAME.getKey());
	}

}
