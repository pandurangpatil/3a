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
package com.agnie.useradmin.session.server.dao;

import java.util.List;

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.useradmin.session.server.entity.UserAuthSession;

public interface UserAuthSessionManager {

	public String createSession(String userId, boolean rememberMe);

	public UserAuthSession getUserAuthBySessionId(String sessionId);

	/**
	 * to retrieve all expired user sessions.
	 * 
	 * @return
	 */
	public List<UserAuthSession> getExpiredUserSessions();

	public void removeExpiredSessions();

	/**
	 * Retrieve all the sessions associated with given User.
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserAuthSession> getUserSessionsByUser(String userId);

	public void updateSession(UserAuthSession uas, UserAccount ua);
}
