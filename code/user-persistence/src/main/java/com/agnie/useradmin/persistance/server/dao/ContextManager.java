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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import open.pandurang.gwt.helper.requestfactory.marker.RFService;
import open.pandurang.gwt.helper.requestfactory.marker.RFServiceMethod;

import org.apache.log4j.Logger;

import com.agnie.common.requestfactory.AgnieRFServiceLocator;
import com.agnie.common.util.client.validator.ConstraintRegularExpressions;
import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.agnie.useradmin.persistance.client.enums.GeneralStatus;
import com.agnie.useradmin.persistance.client.enums.RequestStatus;
import com.agnie.useradmin.persistance.client.enums.UserStatus;
import com.agnie.useradmin.persistance.client.exception.ApplicationUserRegistrationNotActive;
import com.agnie.useradmin.persistance.client.exception.InvalidDomain;
import com.agnie.useradmin.persistance.client.exception.InvalidOperationException;
import com.agnie.useradmin.persistance.client.exception.UserAdminException;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.persistance.server.auth.DomainAuthorizer;
import com.agnie.useradmin.persistance.server.auth.DomainContextAuthorizer;
import com.agnie.useradmin.persistance.server.auth.DomainContextRistrictedResource;
import com.agnie.useradmin.persistance.server.auth.DomainRistrictedResource;
import com.agnie.useradmin.persistance.server.common.QueryGenerator;
import com.agnie.useradmin.persistance.server.entity.AdminContextRole;
import com.agnie.useradmin.persistance.server.entity.Application;
import com.agnie.useradmin.persistance.server.entity.Context;
import com.agnie.useradmin.persistance.server.entity.ContextRole;
import com.agnie.useradmin.persistance.server.entity.Role;
import com.agnie.useradmin.persistance.server.entity.User;
import com.agnie.useradmin.persistance.server.entity.UserApplicationCtxRegistration;
import com.agnie.useradmin.persistance.server.listrequest.PageNSort;
import com.agnie.useradmin.session.client.helper.UserNotAutherisedException;
import com.agnie.useradmin.session.server.auth.RequirePermissions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.persist.Transactional;

/**
 * To manage Contexts of an application.
 * 
 */
@DomainContextRistrictedResource
@RFService(value = AgnieRFServiceLocator.class)
public class ContextManager extends BaseUserAdminService {
	private static org.apache.log4j.Logger	logger	= Logger.getLogger(ContextManager.class);
	@Inject
	private SubContextManager				scm;

	// selected domain
	@Inject
	@Named(DomainAuthorizer.SELECTED_DOMAIN)
	private Provider<String>				domain;

	@Inject
	@Named(DomainContextAuthorizer.SELECTED_CONTEXT)
	private Provider<String>				context;

	@Inject
	private RoleManager						rm;

	UserApplicationCtxRegistration getUserApplicationCtxRegistration(String userName, String context) {
		TypedQuery<UserApplicationCtxRegistration> newownerqry = uaem.get().createNamedQuery("UserApplicationCtxRegistration.getByUserName", UserApplicationCtxRegistration.class);
		newownerqry.setParameter("username", userName);
		newownerqry.setParameter("domain", domain.get());
		newownerqry.setParameter("context", context);
		try {
			return newownerqry.getSingleResult();

		} catch (NoResultException ex) {
		}
		return null;
	}

	@Transactional(rollbackOn = Exception.class)
	@RequirePermissions(permissionExpression = Permissions.CONTEXT_OWNER)
	public void transferOwnerShip(String userName) throws UserAdminException {
		UserApplicationCtxRegistration newowner = getUserApplicationCtxRegistration(userName, context.get());
		if (newowner != null) {
			if (RequestStatus.ACTIVE.equals(newowner.getStatus())) {
				newowner.setOwner(true);
				uaem.get().persist(newowner);
			} else {
				logger.error("New owner should be the ACTIVE user of the application");
				throw new InvalidOperationException();
			}
		} else {
			throw new InvalidOperationException();
		}
		UserApplicationCtxRegistration owner = getUserApplicationCtxRegistration(curntUser.get().getUserName(), context.get());
		owner.setOwner(false);
		uaem.get().persist(owner);
	}

	/**
	 * Create context
	 * 
	 * @param context
	 * @return
	 * @throws ApplicationUserRegistrationNotActive
	 * @throws InvalidDomain
	 */
	@RFServiceMethod
	public Context create(Context context) throws UserAdminException, ApplicationUserRegistrationNotActive {
		return scm.create(context);
	}

	/**
	 * retrieve all contexts registered under given selected application / domain
	 * 
	 * @return
	 */
	@RFServiceMethod
	public List<Context> list(PageNSort pageNSort, String search) {
		return scm.list(pageNSort, search);
	}

	/**
	 * Get Application by passing its id
	 * 
	 * @param id
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.VIEW_CONTEXT)
	public Context getContextById(String id) {
		return uaem.get().find(Context.class, id);
	}

	/***
	 * remove given Context. It will change the status of the Context to "DELETED"
	 * 
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.DELETE_CONTEXT)
	public Boolean removeContext(Context context) {
		context.setStatus(GeneralStatus.DELETED);
		inject(context);
		uaem.get().persist(context);
		return true;
	}

	/**
	 * Retrieve logged in user's ACTIVE / PROVISIONAL application registration details.
	 * 
	 * @return
	 */
	@Transactional
	private List<UserApplicationCtxRegistration> getUserRegistrations() {
		// TODO: move this method inside UserManager and apply user view permissions.
		TypedQuery<UserApplicationCtxRegistration> qry = uaem.get().createNamedQuery("UserApplicationCtxRegistration.getByUser", UserApplicationCtxRegistration.class);
		qry.setParameter("userid", curntUser.get().getId());
		qry.setParameter("domain", domain.get());
		List<UserApplicationCtxRegistration> registrations = qry.getResultList();
		return registrations;
	}

	/**
	 * Retrieve all contexts where logged in user is having admin roles to change user registration to given context.
	 * 
	 * @return @
	 */
	@RFServiceMethod
	@Transactional
	public List<Context> getUserManagerContexts() {
		// TODO: Need to add junit use case to retrieve contexts after user registers with different contexts of a
		// selected domain.
		List<Context> regApps = new ArrayList<Context>();
		if (curntUser.get() != null) {
			User us = null;
			// uaem.get().getEntityManagerFactory().getCache().evictAll();
			UserManager um = injector.getInstance(UserManager.class);
			us = um.getUserById(curntUser.get().getId());
			if (us != null) {
				List<UserApplicationCtxRegistration> userRegistrations = getUserRegistrations();
				if (userRegistrations != null && userRegistrations.size() > 0) {
					for (UserApplicationCtxRegistration cur : userRegistrations) {
						if (RequestStatus.ACTIVE.equals(cur.getStatus()) || RequestStatus.PROVISIONAL.equals(cur.getStatus())) {
							Context context = cur.getContext();
							if (cur.getAdminContextRole() != null && cur.getAdminContextRole().getRoles() != null && cur.getAdminContextRole().getRoles().size() > 0) {
								// TODO : Once roles are defined add a check to see if logged in user has exact required
								// roles to change user regsitration to given context.
								regApps.add(context);
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
	 * retrieve all registered contexts by currently logged in user.
	 * 
	 * @return @
	 */
	@RFServiceMethod
	@Transactional
	public List<Context> getRegisteredContexts() {
		// TODO: Need to add junit use case to retrieve contexts after user registers with different contexts of a
		// selected domain.
		List<Context> regApps = new ArrayList<Context>();
		if (curntUser.get() != null) {
			User us = null;
			// uaem.get().getEntityManagerFactory().getCache().evictAll();
			UserManager um = injector.getInstance(UserManager.class);
			us = um.getUserById(curntUser.get().getId());
			if (us != null) {
				List<UserApplicationCtxRegistration> userRegistrations = getUserRegistrations();
				if (userRegistrations != null && userRegistrations.size() > 0) {
					for (UserApplicationCtxRegistration cur : userRegistrations) {
						if (RequestStatus.ACTIVE.equals(cur.getStatus()) || RequestStatus.PROVISIONAL.equals(cur.getStatus())) {
							Context context = cur.getContext();
							regApps.add(context);
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
	 * Retrieve list of users registered with given application excluding the owner of the application.
	 * 
	 * @param pageNSort
	 * @param status
	 * @param search
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.CONTEXT_USER_MANAGER)
	public List<UserApplicationCtxRegistration> getUsersByContext(PageNSort pageNSort, RequestStatus status, String search) {
		/*
		 * TODO: Write junit for this method. Writing junits for this has dependency on need to be able to register one
		 * user to a applcation. This need to be tackled at the end.
		 */
		List<UserApplicationCtxRegistration> list = new ArrayList<UserApplicationCtxRegistration>();
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
		if (context.get() != null && !context.get().isEmpty()) {
			params.put("context", context.get());
		}
		TypedQuery<UserApplicationCtxRegistration> qry = qg.createQuery("byApplication", UserApplicationCtxRegistration.class, pageNSort, params);
		list = qry.getResultList();
		return list;
	}

	@Transactional
	@RequirePermissions(permissionExpression = Permissions.CONTEXT_USER_MANAGER)
	public UserApplicationCtxRegistration getUserApplicationCtxRegistration(String ctxRegId) {
		UserApplicationCtxRegistration uacr = uaem.get().find(UserApplicationCtxRegistration.class, ctxRegId);
		return uacr;
	}

	/**
	 * Change the user context registration status with given application and context with passed on status except
	 * DISABLE status.
	 * 
	 * @param ids
	 *            UserApplicationCtxRegistration ids
	 * @param status
	 *            RequestStatus which is supposed to be updated.
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.CONTEXT_USER_REG)
	public List<String> updateStatusByUserAppCtxRegId(List<String> ids, RequestStatus status) {
		// TODO: Add junits.
		if (ids != null && ids.size() > 0 && status != null) {
			if (RequestStatus.DISABLED.equals(status) || RequestStatus.REQUESTED.equals(status)) {
				// TODO: Throw appropriate error stating operation not supported with this method. Need to make
				// different method call to disable.
			} else {
				// TODO: Add selected context in query.
				TypedQuery<UserApplicationCtxRegistration> qry = uaem.get().createNamedQuery("UserApplicationCtxRegistration.updateStatus", UserApplicationCtxRegistration.class);
				qry.setParameter("ids", ids);
				qry.setParameter("status", status);
				qry.setParameter("domain", domain.get());
				qry.executeUpdate();
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
	@RequirePermissions(permissionExpression = Permissions.CONTEXT_USER_DEREG)
	public List<String> markDisabledByUserAppCtxRegId(List<String> ids) {
		// TODO: Add junits.
		if (ids != null && ids.size() > 0) {
			TypedQuery<UserApplicationCtxRegistration> qry = uaem.get().createNamedQuery("UserApplicationCtxRegistration.updateStatus", UserApplicationCtxRegistration.class);
			qry.setParameter("ids", ids);
			qry.setParameter("status", RequestStatus.DISABLED);
			qry.setParameter("domain", domain.get());
			qry.executeUpdate();
			return ids;
		}
		return null;
	}

	/**
	 * Retrieve given users context roles with registered application's context.
	 * 
	 * @param uacr
	 *            UserApplicationCtxRegistration for given user
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.CONTEXT_USER_ROLE_MANAGER)
	public List<Role> getUserContextRoles(UserApplicationCtxRegistration uacr) {
		// TODO: Add junits.
		ContextRole cr = uacr.getContextRole();
		if (cr != null) {
			return cr.getRoles();
		}
		return null;
	}

	/**
	 * Update application roles of given user for selected application.
	 * 
	 * @param uacr
	 *            UserApplicationCtxRegistration for given user
	 * @param roleIds
	 *            roles to be updated
	 * @return
	 * @throws InvalidOperationException
	 */
	@Transactional
	public UserApplicationCtxRegistration updateUserApplicationRoleIds(UserApplicationCtxRegistration uacr, List<String> roleIds) throws InvalidOperationException {
		List<Role> roles = rm.getRolesFromIds(roleIds);
		return updateUserApplicationRoles(uacr, roles);
	}

	/**
	 * Update context roles of given user for given application's context.
	 * 
	 * @param uacr
	 *            UserApplicationCtxRegistration for given user
	 * @param roles
	 *            Roles assigned to given user in given context.
	 * @return
	 * @throws InvalidOperationException
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.CONTEXT_USER_ROLE_MANAGER)
	public UserApplicationCtxRegistration updateUserApplicationRoles(UserApplicationCtxRegistration uacr, List<Role> roles) throws InvalidOperationException {
		// TODO: Add junits.
		for (Role role : roles) {
			if (!AuthLevel.CONTEXT.equals(role.getLevel())) {
				throw new InvalidOperationException();
			}
		}
		ContextRole cr = uacr.getContextRole();
		if (cr == null) {
			cr = new ContextRole();
			cr.setUsApplicationCtxRegistration(uacr);
			uacr.setContextRole(cr);
		}
		if (roles != null && roles.size() > 0) {
			cr.setRoles(roles);
		} else {
			cr.getRoles().clear();
		}
		inject(cr);
		uaem.get().persist(cr);
		return uacr;
	}

	/**
	 * Retrieve given users context admin roles with registered application's context.
	 * 
	 * @param uacr
	 *            UserApplicationCtxRegistration for given user
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.CONTEXT_USER_ADMIN_ROLE_MANAGER)
	public List<Role> getUserContextAdminRoles(UserApplicationCtxRegistration uacr) {
		// TODO: Add junits.
		AdminContextRole cr = uacr.getAdminContextRole();
		if (cr != null) {
			return cr.getRoles();
		}
		return null;
	}

	public UserApplicationCtxRegistration updateUserConextAdminRoleIds(UserApplicationCtxRegistration uacr, List<String> roleIds) throws InvalidOperationException {
		List<Role> roles = rm.getRolesFromIds(roleIds);
		return updateUserConextAdminRoles(uacr, roles);
	}

	/**
	 * Update context admin roles of given user for given application's context.
	 * 
	 * @param uacr
	 *            UserApplicationCtxRegistration for given user
	 * @param roles
	 *            Roles assigned to given user in given context.
	 * @return
	 * @throws InvalidOperationException
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.CONTEXT_USER_ADMIN_ROLE_MANAGER)
	public UserApplicationCtxRegistration updateUserConextAdminRoles(UserApplicationCtxRegistration uacr, List<Role> roles) throws InvalidOperationException {
		// TODO: Add junits.
		for (Role role : roles) {
			if (!AuthLevel.ADMIN_CTX.equals(role.getLevel())) {
				throw new InvalidOperationException();
			}
		}
		AdminContextRole cr = uacr.getAdminContextRole();
		if (cr == null) {
			cr = new AdminContextRole();
			cr.setUsApplicationCtxRegistration(uacr);
			uacr.setAdminContextRole(cr);
		}
		if (roles != null && roles.size() > 0) {
			cr.setRoles(roles);
		} else {
			cr.getRoles().clear();
		}
		inject(cr);
		uaem.get().persist(cr);
		return uacr;
	}

	/**
	 * Retrieve paginated list of admin Context level roles.
	 * 
	 * @param pageNSort
	 *            pagination and sorting input
	 * @param search
	 *            search key
	 * @return
	 */
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.CONTEXT_USER_ADMIN_ROLE_MANAGER)
	public List<Role> getAdminRoles(PageNSort pageNSort, String search) {
		return rm.getRoles(pageNSort, AuthLevel.ADMIN_CTX, UserAdminURLGenerator.USERADMIN, search);
	}

	/**
	 * Retrieve context level Admin roles.
	 * 
	 * @return @
	 */
	@Transactional
	@RFServiceMethod
	public List<Role> getAdminRoles() {
		return getAdminRoles(null, null);
	}

	/**
	 * Retrieve context level Admin roles.
	 * 
	 * @return @
	 */
	@Transactional
	@RFServiceMethod
	@RequirePermissions(permissionExpression = Permissions.CONTEXT_USER_ROLE_MANAGER)
	public List<Role> getContextRoles() {
		return rm.getRoles(null, AuthLevel.CONTEXT, domain.get(), null);
	}

	/**
	 * Retrieve context from context name.
	 * 
	 * @param name
	 *            name of the context to be retrieved.
	 * @return
	 */
	@Transactional
	Context getContextByName(String name) {
		return scm.getContextByName(name);
	}
}

/**
 * another sub manager for operations which comes under application level permission access.
 * 
 */
@DomainRistrictedResource
class SubContextManager extends BaseUserAdminService {

	@Inject
	private ApplicationManager	am;

	@Inject
	private UserManager			um;

	// selected domain
	@Inject
	@Named(DomainAuthorizer.SELECTED_DOMAIN)
	private Provider<String>	domain;

	/**
	 * create Context.
	 * 
	 * @param context
	 *            context details with which context need to be created.
	 * @return
	 * @throws ApplicationUserRegistrationNotActive
	 * @throws InvalidDomain
	 * @throws UserAdminException
	 * 
	 */

	@Transactional
	@RequirePermissions(permissionExpression = Permissions.CREATE_CONTEXT)
	Context create(Context context) throws InvalidDomain, ApplicationUserRegistrationNotActive, UserAdminException {
		Application app = am.getApplicationByDomainName(domain.get());
		context.setApplication(app);
		if (context.getId() != null) {
			List<String> perms = new ArrayList<String>();
			perms.add(Permissions.CREATE_CONTEXT);
			throw new UserNotAutherisedException(perms);
		}
		Pattern pattern = Pattern.compile(ConstraintRegularExpressions.NO_WHITE_SPACE_EXP);
		Matcher matcher = pattern.matcher(context.getName());
		if (!matcher.matches()) {
			throw new InvalidOperationException();
		}
		inject(context);
		uaem.get().persist(context);
		um.internalRegisterWithContext(context, curntUser.get().getId(), true);
		return context;
	}

	/**
	 * Retrieve list of context under given selected domain.
	 * 
	 * @return @
	 */

	@Transactional
	@RequirePermissions(permissionExpression = Permissions.LIST_CONTEXT)
	List<Context> list(PageNSort pageNSort, String search) {
		QueryGenerator qg = new QueryGenerator(uaem.get());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("domain", domain.get());
		params.put("status", GeneralStatus.ACTIVE);
		if (search != null && !search.isEmpty()) {
			params.put("search", "%" + search + "%");
		}
		TypedQuery<Context> qry = qg.createQuery("byApplication", Context.class, pageNSort, params);
		return qry.getResultList();
	}

	/**
	 * Retrieve context from context name.
	 * 
	 * @param name
	 *            name of the context to be retrieved.
	 * @return
	 */
	@Transactional
	Context getContextByName(String context) {
		TypedQuery<Context> qry = uaem.get().createNamedQuery("Context.getByName", Context.class);
		qry.setParameter("status", GeneralStatus.ACTIVE);
		qry.setParameter("name", context);
		qry.setParameter("domain", domain.get());
		Context ctx = null;
		try {
			ctx = qry.getSingleResult();
		} catch (NoResultException ex) {
			// its ok if it returns null value
		}
		return ctx;
	}
}
