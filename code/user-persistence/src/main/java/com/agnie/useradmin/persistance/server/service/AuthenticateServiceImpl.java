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

import com.agnie.common.gwt.serverclient.client.helper.URLGenerator;
import com.agnie.useradmin.persistance.client.exception.CriticalException;
import com.agnie.useradmin.persistance.client.exception.InvalidCaptcha;
import com.agnie.useradmin.persistance.client.exception.InvalidRequestException;
import com.agnie.useradmin.persistance.client.exception.InvalidUserInfoException;
import com.agnie.useradmin.persistance.client.exception.UserAdminException;
import com.agnie.useradmin.persistance.client.exception.UserNameExistException;
import com.agnie.useradmin.persistance.client.exception.UserNotFoundException;
import com.agnie.useradmin.persistance.client.service.AuthenticateService;
import com.agnie.useradmin.persistance.client.service.dto.Application;
import com.agnie.useradmin.persistance.client.service.dto.UserInfo;
import com.agnie.useradmin.persistance.server.dao.AuthenticateManager;
import com.agnie.useradmin.persistance.server.dao.AuthenticateManager.FgtPwdSession;
import com.agnie.useradmin.persistance.server.mybatis.mapper.ApplicationMapper;
import com.agnie.useradmin.session.server.injector.AgnieSessionFilter;
import com.agnie.useradmin.session.server.injector.SessionCommonModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.sfeir.captcha.server.CaptchaValidator;

@Singleton
public class AuthenticateServiceImpl extends BaseService implements AuthenticateService {
	@Inject
	@Named(SessionCommonModule.REMEMBER_EXP_TIME)
	private int								rememberExpTime;
	@Inject
	@Named(SessionCommonModule.DEFAULT_EXP_TIME)
	private int								defaultExpTime;
	@Inject
	@Named(URLGenerator.RECAPTCHA_PRIVATE_KEY)
	private String							recapPrivateKey;

	private static final long				serialVersionUID	= 1L;

	@Inject
	AuthenticateManager						am;

	@Inject
	AgnieSessionFilter						asf;

	@Inject
	protected Provider<ApplicationMapper>	appMapper;

	/**
	 * Authenticate the user by checking its username with password.
	 * 
	 * @param username
	 *            username of registered user.
	 * @param password
	 *            password hash of registered user.
	 * @param salt
	 * @param remember
	 *            flag to indicate user want to remember his session for long.
	 * @return
	 */
	@Override
	public String authenticate(String username, String password, String salt, boolean remember) {
		String session = am.authenticate(username, password, salt, remember);
		if (session != null) {
			String cookSession = session;
			if (this.getThreadLocalResponse() != null) {
				int exp = -1;
				if (remember) {
					exp = rememberExpTime;
				} else {
					exp = defaultExpTime;
				}
				asf.setSessionCookie(exp, this.getThreadLocalRequest().getServerName(), cookSession, this.getThreadLocalResponse(), remember);
			}
			return session;
		}
		return null;
	}

	@Override
	public void signup(UserInfo us, String currentPageUrl) throws UserNameExistException, CriticalException, UserAdminException {
		CaptchaValidator validator = new CaptchaValidator(recapPrivateKey);

		if (!validator.validateCaptcha(us.getCaptchaAns(), getThreadLocalRequest().getRemoteAddr())) {
			throw new InvalidCaptcha();
		}
		am.signup(us, currentPageUrl);
	}

	@Override
	public Boolean verifyEmail(String username, String token, String runTimeToken) throws InvalidUserInfoException, CriticalException {
		// TODO: Integrate captha.
		return am.verifyEmail(username, token, runTimeToken);
	}

	@Override
	public Boolean forgotPassword(String username, String existingUrl) throws UserNotFoundException {
		FgtPwdSession session = am.forgotPassword(username, existingUrl);
		return session != null;
	}

	@Override
	public Boolean changePassword(String newpassword, String username, String authtoken, String runTimeToken) throws UserNotFoundException, InvalidRequestException {
		return am.changePassword(newpassword, username, authtoken, runTimeToken);
	}

	@Override
	public Application getApplicationDetails(String domain) {
		if (domain != null && !domain.isEmpty()) {
			return appMapper.get().getApplicationDetails(domain);
		}
		return null;
	}

}
