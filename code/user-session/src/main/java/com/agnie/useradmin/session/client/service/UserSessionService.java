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
package com.agnie.useradmin.session.client.service;

import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.useradmin.session.client.helper.UserNotAuthenticated;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("authUserService")
public interface UserSessionService extends RemoteService {
	/**
	 * To retrieve logged in user details
	 * 
	 * @return
	 * @throws UserNotAuthenticated
	 */
	UserAccount getLoggedInUserAccount() throws UserNotAuthenticated;

	/**
	 * To retrieve logged in users ACL for given domain / application.
	 * 
	 * @return
	 * @throws UserNotAuthenticated
	 */
	AccessControlList getAclForDomain() throws UserNotAuthenticated;

	/**
	 * To retrieve logged in user's ACL for given domain and context.
	 * 
	 * @param context
	 *            selected context
	 * @return
	 * @throws UserNotAuthenticated
	 */
	AccessControlList getAclForContext(String context) throws UserNotAuthenticated;

	/**
	 * This api will logout currently logged in user.
	 */
	void logout();
}
