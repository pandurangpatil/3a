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

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.agnie.common.cache.CacheService;
import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.gwt.serverclient.client.helper.InvalidPermission;
import com.agnie.useradmin.session.server.entity.UserAuthSession;
import com.agnie.useradmin.session.server.entity.UserPermissions;
import com.agnie.useradmin.session.server.mybatis.mapper.UserAccountMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

@Singleton
public class UserSessionDao {

	@Inject
	protected Provider<UserAccountMapper>	uamap;

	@Inject
	protected Provider<EntityManager>		em;

	@Inject
	CacheService							cacheService;

	private static org.apache.log4j.Logger	logger	= Logger.getLogger(UserSessionDao.class);

	private EntityManager getEntityManager() {
		return em.get();
	}

	/**
	 * Retrieve User Details from Session ID
	 * 
	 * @param sessionID
	 * @return
	 */
	@org.mybatis.guice.transactional.Transactional
	public UserAccount getUserAccountBySessionId(String sessionID) {
		UserAccount ua = (UserAccount) cacheService.get(sessionID);
		if (ua == null) {
			ua = uamap.get().getUserAccount(sessionID);
			if (ua != null) {
				cacheService.put(sessionID, ua);
			}
		}
		return ua;
	}

	/**
	 * Remove given session by Session ID.
	 * 
	 * @param sessionID
	 * @return
	 */
	@Transactional
	public boolean removeSession(String sessionID) {
		UserAuthSession uas = getEntityManager().find(UserAuthSession.class, sessionID);
		if (uas != null) {
			getEntityManager().remove(uas);
			return true;
		} else
			return false;
	}

	/**
	 * Retrieve User Permissions of given logged in user for selected domain
	 * 
	 * @param domain
	 *            selected domain
	 * @param sessionId
	 *            logged in user's session id
	 * @return
	 */

	@org.mybatis.guice.transactional.Transactional
	public UserPermissions getPermissionsForDomain(String domain, String sessionId) {
		/*
		 * TODO: Need to add Junits
		 */
		// TODO: When ehcache layer is added add these ACL in cache and expire it after an optimal time limit.
		// So that calls being made in that particular time should not hit to DB.
		if (domain != null && !(domain.isEmpty()) && sessionId != null && !(sessionId.isEmpty())) {
			Integer owner = uamap.get().doesUserOwnsTheDomain(sessionId, domain);
			boolean superUser = false;
			if (owner != null && owner == 1) {
				superUser = true;
			}

			List<String> perms = uamap.get().getAllPermissionsByUserAndDomain(sessionId, domain);
			return new UserPermissions(superUser, perms);
		}
		return null;
	}

	/**
	 * Retrieve ACL object from given SessionID and domain
	 * 
	 * @param domain
	 *            selected domain
	 * @param sessionId
	 *            logged in user's session id
	 * @return
	 */
	@org.mybatis.guice.transactional.Transactional
	public AccessControlList getAclForDomain(String domain, String sessionId) {
		/*
		 * TODO: Need to add Junits
		 */
		// TODO: When ehcache layer is added add these ACL in cache and expire it after an optimal time limit.
		// So that calls being made in that particular time should not hit to DB.
		AccessControlList acl = null;
		UserPermissions userPermissions = getPermissionsForDomain(domain, sessionId);
		if (userPermissions != null) {
			if (userPermissions.isSuperUser()) {
				acl = new AccessControlList(true);
			} else {
				acl = new AccessControlList();
			}

			List<String> perms = userPermissions.getPermissions();
			if (perms != null && perms.size() > 0) {
				for (String perm : perms) {
					try {
						acl.addPermission(perm);
					} catch (InvalidPermission e) {
						logger.error("Invalid permission", e);
					}
				}
			}
		}
		return acl;
	}

	/**
	 * Retrieve logged in users permissions for selected domain and context
	 * 
	 * @param domain
	 *            selected domain
	 * @param context
	 *            selected context
	 * @param sessionId
	 *            logged in user's session id
	 * @return
	 */
	public UserPermissions getPermissionsForContext(String domain, String context, String sessionId) {
		if (domain != null && !(domain.isEmpty()) && sessionId != null && !(sessionId.isEmpty()) && context != null && !(context.isEmpty())) {
			Integer owner = uamap.get().doesUserOwnsTheContext(sessionId, domain, context);
			boolean superUser = false;
			if (owner != null && owner == 1) {
				superUser = true;
			}

			List<String> perms = uamap.get().getAllPermissionsByUserDomainAndContext(sessionId, domain, context);
			return new UserPermissions(superUser, perms);
		}
		return null;
	}

	/**
	 * Retrieve logged in users ACL for selected domain and context
	 * 
	 * @param domain
	 *            selected domain
	 * @param context
	 *            selected context
	 * @param sessionId
	 *            logged in user's session id
	 * @return
	 */
	@org.mybatis.guice.transactional.Transactional
	public AccessControlList getAclForContext(String domain, String context, String sessionId) {
		/*
		 * TODO: Need to add Junits
		 */
		// TODO: When ehcache layer is added add these ACL in cache and expire it after an optimal time limit.
		// So that calls being made in that particular time should not hit to DB.
		AccessControlList acl = null;
		UserPermissions userPermissions = getPermissionsForContext(domain, context, sessionId);
		if (userPermissions.isSuperUser()) {
			acl = new AccessControlList(true);
		} else {
			acl = new AccessControlList();
		}

		List<String> perms = userPermissions.getPermissions();
		if (perms != null && perms.size() > 0) {
			for (String perm : perms) {
				try {
					acl.addPermission(perm);
				} catch (InvalidPermission e) {
					logger.error("Invalid permission", e);
				}
			}
		}
		return acl;
	}

}
