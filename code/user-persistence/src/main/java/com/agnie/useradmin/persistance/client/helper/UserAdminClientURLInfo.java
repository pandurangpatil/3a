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

import com.agnie.common.gwt.serverclient.client.enums.Cokie;
import com.agnie.common.gwt.serverclient.client.helper.URLInfoImpl;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Singleton;

@Singleton
public class UserAdminClientURLInfo extends URLInfoImpl implements UserAdminURLInfo {

	public String getSessionId() {
		return Cookies.getCookie(Cokie.AUTH.getKey());
	}

}
