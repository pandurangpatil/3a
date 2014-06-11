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
/**
 * 
 */
package com.agnie.useradmin.persistance.server.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import open.pandurang.gwt.helper.requestfactory.marker.RFService;
import open.pandurang.gwt.helper.requestfactory.marker.RFServiceMethod;

import org.apache.log4j.Logger;

import com.agnie.common.email.Email;
import com.agnie.common.email.MailSender;
import com.agnie.common.email.MessageTemplate;
import com.agnie.common.email.Recipient;
import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.common.gwt.serverclient.client.helper.URLGenerator;
import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.common.injector.CommonModule;
import com.agnie.common.requestfactory.AgnieRFServiceLocator;
import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.agnie.useradmin.persistance.client.enums.GeneralStatus;
import com.agnie.useradmin.persistance.client.enums.RequestStatus;
import com.agnie.useradmin.persistance.client.enums.UserStatus;
import com.agnie.useradmin.persistance.client.exception.CriticalException;
import com.agnie.useradmin.persistance.client.exception.DomainAuthException;
import com.agnie.useradmin.persistance.client.exception.InvalidDomain;
import com.agnie.useradmin.persistance.client.exception.InvalidOperationException;
import com.agnie.useradmin.persistance.client.exception.RegistrationDisabledException;
import com.agnie.useradmin.persistance.client.exception.RequestedException;
import com.agnie.useradmin.persistance.client.exception.UserAdminException;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.persistance.server.auth.DomainAuthorizer;
import com.agnie.useradmin.persistance.server.auth.DomainRistrictedResource;
import com.agnie.useradmin.persistance.server.common.QueryGenerator;
import com.agnie.useradmin.persistance.server.entity.AdminRole;
import com.agnie.useradmin.persistance.server.entity.Application;
import com.agnie.useradmin.persistance.server.entity.ApplicationRole;
import com.agnie.useradmin.persistance.server.entity.DefaultAppRole;
import com.agnie.useradmin.persistance.server.entity.Role;
import com.agnie.useradmin.persistance.server.entity.User;
import com.agnie.useradmin.persistance.server.entity.UserApplicationRegistration;
import com.agnie.useradmin.persistance.server.injector.CommonUserAdminModule;
import com.agnie.useradmin.persistance.server.listrequest.PageNSort;
import com.agnie.useradmin.session.client.helper.UserNotAuthenticated;
import com.agnie.useradmin.session.client.helper.UserNotAutherisedException;
import com.agnie.useradmin.session.server.auth.RequirePermissions;
import com.agnie.useradmin.session.server.auth.RistrictedResource;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.persist.Transactional;

/**
 * To Manager Application entity and also retrieve some entities in relation with given application
 */
@DomainRistrictedResource
@Singleton
@RFService(value = AgnieRFServiceLocator.class)
public class ApplicationManager extends BaseUserAdminService {
	private static org.apache.log4j.Logger	logger	= Logger.getLogger(ApplicationManager.class);
	@Inject
	private SubApplicationManager			subAppMgr;
	@Inject
	@Named(DomainAuthorizer.SELECTED_DOMAIN)
	private Provider<String>				domain;
	@Inject
	@Named(CommonUserAdminModule.REG_STATUS_TPL)
	private MessageTemplate					regStatusTpl;
	@Inject
	private RoleManager						rm;
	@Inject
	@Named(CommonModule.GMAIL_SENDER)
	private MailSender						mailSender;
	@Inject
	private Provider<URLInfo>				urlInfo;
	@Inject
	private URLGenerator					urlGen;

	@Transactional
	UserApplicationRegistration getUserRegistration(String username) {
		TypedQuery<UserApplicationRegistration> newownerqry = uaem.get().createNamedQuery("UserApplicationRegistration.getByUserName", UserApplicationRegistration.class);
		newownerqry.setParameter("username", username);
		newownerqry.setParameter("domain", domain.get());
		try {
			return newownerqry.getSingleResult();

		} catch (NoResultException ex) {
		}
		return null;
	}

	@RFServiceMethod
	@Transactional(rollbackOn = Exception.class)
	@RequirePermissions(permissionExpression = Permissions.APPLICATION_OWNER)
	public void transferOwnerShip(String username) throws UserAdminException {
		UserApplicationRegistration newowner = getUserRegistration(username);
		if (newowner == null) {
			throw new InvalidOperationException();
		}
		if (RequestStatus.ACTIVE.equals(newowner.getStatus())) {
			newowner.setOwner(true);
			uaem.get().persist(newowner);
		} else {
			logger.error("New owner should be the ACTIVE user of the application");
			throw new InvalidOperationException();
		}
		UserApplicationRegistration owner = getUserRegistration(curntUser.get().getUserName());
		owner.setOwner(false);
		uaem.get().persist(owner);
	}

	/**
	 * Create application
	 * 
	 * @param application
	 */
	@RFServiceMethod
	@Transactional
	public Application createApplication(Application application) throws UserNotAutherisedException {
		return subAppMgr.create(application);
	}

	/**
	 * Update general details of application.
	 * 
	 * @param application
	 * @throws InvalidOperationException
	 */
	@RFServiceMethod
	@Transactional(rollbackOn = Exception.class)
	@RequirePermissions(permissionExpression = Permissions.EDIT_APPLICATION_DETAILS)
	public Application updateApplication(Application application) throws UserNotAutherisedException, InvalidOperationException {
		Application app = getApplicationByDomainName(domain.get());
		if (app != null && app.getId().equals(application.getId())) {
			inject(application);
			uaem.get().merge(application);
			return application;
		} else {
			throw new InvalidOperationException();
		}
	}

	/**
	 * Generate new api access key, to access REST api in the context of given application.
	 * 
	 * @param application
	 *            application for which api access key need to be generated.
	 * @return
	 * @throws UserNotAutherisedException
	 * @throws InvalidOperationException
	 */
	@RFServiceMethod
	@Transactional(rollbackOn = Exception.class)
	@RequirePermissions(permissionExpression = Permissions.EDIT_APPLICATION_DETAILS)
	public Application generateNewApiAccessKey(Application application) throws UserNotAutherisedException, InvalidOperationException {
		// TODO: Need to add junits.
		Application app = getApplicationByDomainName(domain.get());
		if (app != null && app.getId().equals(application.getId())) {
			inject(application);
			application.setApiAccessKey(java.util.UUID.randomUUID().toString());
			uaem.get().merge(application);
			return application;
		} else {
			throw new InvalidOperationException();
		}
	}

	/**
	 * Get Application by passing its id
	 * 
	 * @param id
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.APPLICATION)
	public Application getApplicationById(String id) throws UserNotAutherisedException {
		return uaem.get().find(Application.class, id);
	}

	/***
	 * remove given Application. It will change the status of the application to "DELETED"
	 * 
	 * @param application
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.DELETE_APPLICATION)
	public Boolean removeApplication(Application application) throws UserNotAutherisedException {
		application.setStatus(GeneralStatus.DELETED);
		inject(application);
		application.setCurntUser(curntUser.get());
		uaem.get().persist(application);
		return true;
	}

	/**
	 * get Application entity by Domain Name registered. It will return list of only Active application.
	 * 
	 * @param domain
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.APPLICATION)
	public Application getApplicationByDomainName(String domain) throws UserNotAutherisedException {
		Application dom = null;
		if (domain != null) {
			TypedQuery<Application> qry = uaem.get().createNamedQuery("Application.getByDomain", Application.class);
			qry.setParameter("domain", domain);
			qry.setParameter("status", GeneralStatus.ACTIVE);
			try {
				dom = qry.getSingleResult();
			} catch (NoResultException e) {

			}
		}
		return dom;
	}

	/**
	 * Retrieve logged in user's ACTIVE / PROVISIONAL application registration details.
	 * 
	 * @return
	 */
	@Transactional
	private List<UserApplicationRegistration> getUserRegistrations() {
		TypedQuery<UserApplicationRegistration> qry = uaem.get().createNamedQuery("UserApplicationRegistration.getByUser", UserApplicationRegistration.class);
		qry.setParameter("userid", curntUser.get().getId());
		List<UserApplicationRegistration> registrations = qry.getResultList();
		return registrations;
	}

	/**
	 * Retrieve registered applications by given user. User level permission will be checked while accessing user data.
	 * 
	 * @param userId
	 * @return
	 * @throws UserNotAutherisedException
	 */
	@RFServiceMethod
	@Transactional
	public List<Application> getRegisteredApps() throws UserNotAutherisedException {
		/*
		 * Note: This method doesn't need to have permission check done as um.getUserById method call we have agnie
		 * application level permission check done at user level. Logged in user will have access to his / her own
		 * information.
		 */
		List<Application> regApps = new ArrayList<Application>();
		User us = null;
		// uaem.get().getEntityManagerFactory().getCache().evictAll();
		UserManager um = injector.getInstance(UserManager.class);
		if (curntUser.get() != null) {
			us = um.getUserById(curntUser.get().getId());
			if (us != null) {
				List<UserApplicationRegistration> userRegistrations = getUserRegistrations();
				if (userRegistrations != null && userRegistrations.size() > 0) {
					for (UserApplicationRegistration cur : userRegistrations) {
						if (RequestStatus.ACTIVE.equals(cur.getStatus()) || RequestStatus.PROVISIONAL.equals(cur.getStatus())) {
							Application app = cur.getApp();
							// TODO: this has to be done more elgently by defning some flag on application entity and
							// excluding all apps which with that flag set.
							if (GeneralStatus.ACTIVE.equals(app.getStatus()) && !("1".equals(app.getId()))) {
								if (cur.getOwner() || (cur.getAdminRole() != null && cur.getAdminRole().getRoles() != null && cur.getAdminRole().getRoles().size() > 0)) {
									app.setAdmin(true);
								}
								regApps.add(app);
							}
						}
					}
				}
			}
		} else {
			throw new UserNotAutherisedException();
		}
		return regApps;
	}

	/**
	 * To check if logged in user is registered user with given application identified by its domain.
	 * 
	 * @param domaintologin
	 *            web domain of registered application
	 * @return
	 * @throws UserNotAuthenticated
	 *             if there is no logged in user in context.
	 * @throws InvalidDomain
	 *             if domain to login is invalid / not registered with the system.
	 * @throws RequestedException
	 *             In case user is registered with given application but registration status is in Requested state.
	 * @throws DomainAuthException
	 *             if user is registered user of 3a4users but didn't enrolled to targeted domain. In that case this
	 *             exception will be thrown.
	 * @throws RegistrationDisabledException
	 */
	public String checkIfUserIsRegistered(String domaintologin) throws UserNotAuthenticated, InvalidDomain, RequestedException, DomainAuthException, RegistrationDisabledException {
		// TODO: Need to add Junits.
		User us = null;
		UserManager um = injector.getInstance(UserManager.class);
		if (curntUser.get() != null) {
			us = um.getUserById(curntUser.get().getId());
			if (us != null) {
				List<UserApplicationRegistration> userRegistrations = getUserRegistrations();
				if (userRegistrations != null && userRegistrations.size() > 0) {
					for (UserApplicationRegistration cur : userRegistrations) {
						Application app = cur.getApp();
						if (app.getDomain().equalsIgnoreCase(domaintologin)) {
							if (GeneralStatus.ACTIVE.equals(app.getStatus())) {
								if (RequestStatus.ACTIVE.equals(cur.getStatus()) || RequestStatus.PROVISIONAL.equals(cur.getStatus())) {
									return app.getURL();
								} else if (RequestStatus.REQUESTED.equals(cur.getStatus())) {
									throw new RequestedException();
								} else if (RequestStatus.DISABLED.equals(cur.getStatus())) {
									throw new RegistrationDisabledException();
								}
							} else {
								logger.debug("Domain is not ACTIVE'" + domaintologin + "'");
								throw new InvalidDomain();
							}
						}
					}
					throw new DomainAuthException();
				}

			}
		} else {
			throw new UserNotAuthenticated();
		}
		return null;
	}

	@Transactional
	@RequirePermissions(permissionExpression = Permissions.APPLICATION_USER_MANAGER)
	public UserApplicationRegistration getUserApplicationRegistration(String appRegId) {
		UserApplicationRegistration uar = uaem.get().find(UserApplicationRegistration.class, appRegId);
		return uar;
	}

	/**
	 * Retrieve list of users registered with given application excluding the owner of the application.
	 * 
	 * @param pageNSort
	 * @param status
	 * @param search
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.APPLICATION_USER_MANAGER)
	public List<UserApplicationRegistration> getUsersByApplication(PageNSort pageNSort, RequestStatus status, String search) {
		/*
		 * TODO: Improve the performance of this method by making use of MyBatis and Join Query rather than relying on
		 * JPA engine to fire multiple queries to retrieve embedded user object.
		 */
		List<UserApplicationRegistration> list = new ArrayList<UserApplicationRegistration>();
		QueryGenerator qg = new QueryGenerator(uaem.get());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("domain", domain.get());
		params.put("userstatus", UserStatus.ACTIVE);
		if (status != null) {
			params.put("status", status);
		}
		if (search != null && !search.isEmpty()) {
			params.put("search", "%" + search + "%");
		}
		TypedQuery<UserApplicationRegistration> qry = qg.createQuery("byApplication", UserApplicationRegistration.class, pageNSort, params);
		list = qry.getResultList();
		return list;
	}

	/**
	 * Change the user registration status with given application with passed on status except DISABLE status.
	 * 
	 * @param ids
	 *            UserApplicationRegistration ids
	 * @param status
	 *            RequestStatus which is supposed to be updated.
	 * @return
	 */
	@RFServiceMethod
	@Transactional(rollbackOn = Exception.class)
	@RequirePermissions(permissionExpression = Permissions.APPLICATION_USER_REG)
	public List<String> updateStatusByUserAppRegId(List<String> ids, RequestStatus status) {
		if (ids != null && ids.size() > 0 && status != null) {
			if (RequestStatus.DISABLED.equals(status) || RequestStatus.REQUESTED.equals(status)) {
				// TODO: Throw appropriate error stating operation not supported with this method. Need to make
				// different method call to disable.
			} else {
				TypedQuery<User> query = uaem.get().createQuery(
						"select aur.user from UserApplicationRegistration aur  WHERE aur.id IN :ids and aur.app.domain = :domain and aur.owner = 0 and aur.status = :status", User.class);
				query.setParameter("ids", ids);
				query.setParameter("status", RequestStatus.REQUESTED);
				query.setParameter("domain", domain.get());
				List<User> results = query.getResultList();

				TypedQuery<UserApplicationRegistration> qry = uaem.get().createNamedQuery("UserApplicationRegistration.updateStatus", UserApplicationRegistration.class);
				qry.setParameter("ids", ids);
				qry.setParameter("status", status);
				qry.setParameter("domain", domain.get());
				qry.executeUpdate();
				if (results != null) {
					for (User user : results) {
						try {
							Map<String, String> variables = new HashMap<String, String>();
							variables.put("user", (user.getFirstName() != null && !user.getFirstName().trim().isEmpty() ? user.getFirstName() : user.getUserName()));
							variables.put("domain", domain.get());
							variables.put("link", urlGen.getLoginURL(urlInfo.get(), domain.get(), urlInfo.get().getParameter(QueryString.GWT_DEV_MODE.getKey()), true));
							mailSender.sendMailAsync(new Email("verifier", new Recipient(user.getEmailId()), "Registration status updated check your account", regStatusTpl.getMessage(variables)));
						} catch (AddressException e) {
							logger.error(e);
							throw new CriticalException();
						} catch (IOException e) {
							logger.error(e);
							throw new CriticalException();
						}
					}
				}
				return ids;
			}
		}
		return null;
	}

	/**
	 * To update user registration status to "DISABLED" for a given application. DISABLED registrations will be deleted
	 * automatically after pre configured time.
	 * 
	 * @param ids
	 *            UserApplicationRegistration ids.
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.APPLICATION_USER_DEREG)
	public List<String> markDisabledByUserAppRegId(List<String> ids) {
		// TODO: Add Junit
		if (ids != null && ids.size() > 0) {
			TypedQuery<UserApplicationRegistration> qry = uaem.get().createNamedQuery("UserApplicationRegistration.updateStatus", UserApplicationRegistration.class);
			qry.setParameter("ids", ids);
			qry.setParameter("status", RequestStatus.DISABLED);
			qry.setParameter("domain", domain.get());
			qry.executeUpdate();
			return ids;
		}
		return null;
	}

	/**
	 * Retrieve given users roles with registered application.
	 * 
	 * @param uar
	 *            UserApplicationRegistration for given user
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.APPLICATION_USER_ROLE_MANAGER)
	public List<Role> getUserApplicationRoles(UserApplicationRegistration uar) {
		ApplicationRole ar = uar.getApplicationRole();
		if (ar != null) {
			return ar.getRoles();
		}
		return null;
	}

	/**
	 * Update application roles of given user for selected application.
	 * 
	 * @param uar
	 *            UserApplicationRegistration for given user
	 * @param roleIds
	 *            roles to be updated
	 * @return
	 * @throws InvalidOperationException
	 */
	@Transactional
	public UserApplicationRegistration updateUserApplicationRoleIds(UserApplicationRegistration uar, List<String> roleIds) throws InvalidOperationException {
		List<Role> roles = rm.getRolesFromIds(roleIds);
		return updateUserApplicationRoles(uar, roles);
	}

	/**
	 * Update application roles of given user for given application.
	 * 
	 * @param uar
	 *            UserApplicationRegistration for given user
	 * @param roles
	 *            Roles assigned to given user in given application.
	 * @return
	 * @throws InvalidOperationException
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.APPLICATION_USER_ROLE_MANAGER)
	public UserApplicationRegistration updateUserApplicationRoles(UserApplicationRegistration uar, List<Role> roles) throws InvalidOperationException {
		for (Role role : roles) {
			if (!AuthLevel.APPLICATION.equals(role.getLevel())) {
				throw new InvalidOperationException();
			}
		}
		ApplicationRole ar = uar.getApplicationRole();
		if (ar == null) {
			ar = new ApplicationRole();
			ar.setUserAppRegistration(uar);
			uar.setApplicationRole(ar);
		}
		if (roles != null && roles.size() > 0) {
			ar.setRoles(roles);
		} else {
			ar.getRoles().clear();
		}
		inject(ar);
		uaem.get().persist(ar);
		return uar;
	}

	/**
	 * Retrieve given users admin roles with registered application.
	 * 
	 * @param uar
	 *            UserApplicationRegistration for given user
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.APPLICATION_USER_ADMIN_ROLE_MANAGER)
	public List<Role> getUserApplicationAdminRoles(UserApplicationRegistration uar) {
		AdminRole ar = uar.getAdminRole();
		if (ar != null) {
			return ar.getRoles();
		}
		return null;
	}

	/**
	 * Update admin roles of given user for selected application.
	 * 
	 * @param uar
	 *            UserApplicationRegistration for given user
	 * @param roleIds
	 *            Roles assigned to given user in given application.
	 * @return
	 * @throws InvalidOperationException
	 */
	@Transactional
	public UserApplicationRegistration updateUserApplicationAdminRoleIds(UserApplicationRegistration uar, List<String> roleIds) throws InvalidOperationException {
		List<Role> roles = rm.getRolesFromIds(roleIds);
		return updateUserApplicationAdminRoles(uar, roles);
	}

	/**
	 * Update admin roles of given user for given application.
	 * 
	 * @param uar
	 *            UserApplicationRegistration for given user
	 * @param roles
	 *            Roles assigned to given user in given application.
	 * @return
	 * @throws InvalidOperationException
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.APPLICATION_USER_ADMIN_ROLE_MANAGER)
	public UserApplicationRegistration updateUserApplicationAdminRoles(UserApplicationRegistration uar, List<Role> roles) throws InvalidOperationException {
		for (Role role : roles) {
			if (!AuthLevel.ADMIN_APP.equals(role.getLevel())) {
				throw new InvalidOperationException();
			}
		}
		AdminRole ar = uar.getAdminRole();
		if (ar == null) {
			ar = new AdminRole();
			ar.setUserAppRegistration(uar);
			uar.setAdminRole(ar);
		}
		if (roles != null && roles.size() > 0) {
			ar.setRoles(roles);
		}
		inject(ar);
		uaem.get().persist(ar);
		return uar;
	}

	@Transactional
	public DefaultAppRole getDefaultApplicationRole(AuthLevel level, String domain) {
		TypedQuery<DefaultAppRole> qry = uaem.get().createNamedQuery("DefaultAppRole.getByDomainAndLevel", DefaultAppRole.class);
		qry.setParameter("domain", domain);
		qry.setParameter("level", level);
		DefaultAppRole dar = null;
		try {
			dar = qry.getSingleResult();
		} catch (NoResultException ex) {
			// its ok if it returns null value
		}
		return dar;
	}

	/**
	 * update Default application roles
	 * 
	 * @param roles
	 * @throws InvalidOperationException
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.EDIT_APPLICATION_DETAILS)
	public void updateDefaultAppRoles(List<Role> roles) throws UserNotAutherisedException, InvalidOperationException {
		// TODO: need to add junit
		for (Role role : roles) {
			if (!AuthLevel.APPLICATION.equals(role.getLevel())) {
				throw new InvalidOperationException();
			}
		}
		Application app = getApplicationByDomainName(domain.get());
		DefaultAppRole dar = getDefaultApplicationRole(AuthLevel.APPLICATION, domain.get());
		if (dar == null) {
			dar = new DefaultAppRole();
			dar.setApplication(app);
			dar.setLevel(AuthLevel.APPLICATION);
		}
		dar.setRoles(roles);
		inject(dar);
		uaem.get().persist(dar);
	}

	/**
	 * update Default application context roles
	 * 
	 * @param roles
	 * @throws InvalidOperationException
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.EDIT_APPLICATION_DETAILS)
	public void updateDefaultAppCtxRoles(List<Role> roles) throws UserNotAutherisedException, InvalidOperationException {
		// TODO: need to add junit
		for (Role role : roles) {
			if (!AuthLevel.CONTEXT.equals(role.getLevel())) {
				throw new InvalidOperationException();
			}
		}
		Application app = getApplicationByDomainName(domain.get());
		DefaultAppRole dacr = getDefaultApplicationRole(AuthLevel.CONTEXT, domain.get());
		if (dacr == null) {
			dacr = new DefaultAppRole();
			dacr.setApplication(app);
			dacr.setLevel(AuthLevel.CONTEXT);
		}
		dacr.setRoles(roles);
		inject(dacr);
		uaem.get().persist(dacr);
	}

	/**
	 * Retrieve list of default application roles for given selected application.
	 * 
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.APPLICATION)
	public List<Role> getDefaultAppRoles() {
		// TODO: need to add junit
		DefaultAppRole dar = getDefaultApplicationRole(AuthLevel.APPLICATION, domain.get());
		if (dar != null)
			return dar.getRoles();

		return null;
	}

	/**
	 * Retrieve list of default context roles for given selected application.
	 * 
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.APPLICATION)
	public List<Role> getDefaultAppCtxRoles() {
		// TODO: need to add junit
		DefaultAppRole dacr = getDefaultApplicationRole(AuthLevel.CONTEXT, domain.get());
		if (dacr != null)
			return dacr.getRoles();

		return null;
	}

	/**
	 * Retrieve paginated list of admin app level roles.
	 * 
	 * @param pageNSort
	 *            pagination and sorting input
	 * @param search
	 *            search key
	 * @return
	 */
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.APPLICATION_USER_ADMIN_ROLE_MANAGER)
	public List<Role> getAdminRoles(PageNSort pageNSort, String search) {
		return rm.getRoles(pageNSort, AuthLevel.ADMIN_APP, UserAdminURLGenerator.USERADMIN, search);
	}

	/**
	 * Retrieve application level Admin roles.
	 * 
	 * @return
	 * @throws UserNotAutherisedException
	 */
	@Transactional
	@RFServiceMethod
	public List<Role> getAdminRoles() {
		return getAdminRoles(null, null);
	}

	@Transactional
	@RFServiceMethod
	public boolean isAvilable(String domain) {
		return subAppMgr.isAvilable(domain);
	}

}

/**
 * The reason sub class is created as create method requires application level ACL check than the domain level ACL
 * check.
 * 
 */
@RistrictedResource
@Singleton
class SubApplicationManager extends BaseUserAdminService {

	/**
	 * create Application.
	 * 
	 * @param application
	 */
	@Transactional(rollbackOn = Exception.class)
	@RequirePermissions(permissionExpression = Permissions.CREATE_APPLICATION)
	public Application create(Application application) throws UserNotAutherisedException {

		if (application.getId() != null) {
			List<String> perms = new ArrayList<String>();
			perms.add(Permissions.CREATE_APPLICATION);
			throw new UserNotAutherisedException(perms);
		}
		if (!isAvilable(application.getDomain())) {
			throw new RuntimeException("Domain is not available '" + application.getDomain() + "' ");
		}
		UserManager um = injector.getInstance(UserManager.class);
		User us = um.getUserById(curntUser.get().getId());
		application.setApiAccessKey(java.util.UUID.randomUUID().toString());
		inject(application);
		application.setCurntUser(curntUser.get());
		uaem.get().persist(application);
		uaem.get().persist(application);

		UserApplicationRegistration uar = new UserApplicationRegistration();
		uar.setApp(application);
		uar.setUser(us);
		uar.setStatus(RequestStatus.ACTIVE);
		uar.setOwner(true);
		uaem.get().persist(uar);
		return application;
	}

	@Transactional
	public boolean isAvilable(String domain) {
		boolean avilable = true;
		if (domain != null) {
			TypedQuery<Application> qry = uaem.get().createNamedQuery("Application.getByDomainAnyStatus", Application.class);
			qry.setParameter("domain", domain);
			try {
				Application dom = qry.getSingleResult();
				if (dom != null && dom.getId() != null && !dom.getId().isEmpty()) {
					avilable = false;
				}
			} catch (NoResultException e) {

			}
		}
		return avilable;
	}
}
