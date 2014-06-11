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

import java.util.List;

import org.apache.log4j.Logger;

import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.agnie.common.gwt.serverclient.client.helper.InvalidPermission;
import com.agnie.useradmin.persistance.server.mybatis.mapper.UserPermissionsMapper;
import com.agnie.useradmin.session.server.mybatis.mapper.UserAccountMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class UserProfileDao {

	private static org.apache.log4j.Logger		logger	= Logger.getLogger(UserProfileDao.class);

	@Inject
	protected Provider<UserPermissionsMapper>	upmap;

	@Inject
	protected Provider<UserAccountMapper>		uamap;

	/**
	 * Retrieve logged in user's Admin ACL for given domain
	 * 
	 * @param domain
	 *            domain for which logged in user's admin ACL need to be retrieved.
	 * @param sessionId
	 *            session id of currently logged in user.
	 * @return
	 */
	@org.mybatis.guice.transactional.Transactional
	public AccessControlList getAdminAclForDomain(String domain, String sessionId) {
		/*
		 * TODO: Need to add Junits
		 */
		// TODO: When ehcache layer is added add these ACL in cache and expire it after an optimal time limit.
		// So that calls being made in that particular time should not hit to DB.
		AccessControlList acl = null;
		if (domain != null && !(domain.isEmpty()) && sessionId != null && !(sessionId.isEmpty())) {
			Integer owner = uamap.get().doesUserOwnsTheDomain(sessionId, domain);
			if (owner != null && owner == 1) {
				acl = new AccessControlList(true);
			} else {
				acl = new AccessControlList();
			}

			// Retrieve admin permissions
			List<String> perms = upmap.get().getAllAdminPermissionsByUserAndDomain(sessionId, domain);
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
	 * Retrieve logged in user's admin ACL for given selected domain and context.
	 * 
	 * @param domain
	 *            Selected domain
	 * @param context
	 *            Selected context
	 * @param sessionId
	 *            Session id of logged in user.
	 * @return
	 */
	@org.mybatis.guice.transactional.Transactional
	public AccessControlList getAdminAclForContext(String domain, String context, String sessionId) {
		/*
		 * TODO: Need to add Junits
		 */
		// TODO: When ehcache layer is added add these ACL in cache and expire it after an optimal time limit.
		// So that calls being made in that particular time should not hit to DB.
		AccessControlList acl = null;
		if (domain != null && !(domain.isEmpty()) && sessionId != null && !(sessionId.isEmpty()) && context != null && !(context.isEmpty())) {
			Integer owner = uamap.get().doesUserOwnsTheContext(sessionId, domain, context);
			if (owner != null && owner == 1) {
				acl = new AccessControlList(true);
			} else {
				acl = new AccessControlList();
			}

			// Retrieve admin permissions
			List<String> perms = upmap.get().getAllAdminPermissionsByUserDomainAndContext(sessionId, domain, context);
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
}
