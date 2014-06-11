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
package com.agnie.useradmin.persistance.client.service;

import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.agnie.useradmin.persistance.client.exception.CriticalException;
import com.agnie.useradmin.persistance.client.exception.DomainAuthException;
import com.agnie.useradmin.persistance.client.exception.InvalidDomain;
import com.agnie.useradmin.persistance.client.exception.RegistrationDisabledException;
import com.agnie.useradmin.persistance.client.exception.RequestedException;
import com.agnie.useradmin.persistance.client.exception.UserAdminException;
import com.agnie.useradmin.persistance.client.service.dto.Credential;
import com.agnie.useradmin.session.client.helper.UserNotAuthenticated;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("profileService")
public interface UserProfileService extends RemoteService {
	/**
	 * This method assumes user is already logged in and session is established. This will validate whether logged in
	 * user has access to given Source url ultimately given domain
	 * 
	 * @param domain
	 *            application domain name to which user is interested to get logged in to.
	 * @param sourceUrl
	 *            redirect url passed by application who had redirected user to login to user admin
	 * @return
	 * @throws DomainAuthException
	 *             In case user is not registered with given domain/application
	 * @throws InvalidDomain
	 *             In case passed domain/application is not registered with useradmin.in.
	 * @throws RequestedException
	 *             In case user has raised a request to register with respective domain/application but request is not
	 *             yet approved.
	 * @throws UserNotAuthenticated
	 *             In case somehow this method is called with out establishment of session, rather before user is
	 *             authenticated
	 * @throws RegistrationDisabledException
	 */
	String validateApplicationAccess(String domain, String sourceUrl) throws DomainAuthException, InvalidDomain, RequestedException, UserNotAuthenticated, RegistrationDisabledException;

	/**
	 * To retrieve logged in users Admin ACL for given domain / application.
	 * 
	 * @return
	 * @throws UserNotAuthenticated
	 */
	AccessControlList getAdminAclForDomain() throws UserNotAuthenticated;

	/**
	 * To retrieve logged in users Admin ACL for given selected domain and Context.
	 * 
	 * @return
	 * @throws UserNotAuthenticated
	 */
	AccessControlList getAdminAclForContext() throws UserNotAuthenticated;

	/**
	 * For existing user to register with new domain.
	 * 
	 * @param cred
	 * @param salt
	 * @return
	 * @throws UserAdminException
	 */
	String registerWithNewDomain(Credential cred, String salt) throws UserAdminException, CriticalException;
}
