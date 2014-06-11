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
package com.agnie.useradmin.persistance.server.service;

import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.agnie.useradmin.persistance.client.exception.CriticalException;
import com.agnie.useradmin.persistance.client.exception.DomainAuthException;
import com.agnie.useradmin.persistance.client.exception.InvalidDomain;
import com.agnie.useradmin.persistance.client.exception.RegistrationDisabledException;
import com.agnie.useradmin.persistance.client.exception.RequestedException;
import com.agnie.useradmin.persistance.client.exception.UserAdminException;
import com.agnie.useradmin.persistance.client.service.UserProfileService;
import com.agnie.useradmin.persistance.client.service.dto.Credential;
import com.agnie.useradmin.persistance.server.auth.DomainAuthorizer;
import com.agnie.useradmin.persistance.server.auth.DomainContextAuthorizer;
import com.agnie.useradmin.persistance.server.dao.AuthenticateManager;
import com.agnie.useradmin.persistance.server.dao.UserProfileDao;
import com.agnie.useradmin.session.client.helper.UserNotAuthenticated;
import com.agnie.useradmin.session.server.injector.SessionServletModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class UserProfileServiceImpl extends BaseService implements UserProfileService {

	private static final long	serialVersionUID	= 1L;
	@Inject
	@Named(DomainAuthorizer.SELECTED_DOMAIN)
	private Provider<String>	selectedDomain;
	@Inject
	@Named(DomainContextAuthorizer.SELECTED_CONTEXT)
	private Provider<String>	selectedContext;
	@Inject
	private UserProfileDao		updao;
	@Inject
	@Named(SessionServletModule.SESSION_ID)
	private Provider<String>	sessionId;
	@Inject
	AuthenticateManager			am;

	/**
	 * To retrieve logged in users Admin ACL for given domain / application.
	 * 
	 * @return
	 * @throws UserNotAuthenticated
	 */
	@Override
	public AccessControlList getAdminAclForDomain() throws UserNotAuthenticated {
		return updao.getAdminAclForDomain(selectedDomain.get(), sessionId.get());
	}

	/**
	 * To retrieve logged in users Admin ACL for given selected domain and Context.
	 * 
	 * @return
	 * @throws UserNotAuthenticated
	 */
	@Override
	public AccessControlList getAdminAclForContext() throws UserNotAuthenticated {
		return updao.getAdminAclForContext(selectedDomain.get(), selectedContext.get(), sessionId.get());
	}

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
	@Override
	public String validateApplicationAccess(String domain, String sourceUrl) throws DomainAuthException, InvalidDomain, RequestedException, UserNotAuthenticated, RegistrationDisabledException {
		return am.validateApplicationAccess(domain, sourceUrl);
	}

	/**
	 * For existing user to register with new domain.
	 * 
	 * @param cred
	 * @param salt
	 * @return
	 * @throws InvalidDomain
	 */
	public String registerWithNewDomain(Credential cred, String salt) throws UserAdminException, CriticalException {
		return am.registerWithNewDomain(cred, salt);
	}

}
