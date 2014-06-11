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

import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.google.gwt.user.client.Window.Location;

/**
 * to retrieve common query string parameters.
 * 
 */
public class QueryStringProcessor {

	public static String	DEFAULT_LOCALE	= "en";
	private static String	currentLocale	= null;

	public static String getCurrentLocale() {

		if (currentLocale == null) {
			String selectedLocale = Location.getParameter(QueryString.LOCALE.getKey());
			if (selectedLocale != null && !("".equals(selectedLocale))) {
				currentLocale = selectedLocale;
			} else {
				currentLocale = DEFAULT_LOCALE;
			}
		}
		return currentLocale;
	}
}
