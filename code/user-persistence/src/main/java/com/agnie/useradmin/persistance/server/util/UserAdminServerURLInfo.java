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
package com.agnie.useradmin.persistance.server.util;

import javax.servlet.http.HttpServletRequest;

import com.agnie.common.helper.ServerURLInfo;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLInfo;
import com.agnie.useradmin.session.server.injector.SessionServletModule;
import com.google.inject.name.Named;

public class UserAdminServerURLInfo extends ServerURLInfo implements UserAdminURLInfo {

	private String	sessionId;

	public UserAdminServerURLInfo(HttpServletRequest request, @Named(SessionServletModule.SESSION_ID) String sessionId) {
		super(request);
		this.sessionId = sessionId;
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

}
