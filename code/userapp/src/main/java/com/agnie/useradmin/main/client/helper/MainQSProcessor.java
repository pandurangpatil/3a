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
package com.agnie.useradmin.main.client.helper;

import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.common.gwt.serverclient.client.helper.QueryStringProcessor;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.google.gwt.user.client.Window.Location;

public class MainQSProcessor extends QueryStringProcessor {
	private static String	crntdm	= null;

	public static String getSelDomain() {
		if (crntdm == null) {
			String selectedDomain = Location.getParameter(QueryString.SELECTED_DOMAIN.getKey());
			if (selectedDomain != null && !("".equals(selectedDomain))) {
				crntdm = selectedDomain;
			} else {
				crntdm = UserAdminURLGenerator.USERADMIN;
			}
		}
		return crntdm;
	}

}
