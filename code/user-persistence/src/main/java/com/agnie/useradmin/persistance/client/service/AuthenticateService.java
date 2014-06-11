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

import com.agnie.useradmin.persistance.client.exception.CriticalException;
import com.agnie.useradmin.persistance.client.exception.InvalidRequestException;
import com.agnie.useradmin.persistance.client.exception.InvalidUserInfoException;
import com.agnie.useradmin.persistance.client.exception.UserAdminException;
import com.agnie.useradmin.persistance.client.exception.UserNameExistException;
import com.agnie.useradmin.persistance.client.exception.UserNotFoundException;
import com.agnie.useradmin.persistance.client.service.dto.Application;
import com.agnie.useradmin.persistance.client.service.dto.UserInfo;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("authService")
public interface AuthenticateService extends RemoteService {

	/**
	 * Service to authenticate user by checking passed username and password. If user is authentic user then it will
	 * also start the session by setting session id in cookie.
	 * 
	 * @param username
	 *            registered username of given user.
	 * @param password
	 *            registered password along with given username.
	 * @param salt
	 * @param remember
	 *            flag to indicate user want to remember his session for long.
	 * @return
	 */
	String authenticate(String username, String password, String salt, boolean remember);

	/**
	 * to register new user with the system.
	 * 
	 * @param us
	 *            User Information
	 * @param currentPageUrl
	 *            as it is ajax call, we cannot get all the parameters present in the existing url so, we will pass on
	 *            existing url to backend.
	 * @throws UserNameExistException
	 *             If input username already exist's in to system
	 * @throws UserAdminException
	 */
	void signup(UserInfo us, String currentPageUrl) throws UserNameExistException, CriticalException, UserAdminException;

	/**
	 * When User signup, a link to verify the email id will be sent to user. When User clicks on the link eventually
	 * verifyEmail method will be called to verify the email
	 * 
	 * @param username
	 *            registered username of given user.
	 * @param token
	 *            email token generated to verify given user's email id.
	 * 
	 * @param runTimeToken
	 *            runtimeToken generated
	 * @throws InvalidUserInfoException
	 */
	Boolean verifyEmail(String username, String token, String runTimeToken) throws InvalidUserInfoException, CriticalException;

	/**
	 * When user has forgotten his / her password. He can get an opportunity to change his / her password to new one
	 * through this method. A forgotpassword session is created and an email is sent to user's registered email id with
	 * the forgot password session token.
	 * 
	 * @param username
	 *            registered username of given uesr.
	 * @return
	 * 
	 * @throws UserNotFoundException
	 */
	Boolean forgotPassword(String username, String existingUrl) throws UserNotFoundException;

	/**
	 * Once user clicks on forgot password link. User can set new password. Client application will make use of this
	 * method to set this new password.
	 * 
	 * @param newpassword
	 *            new password that user want to set.
	 * @param username
	 *            registered username for which user want to set the new password
	 * @param authtoken
	 *            authorization token that user has been sent to his / her email id as part forgot password request.
	 * @param runTimeToken
	 *            Run Time token
	 * @return
	 * 
	 * @throws UserNotFoundException
	 * @throws InvalidRequestException
	 */
	Boolean changePassword(String newpassword, String username, String authtoken, String runTimeToken) throws UserNotFoundException, InvalidRequestException;

	/**
	 * Retrieve application details from domain name to check is there any real application registered with given
	 * domain.
	 * 
	 * @param domain
	 *            domain of registered application.
	 * @return
	 */
	Application getApplicationDetails(String domain);
}
