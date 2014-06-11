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
package com.agnie.useradmin.persistance.server.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import com.agnie.common.email.Email;
import com.agnie.common.email.MailSender;
import com.agnie.common.email.MessageTemplate;
import com.agnie.common.email.Recipient;
import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.common.helper.SHA256;
import com.agnie.common.injector.CommonModule;
import com.agnie.common.time.DateService;
import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.agnie.useradmin.persistance.client.enums.GeneralStatus;
import com.agnie.useradmin.persistance.client.enums.RequestStatus;
import com.agnie.useradmin.persistance.client.enums.UserStatus;
import com.agnie.useradmin.persistance.client.exception.CriticalException;
import com.agnie.useradmin.persistance.client.exception.DomainAuthException;
import com.agnie.useradmin.persistance.client.exception.InvalidDomain;
import com.agnie.useradmin.persistance.client.exception.InvalidRequestException;
import com.agnie.useradmin.persistance.client.exception.InvalidUserInfoException;
import com.agnie.useradmin.persistance.client.exception.RegistrationDisabledException;
import com.agnie.useradmin.persistance.client.exception.RequestedException;
import com.agnie.useradmin.persistance.client.exception.UserAdminException;
import com.agnie.useradmin.persistance.client.exception.UserAlreadyRegistered;
import com.agnie.useradmin.persistance.client.exception.UserNameExistException;
import com.agnie.useradmin.persistance.client.exception.UserNotFoundException;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.persistance.client.service.dto.Credential;
import com.agnie.useradmin.persistance.client.service.dto.UserInfo;
import com.agnie.useradmin.persistance.server.entity.AdminRole;
import com.agnie.useradmin.persistance.server.entity.Application;
import com.agnie.useradmin.persistance.server.entity.ApplicationRole;
import com.agnie.useradmin.persistance.server.entity.DefaultAppRole;
import com.agnie.useradmin.persistance.server.entity.ForgotPasswordSession;
import com.agnie.useradmin.persistance.server.entity.Role;
import com.agnie.useradmin.persistance.server.entity.User;
import com.agnie.useradmin.persistance.server.entity.UserApplicationRegistration;
import com.agnie.useradmin.persistance.server.injector.CommonUserAdminModule;
import com.agnie.useradmin.session.client.helper.UserNotAuthenticated;
import com.agnie.useradmin.session.server.dao.UserAuthSessionManager;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.persist.Transactional;

/**
 * Don't expose it as request factory Service as all request factory service method calls required authenticated user.
 * Only Authentication GWT RPC service is by passed through authentication check.
 * 
 */
@Singleton
public class AuthenticateManager extends BaseUserAdminService {
	private static org.apache.log4j.Logger	logger	= Logger.getLogger(AuthenticateManager.class);

	@Inject
	@Named(CommonModule.GMAIL_SENDER)
	private MailSender						mailSender;

	@Inject
	@Named(CommonUserAdminModule.VERIFY_ACC_TPL)
	private MessageTemplate					verifyAccmsgTpl;

	@Inject
	@Named(CommonUserAdminModule.FGT_PWD_TPL)
	private MessageTemplate					fgtPwdmsgTpl;

	@Inject
	@Named(CommonUserAdminModule.NEW_REG_REQ_TPL)
	private MessageTemplate					regReqTpl;

	@Inject
	private Provider<URLInfo>				urlinfo;

	@Inject
	private ApplicationManager				am;

	/**
	 * Check if given username is available for use.
	 * 
	 * @param username
	 *            username string.
	 * @return
	 */
	private boolean checkUsernameAvailable(String username) {
		Query qry = uaem.get().createQuery("Select us from User us where us.userName=:userName");
		qry.setParameter("userName", username);
		try {
			return (qry.getSingleResult() == null);
		} catch (NoResultException ex) {
			// its ok if it returns null value
		}
		return true;
	}

	/**
	 * find only ACTIVE user by its username. As this method is only supposed to be used inside Authenticate manager so
	 * it is private.
	 * 
	 * TODO: This one is unresitricted copy implementation from UserManager. Need to identify some way to skip
	 * Authorisation if the method is being called from AuthenticationManager.
	 * 
	 * @param userName
	 *            username of registered user.
	 * @return
	 */
	@Transactional
	private User getPendingUserByUserName(String userName) {
		User us = null;
		if (userName != null) {
			TypedQuery<User> qry = uaem.get().createNamedQuery("User.getByUserName", User.class);
			qry.setParameter("userName", userName);
			qry.setParameter("status", UserStatus.PENDING_FOR_VERIFICATION);
			try {
				us = qry.getSingleResult();
			} catch (NoResultException ex) {
				// its ok if it returns null value
			}
		}
		return us;
	}

	/**
	 * find only ACTIVE user by its username. As this method is only supposed to be used inside Authenticate manager so
	 * it is private.
	 * 
	 * TODO: This one is unresitricted copy implementation from UserManager. Need to identify some way to skip
	 * Authorisation if the method is being called from AuthenticationManager.
	 * 
	 * @param userName
	 *            username of registered user.
	 * @return
	 */
	@Transactional
	private User getActiveUserByUserName(String userName) {
		User us = null;
		if (userName != null) {
			TypedQuery<User> qry = uaem.get().createNamedQuery("User.getByUserName", User.class);
			qry.setParameter("userName", userName);
			qry.setParameter("status", UserStatus.ACTIVE);
			try {
				us = qry.getSingleResult();
			} catch (NoResultException ex) {
				// its ok if it returns null value
			}
		}
		return us;
	}

	/**
	 * get Application entity by Domain Name registered. As this
	 * 
	 * TODO: This one is unresitricted copy implementation from ApplicationManager. Need to identify some way to skip
	 * autherization if the method is being called from AuthenticationManager.
	 * 
	 * @param domain
	 *            domain value of registered application.
	 * @return
	 */
	@Transactional
	Application getApplicationByDomainName(String domain) {
		Application dom = null;
		if (domain != null) {
			TypedQuery<Application> qry = uaem.get().createNamedQuery("Application.getByDomain", Application.class);
			qry.setParameter("domain", domain);
			qry.setParameter("status", GeneralStatus.ACTIVE);
			dom = qry.getSingleResult();
		}
		return dom;
	}

	/**
	 * Authenticate the user by checking its username with password.
	 * 
	 * @param username
	 *            username of registered user.
	 * @param password
	 *            password hash of registered user.
	 * @return
	 */
	@Transactional
	public String authenticate(String username, String password, String salt, boolean remember) {
		if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
			logger.trace("Either username or pssword is empty");
			return null;
		}
		User us = getActiveUserByUserName(username);
		if (us == null) {
			logger.trace("No Active user found with username '" + username + "' ");
			return null;
		}

		if (SHA256.getHashedString(us.getPassword() + salt).equals(password)) {
			DateService ds = injector.getInstance(DateService.class);
			Date dt = ds.getCurrentDate();
			// NOTE: One should not change any other property of user entity here and persist the change. Without
			// approval from manager
			us.setLastLoginTimeStamp(dt);
			// Directly persisting user as user is not logged in yet.
			uaem.get().persist(us);
			UserAuthSessionManager uasm = injector.getInstance(UserAuthSessionManager.class);
			String sessionId = uasm.createSession(us.getId(), remember);
			return sessionId;
		}
		return null;
	}

	/**
	 * To register new user with the system.
	 * 
	 * 
	 * @param us
	 *            User details for registration.
	 * @throws UserAdminException
	 * @throws CriticalException
	 */
	@Transactional(rollbackOn = Exception.class)
	public void signup(UserInfo us, String currentPageUrl) throws UserAdminException, CriticalException {
		User user = signupUser(us);
		/**
		 * send email with email verification link.
		 * <p>
		 * dev => http://<host>:<port>/login.jsp?gwt.codesvr=127.0.0.1:9997#VERIFY:user=<username>&token=<email
		 * token>&runtimetoken=<run time token>
		 * </p>
		 * <p>
		 * prod=> http://<host>:<port>/login.jsp#VERIFY:user=<username>&token=<email token>&runtimetoken=<run time
		 * token>
		 * </p>
		 */
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("user", (us.getFirstName() != null && !us.getFirstName().trim().isEmpty() ? us.getFirstName() : us.getUserName()));
		variables.put("link", currentPageUrl + "#VERIFY:user=" + user.getUserName() + "&token=" + user.getId() + "&runtimetoken=" + user.getRuntTimeToken());
		try {
			mailSender.sendMailAsync(new Email("verifier", new Recipient(user.getEmailId()), "Account verification", verifyAccmsgTpl.getMessage(variables)));
		} catch (AddressException e) {
			logger.error(e);
			throw new CriticalException();
		} catch (FileNotFoundException e) {
			logger.error(e);
			throw new CriticalException();
		} catch (IOException e) {
			logger.error(e);
			throw new CriticalException();
		}
		/*
		 * As user by default gets registered with useradmin.in domain in signupUser() method no need to register it
		 * again.
		 */
		if (us.getCurrentLogingDomain() != null && !(UserAdminURLGenerator.USERADMIN.equals(us.getCurrentLogingDomain()))) {
			registerWithDomain(us.getCurrentLogingDomain(), user);
		}
	}

	/**
	 * by calling this method and passing userid and token sent to users emaild. User's emaild id will be verified and
	 * user's account will be activated.
	 * 
	 * @param userName
	 *            username of registered user
	 * @param token
	 *            token guid sent through email to user's emailid
	 * @param runTimeToken
	 *            runTimetoken
	 * @return
	 * @throws InvalidUserInfoException
	 * @throws CriticalException
	 */
	@Transactional
	public Boolean verifyEmail(String userName, String token, String runTimeToken) throws InvalidUserInfoException, CriticalException {
		boolean resp = false;
		if (userName == null || token == null || "".equals(userName.trim()) || "".equals(token.trim())) {
			throw new InvalidUserInfoException();
		} else {
			User us = getPendingUserByUserName(userName);
			if (us == null) {
				throw new InvalidUserInfoException();
			} else if (us.getEmailVarificationToken().equals(SHA256.getHashedString(token + runTimeToken))) {
				us.setStatus(UserStatus.ACTIVE);
				try {
					// Directly persisting user using entity manager as signup
					// is not suppose to check for permission
					uaem.get().persist(us);
					resp = true;
				} catch (Exception e) {
					logger.error("Critical Error:", e);
					throw new CriticalException();
				}
			}
		}
		return resp;
	}

	/**
	 * Signup method which will signup the user and return email verification code.
	 * 
	 * @param us
	 * @return
	 * @throws InvalidUserInfoException
	 * @throws UserNameExistException
	 * @throws CriticalException
	 */
	@Transactional
	public User signupUser(UserInfo us) throws InvalidUserInfoException, UserNameExistException, CriticalException {
		if (us != null) {
			if (us.getUserName() != null && !(us.getUserName().isEmpty())) {
				if (!checkUsernameAvailable(us.getUserName())) {
					throw new UserNameExistException();
				}
				User user = new User().importDTO(us);
				try {
					String id = java.util.UUID.randomUUID().toString();
					user.setRuntTimeToken(java.util.UUID.randomUUID().toString());
					user.setId(id);
					user.setStatus(UserStatus.PENDING_FOR_VERIFICATION);
					user.setEmailVarificationToken(SHA256.getHashedString(id + user.getRuntTimeToken()));
					// Directly persisting user using entity manager as signup
					// is not suppose to check for permission
					uaem.get().persist(user);
					/*
					 * Every user will by default get registered with useradmin.in domain as a regular user role.
					 */
					registerWithDomain(UserAdminURLGenerator.USERADMIN, user);
					return user;

				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Critical error:", e);
					throw new CriticalException();
				}
			} else {
				throw new InvalidUserInfoException();
			}
		} else {
			throw new InvalidUserInfoException();
		}
	}

	/**
	 * To register given user for given domain
	 * 
	 * @param domain
	 *            domain of a given application
	 * @param user
	 *            User entity
	 * @throws CriticalException
	 * @throws UserAdminException
	 */
	@Transactional
	public String registerWithDomain(String domain, User user) throws CriticalException, UserAdminException {
		String defaultUrl = "";
		if (user != null && !UserStatus.DELETED.equals(user.getStatus())) {

			if (domain != null && !("".equals(domain.trim()))) {
				Application dom = getApplicationByDomainName(domain);
				if (dom != null && GeneralStatus.ACTIVE.equals(dom.getStatus())) {
					DefaultAppRole dfAprole = am.getDefaultApplicationRole(AuthLevel.APPLICATION, domain);
					List<Role> defaultRoles = null;
					if (dfAprole != null)
						defaultRoles = dfAprole.getRoles();

					DefaultAppRole dfAdminApprole = am.getDefaultApplicationRole(AuthLevel.ADMIN_APP, domain);
					List<Role> defaultAdminRoles = null;
					if (dfAdminApprole != null)
						defaultAdminRoles = dfAdminApprole.getRoles();

					UserApplicationRegistration uar = am.getUserRegistration(user.getUserName());
					if (uar == null) {
						uar = new UserApplicationRegistration();
						uar.setApp(dom);
						uar.setUser(user);
						if (RequestStatus.REQUESTED.equals(dom.getDefaultAppStatus())) {
							Map<String, String> variables = new HashMap<String, String>();
							variables.put("domain", dom.getDomain());
							String link = urlinfo.get().getRootContextURL() + "/useradmin.jsp?sel-domain=" + dom.getDomain();
							variables.put("link", link);
							try {
								mailSender.sendMailAsync(new Email("verifier", new Recipient(user.getEmailId()), "New user registered", regReqTpl.getMessage(variables)));
							} catch (Exception e) {
								logger.error(e);
							}
						}
						uar.setStatus(dom.getDefaultAppStatus());
						if (defaultRoles != null && defaultRoles.size() > 0) {
							ApplicationRole appRole = new ApplicationRole();
							appRole.setId(java.util.UUID.randomUUID().toString());
							appRole.setUserAppRegistration(uar);
							appRole.setRoles(defaultRoles);
							uar.setApplicationRole(appRole);
						}

						if (defaultAdminRoles != null && defaultAdminRoles.size() > 0) {
							AdminRole appRole = new AdminRole();
							appRole.setId(java.util.UUID.randomUUID().toString());
							appRole.setUserAppRegistration(uar);
							appRole.setRoles(defaultAdminRoles);
							uar.setAdminRole(appRole);
						}
						// Directly persisting using entity manager as it should be
						// saved without checking the permission.
						uaem.get().persist(uar);
					} else if (!RequestStatus.ACTIVE.equals(uar.getStatus())) {
						throw new UserAlreadyRegistered(uar.getStatus());
					}
					/**
					 * TODO: If domain's default status is other than ACTIVE then send a mail to domain's owner to take
					 * action.
					 */
					if (RequestStatus.ACTIVE.equals(dom.getDefaultAppStatus()) || RequestStatus.PROVISIONAL.equals(dom.getDefaultAppStatus())) {
						defaultUrl = dom.getURL();
					}
				} else {
					throw new InvalidDomain();
				}
			}
		}
		return defaultUrl;
	}

	/**
	 * For existing user to register with new domain.
	 * 
	 * @param cred
	 * @param salt
	 * @return
	 * @throws InvalidDomain
	 * @throws UserAdminException
	 */
	public String registerWithNewDomain(Credential cred, String salt) throws UserAdminException, CriticalException {
		User us = getActiveUserByUserName(cred.getUsername());
		if (us == null) {
			logger.trace("No user found with username '" + cred.getUsername() + "' ");
			return null;
		}

		if (SHA256.getHashedString(us.getPassword() + salt).equals(cred.getPassword())) {
			return registerWithDomain(cred.getDomain(), us);
		}
		return null;
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
	public String validateApplicationAccess(String domain, String sourceUrl) throws DomainAuthException, InvalidDomain, RequestedException, UserNotAuthenticated, RegistrationDisabledException {
		// TODO: We need to add Junit test case for his method.
		String domaintologin = UserAdminURLGenerator.USERADMIN;
		if (domain != null && !(domain.isEmpty())) {
			domaintologin = domain;
			/*
			 * TODO: To add this check we need have a mechanism to specify multiple base url's. As client application
			 * can be deployed in different environment like qa, staging etc. So till the time we don't have that
			 * feature we have to disable source url check. Once that mechanism is added we need to check if source url
			 * has the base url from either of the specified list of base urls, rather than the current check of just
			 * given domain.
			 * 
			 * if (sourceUrl != null && !(sourceUrl.isEmpty())) {
			 * 
			 * // domain = y && srcurl = y if (!sourceUrl.contains(domaintologin)) { if
			 * (!sourceUrl.contains("localhost") && !sourceUrl.contains("127.0.0.1")) { throw new InvalidDomain(); } //
			 * if user is trying to access the application deployed on local machine source url is localhost or //
			 * 127.0.0.1 } } else { // domain = y && srcurl = n }
			 */
		} else if (sourceUrl != null && !(sourceUrl.isEmpty())) {
			// domain = n && srcurl = y
			try {
				URL url = new URL(sourceUrl);
				domaintologin = url.getHost();
			} catch (MalformedURLException e) {
				logger.error(e);
			}
		}
		// else {
		// domain = n && srcurl = n
		// as domaintologin by default initialized with useradmin root domain so commented this combination
		// }
		String homeURL = injector.getInstance(ApplicationManager.class).checkIfUserIsRegistered(domaintologin);
		if (homeURL == null || homeURL.isEmpty())
			return null;
		return ((sourceUrl != null && !(sourceUrl.isEmpty())) ? sourceUrl : homeURL);
	}

	/**
	 * If user forgets his/her login password they can reset it by using Forgot Password link. There user need to enter
	 * his / her username /userid. And click on reset password button. forgot password method will be called by passing
	 * user id to it. This will create a forgotpassword session and send change password link to user's registered email
	 * id by embedding forgot password session token in it.
	 * 
	 * @param username
	 * @return
	 * @throws UserNotFoundException
	 *             thrown when user is not found with given username.
	 */
	@Transactional
	public FgtPwdSession forgotPassword(String username, String existingUrl) throws UserNotFoundException {
		if (username != null) {
			User us = getActiveUserByUserName(username);
			if (us != null) {
				String id = java.util.UUID.randomUUID().toString();
				String runTimeToken = java.util.UUID.randomUUID().toString();
				ForgotPasswordSession fps = new ForgotPasswordSession();
				fps.setId(SHA256.getHashedString(id + runTimeToken));
				fps.setUser(us);
				uaem.get().persist(fps);

				/**
				 * Send change password link through email on user's registered email id.
				 * <p>
				 * dev =>
				 * http://<host>:<port>/login.jsp?gwt.codesvr=127.0.0.1:9997#FGT_CPWD:user=<username>&token=<email
				 * token>&runtimetoken=<run time token>
				 * </p>
				 * <p>
				 * prod=> http://<host>:<port>/userapp/login.jsp#FGT_CPWD:user=<username>&token=<email
				 * token>&runtimetoken=<run time token>
				 * </p>
				 */
				Map<String, String> variables = new HashMap<String, String>();
				variables.put("user", (us.getFirstName() != null && !us.getFirstName().trim().isEmpty() ? us.getFirstName() : us.getUserName()));
				variables.put("link", existingUrl + "#FGT_CPWD:user=" + us.getUserName() + "&token=" + id + "&runtimetoken=" + runTimeToken);
				try {
					mailSender.sendMailAsync(new Email("verifier", new Recipient(us.getEmailId()), "Did you forgot your password?", fgtPwdmsgTpl.getMessage(variables)));
				} catch (AddressException e) {
					logger.error(e);
					throw new CriticalException();
				} catch (FileNotFoundException e) {
					logger.error(e);
					throw new CriticalException();
				} catch (IOException e) {
					logger.error(e);
					throw new CriticalException();
				}

				return new FgtPwdSession(id, runTimeToken);
			} else {
				throw new UserNotFoundException();
			}
		}
		return null;
	}

	/**
	 * When user get change password link after he/she completes the forgot password request. This method will be used
	 * by client application to update user's new password in the system.
	 * 
	 * @param newpassword
	 *            new password that user want to set.
	 * @param username
	 *            username/ userid of the user.
	 * @param authtoken
	 *            change password token issued as a result of forgot password request raised by user.
	 * @param runTimeToken
	 *            Run Time token
	 * @return
	 * @throws UserNotFoundException
	 *             thrown when user is not found with given username.
	 */
	@Transactional
	public Boolean changePassword(String newpassword, String username, String authtoken, String runTimeToken) throws UserNotFoundException, InvalidRequestException {
		boolean response = false;
		if (username != null && newpassword != null && !(newpassword.isEmpty())) {
			User us = getActiveUserByUserName(username);
			if (us != null) {
				ForgotPasswordSession fgs = uaem.get().find(ForgotPasswordSession.class, SHA256.getHashedString(authtoken + runTimeToken));
				if (fgs != null) {
					DateService ds = injector.getInstance(DateService.class);
					Date current = ds.getCurrentDate();
					Calendar cal = Calendar.getInstance();
					cal.setTime(fgs.getCreatedOn());
					cal.add(Calendar.DAY_OF_MONTH, 1);
					Date expire = cal.getTime();
					if (us.equals(fgs.getUser()) && fgs != null && !current.after(expire)) {
						us.setPassword(newpassword);
						uaem.get().persist(us);
						response = true;
						uaem.get().remove(fgs);
					} else {
						throw new InvalidRequestException();
					}
				} else {
					throw new InvalidRequestException();
				}
			} else {
				throw new UserNotFoundException();
			}
		}
		return response;
	}

	public static class FgtPwdSession {
		private String	id;
		private String	runTimeToken;

		public FgtPwdSession() {
			super();
		}

		/**
		 * @param id
		 * @param runTimeToken
		 */
		public FgtPwdSession(String id, String runTimeToken) {
			super();
			this.id = id;
			this.runTimeToken = runTimeToken;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @return the runTimeToken
		 */
		public String getRunTimeToken() {
			return runTimeToken;
		}

		/**
		 * @param runTimeToken
		 *            the runTimeToken to set
		 */
		public void setRunTimeToken(String runTimeToken) {
			this.runTimeToken = runTimeToken;
		}

	}
}
