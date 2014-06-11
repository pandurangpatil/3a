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

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import open.pandurang.gwt.helper.requestfactory.marker.RFService;
import open.pandurang.gwt.helper.requestfactory.marker.RFServiceMethod;

import com.agnie.common.cache.CacheService;
import com.agnie.common.requestfactory.AgnieRFServiceLocator;
import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.agnie.useradmin.persistance.client.enums.GeneralStatus;
import com.agnie.useradmin.persistance.client.enums.RequestStatus;
import com.agnie.useradmin.persistance.client.enums.UserStatus;
import com.agnie.useradmin.persistance.client.exception.ApplicationUserRegistrationNotActive;
import com.agnie.useradmin.persistance.client.exception.CriticalException;
import com.agnie.useradmin.persistance.client.exception.InvalidContext;
import com.agnie.useradmin.persistance.client.exception.InvalidDomain;
import com.agnie.useradmin.persistance.client.exception.OldPasswordDontMatch;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.server.auth.DomainAuthorizer;
import com.agnie.useradmin.persistance.server.auth.UserData;
import com.agnie.useradmin.persistance.server.auth.UsersRistrictedResource;
import com.agnie.useradmin.persistance.server.common.QueryGenerator;
import com.agnie.useradmin.persistance.server.entity.AdminContextRole;
import com.agnie.useradmin.persistance.server.entity.Application;
import com.agnie.useradmin.persistance.server.entity.Context;
import com.agnie.useradmin.persistance.server.entity.ContextRole;
import com.agnie.useradmin.persistance.server.entity.DefaultAppRole;
import com.agnie.useradmin.persistance.server.entity.Role;
import com.agnie.useradmin.persistance.server.entity.User;
import com.agnie.useradmin.persistance.server.entity.UserApplicationCtxRegistration;
import com.agnie.useradmin.persistance.server.entity.UserApplicationRegistration;
import com.agnie.useradmin.persistance.server.listrequest.PageNSort;
import com.agnie.useradmin.session.server.auth.RequirePermissions;
import com.agnie.useradmin.session.server.injector.SessionServletModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.persist.Transactional;

@UsersRistrictedResource
@RFService(value = AgnieRFServiceLocator.class)
public class UserManager extends BaseUserAdminService {
	@Inject
	@Named(DomainAuthorizer.SELECTED_DOMAIN)
	// this is available only if manager method is invoked in selected context of domain.
	private Provider<String>	domain;
	@Inject
	private AuthenticateManager	am;
	@Inject
	private ContextManager		cm;
	@Inject
	CacheService				cacheService;
	@Inject
	@Named(SessionServletModule.SESSION_ID)
	private Provider<String>	sessionId;
	@Inject
	private ApplicationManager	appm;

	/**
	 * save User
	 * 
	 * @param user
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.EDIT_USER_DETAIL + " || " + Permissions.CREATE_USER)
	public String saveUser(@UserData User user) {
		uaem.get().persist(user);
		// TODO: Note this is a fix towards user details not getting updated on Account widget when user update his /
		// her details. We probably need some better fix, as save user could be called to save different user other than
		// that of currently logged in user.
		if (sessionId.get() != null) {
			cacheService.put(sessionId.get(), null);
		}
		return user.getId();
	}

	/**
	 * Get User by passing its id
	 * 
	 * @param id
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.USER)
	public User getUserById(@UserData String id) {
		return uaem.get().find(User.class, id);
	}

	/***
	 * To remove given single User
	 * 
	 * @param user
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.DELETE_USER)
	public Boolean removeUser(@UserData User user) {
		user.setStatus(UserStatus.DELETED);
		uaem.get().persist(user);
		return true;
	}

	/**
	 * To remove list of users.
	 * 
	 * @param users
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.DELETE_USER)
	public Boolean removeUsers(List<User> users) {
		if (users != null && users.size() > 0) {
			List<String> userIds = new ArrayList<String>();
			for (User user : users) {
				userIds.add(user.getId());
			}
			return removeUserFromIds(userIds);
		}
		return true;
	}

	/**
	 * To remove list of user from their respective ids
	 * 
	 * @param ids
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.DELETE_USER)
	public Boolean removeUserFromIds(List<String> ids) {
		if (ids != null && ids.size() > 0) {
			// TODO: add delete native query
			TypedQuery<User> qry = uaem.get().createNamedQuery("User.delete", User.class);
			qry.setParameter("status", UserStatus.DELETED);
			qry.setParameter("ids", ids);
			qry.executeUpdate();
			uaem.get().clear();
		}
		return true;
	}

	/**
	 * find user by its username.
	 * 
	 * @param userName
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.USER)
	public User getUserByUserName(@UserData String userName) {
		User us = null;
		if (userName != null) {
			TypedQuery<User> qry = uaem.get().createNamedQuery("User.getByUserName", User.class);
			qry.setParameter("userName", userName);
			qry.setParameter("status", UserStatus.ACTIVE);
			us = qry.getSingleResult();
		}
		return us;
	}

	/**
	 * find user by its email id.
	 * 
	 * @param emailId
	 * @return
	 */
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.USER)
	public User getUserByEmailId(String emailId) {
		User us = null;
		if (emailId != null) {
			TypedQuery<User> qry = uaem.get().createNamedQuery("User.getByEmailId", User.class);
			qry.setParameter("emailId", emailId);
			qry.setParameter("status", UserStatus.ACTIVE);
			us = qry.getSingleResult();
		}
		return us;
	}

	@Transactional
	@RFServiceMethod
	@RequirePermissions(permissionExpression = Permissions.RESET_USER_PASSWORD)
	public void resetPassword(@UserData String id, String oldPassword, String newPassword) throws OldPasswordDontMatch {
		User user = getUserById(id);
		if (user != null) {
			if (user.getPassword().equals(oldPassword)) {
				user.setPassword(newPassword);
			} else {
				throw new OldPasswordDontMatch();
			}
		}
	}

	/**
	 * Retrieve paginated list of users.
	 * 
	 * @param pageNSort
	 * @param status
	 * @param search
	 * @return
	 */
	@RFServiceMethod
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.USER)
	public List<User> getUsers(PageNSort pageNSort, UserStatus status, String search) {
		List<User> list = new ArrayList<User>();
		QueryGenerator qg = new QueryGenerator(uaem.get());
		Map<String, Object> params = new HashMap<String, Object>();
		if (status != null) {
			params.put("status", status);
		}
		if (search != null && !search.isEmpty()) {
			params.put("search", "%" + search + "%");
		}
		TypedQuery<User> qry = qg.createQuery("all", User.class, pageNSort, params);
		list = qry.getResultList();
		return list;
	}

	/**
	 * Retrieve User's application registration details for given domain
	 * 
	 * @param selDomain
	 *            selected domain.
	 * @param userId
	 *            user id.
	 * @return
	 */
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.USER)
	public UserApplicationRegistration getUserAppReg(String selDomain, @UserData String userId) {
		TypedQuery<UserApplicationRegistration> qry = uaem.get().createNamedQuery("UserApplicationRegistration.getByUserAndApplication", UserApplicationRegistration.class);
		qry.setParameter("domain", selDomain);
		qry.setParameter("userid", userId);
		UserApplicationRegistration uar = null;
		try {
			uar = qry.getSingleResult();
		} catch (NoResultException ex) {
			// its ok if it returns null value
		}
		return uar;
	}

	@Transactional
	@RequirePermissions(permissionExpression = Permissions.USER)
	public UserApplicationCtxRegistration getUserAppContextReg(String selDomain, String context, @UserData String userId) {
		TypedQuery<UserApplicationCtxRegistration> qry = uaem.get().createNamedQuery("UserApplicationCtxRegistration.getByUserApplicationAndContext", UserApplicationCtxRegistration.class);
		qry.setParameter("domain", selDomain);
		qry.setParameter("userid", userId);
		qry.setParameter("context", context);

		UserApplicationCtxRegistration uacr = null;
		try {
			uacr = qry.getSingleResult();
		} catch (NoResultException ex) {
			// its ok if it returns null value
		}
		return uacr;
	}

	@Transactional
	void internalRegisterWithContext(Context context, String userId, boolean owner) throws InvalidDomain, ApplicationUserRegistrationNotActive {
		String selDomain = domain.get();
		if (selDomain == null || selDomain.isEmpty()) {
			throw new InvalidDomain();
		}
		UserApplicationRegistration uar = getUserAppReg(selDomain, userId);
		if (uar != null && (RequestStatus.ACTIVE.equals(uar.getStatus()) || RequestStatus.PROVISIONAL.equals(uar.getStatus()))) {
			// User status check is not done as this method will be allowed to be called by only the logged in user.
			// And if user is able to login then his / her status is either active or provisional.
			Application dom = am.getApplicationByDomainName(selDomain);
			if (dom != null && GeneralStatus.ACTIVE.equals(dom.getStatus()) && context != null) {
				DefaultAppRole dfAprole = appm.getDefaultApplicationRole(AuthLevel.CONTEXT, selDomain);
				List<Role> defaultRoles = null;
				if (dfAprole != null)
					defaultRoles = dfAprole.getRoles();
				DefaultAppRole dfAdminApprole = appm.getDefaultApplicationRole(AuthLevel.ADMIN_CTX, selDomain);
				List<Role> defaultAdminRoles = null;
				if (dfAdminApprole != null)
					defaultAdminRoles = dfAdminApprole.getRoles();
				// TODO: Add a check if user is already registered and user's request is in non ACTIVE state.
				UserApplicationCtxRegistration uacr = cm.getUserApplicationCtxRegistration(userId, context.getName());
				if (uacr == null) {
					uacr = new UserApplicationCtxRegistration();
					uacr.setUsApplicationRegistration(uar);
					uacr.setUser(uar.getUser());
					uacr.setContext(context);
					uacr.setOwner(owner);
					uacr.setStatus(dom.getDefaultCtxStatus());
					if (RequestStatus.REQUESTED.equals(dom.getDefaultCtxStatus())) {
						/**
						 * TODO: If Contextual default status is other than ACTIVE then send a mail to app owner /
						 * user's having Contextual Reg role to take action. Challenge here is to get email id of
						 * Context administrator
						 */
					}
					if (defaultRoles != null && defaultRoles.size() > 0) {
						ContextRole ctxRole = new ContextRole();
						ctxRole.setId(java.util.UUID.randomUUID().toString());
						ctxRole.setUsApplicationCtxRegistration(uacr);
						ctxRole.setRoles(defaultRoles);
						uacr.setContextRole(ctxRole);
					}

					if (defaultAdminRoles != null && defaultAdminRoles.size() > 0) {
						AdminContextRole ctxRole = new AdminContextRole();
						ctxRole.setId(java.util.UUID.randomUUID().toString());
						ctxRole.setUsApplicationCtxRegistration(uacr);
						ctxRole.setRoles(defaultAdminRoles);
						uacr.setAdminContextRole(ctxRole);
					}
					uaem.get().persist(uacr);
				}
			} else {
				throw new InvalidDomain();
			}
		} else {
			throw new ApplicationUserRegistrationNotActive();
		}
	}

	/**
	 * Register logged in user with given Context.
	 * 
	 * @param context
	 *            Context to which user want to register.
	 * @param userId
	 *            User Id of the registering user.
	 * @throws InvalidDomain
	 * @throws CriticalException
	 * @throws InvalidContext
	 * @throws ApplicationUserRegistrationNotActive
	 */

	@Transactional
	@RequirePermissions(permissionExpression = Permissions.REGISTER_TO_CTX)
	public void registerWithContext(String context, @UserData String userId) throws InvalidDomain, InvalidContext, ApplicationUserRegistrationNotActive {
		if (context != null && !context.isEmpty()) {
			Context ctx = cm.getContextByName(context);
			internalRegisterWithContext(ctx, userId, false);
		} else {
			throw new InvalidContext();
		}
	}
}
