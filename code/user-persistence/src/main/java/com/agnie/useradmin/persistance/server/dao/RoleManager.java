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

import javax.persistence.TypedQuery;

import open.pandurang.gwt.helper.requestfactory.marker.RFService;
import open.pandurang.gwt.helper.requestfactory.marker.RFServiceMethod;

import org.apache.log4j.Logger;

import com.agnie.common.requestfactory.AgnieRFServiceLocator;
import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.server.auth.DomainAuthorizer;
import com.agnie.useradmin.persistance.server.auth.DomainRistrictedResource;
import com.agnie.useradmin.persistance.server.common.QueryGenerator;
import com.agnie.useradmin.persistance.server.entity.Application;
import com.agnie.useradmin.persistance.server.entity.Role;
import com.agnie.useradmin.persistance.server.listrequest.PageNSort;
import com.agnie.useradmin.session.client.helper.UserNotAutherisedException;
import com.agnie.useradmin.session.server.auth.RequirePermissions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.persist.Transactional;

@DomainRistrictedResource
@Singleton
@RFService(value = AgnieRFServiceLocator.class)
public class RoleManager extends BaseUserAdminService {
	@SuppressWarnings("unused")
	private static org.apache.log4j.Logger	logger	= Logger.getLogger(RoleManager.class);

	@Inject
	@Named(DomainAuthorizer.SELECTED_DOMAIN)
	private Provider<String>				domain;

	@Transactional
	@RequirePermissions(permissionExpression = Permissions.CREATE_ROLE + " || " + Permissions.EDIT_ROLE)
	@RFServiceMethod
	public Role save(Role role) throws UserNotAutherisedException {
		if (AuthLevel.ADMIN_APP.equals(role.getLevel()) || AuthLevel.ADMIN_CTX.equals(role.getLevel())) {
			throw new UserNotAutherisedException();
		}
		if (domain != null && domain.get() != null) {
			ApplicationManager am = injector.getInstance(ApplicationManager.class);
			if (role.getApplication() == null) {
				Application app = am.getApplicationByDomainName(domain.get());
				role.setApplication(app);
			}
			inject(role);
			uaem.get().persist(role);
			return role;
		}
		return null;
	}

	/**
	 * Retrieve roles by role id.
	 * 
	 * @param roleId
	 *            guid of the role.
	 * @return
	 * @throws UserNotAutherisedException
	 */
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.ROLE)
	@RFServiceMethod
	public Role getRoleById(String roleId) {
		if (roleId != null && !(roleId.isEmpty())) {
			return uaem.get().find(Role.class, roleId);
		}
		return null;
	}

	/**
	 * Remove list of Roles.
	 * 
	 * @param roles
	 *            List of roles.
	 * @return
	 */
	@Transactional
	public Boolean removeRoles(List<Role> roles) {
		if (roles != null && roles.size() > 0) {
			List<String> roleIds = new ArrayList<String>();
			for (Role role : roles) {
				roleIds.add(role.getId());
			}
			// Note: No permission check has been applied on removeRoles as it is internally calling
			// removeRoleByIds and we have applied permission check for the same.
			return removeRolesByIds(roleIds);
		}
		return true;
	}

	/**
	 * Remove list of roles from their respective ids.
	 * 
	 * @param ids
	 *            list of role ids.
	 * @return
	 * @throws UserNotAutherisedException
	 */
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.DELETE_ROLE)
	@RFServiceMethod
	public Boolean removeRolesByIds(List<String> ids) {
		// TODO: Add a check before removing any role if that is not getting used any where.
		if (ids != null && ids.size() > 0) {
			TypedQuery<Role> qry = uaem.get().createNamedQuery("Role.delete", Role.class);
			qry.setParameter("ids", ids);
			qry.executeUpdate();
		}
		return true;
	}

	/**
	 * Retrieve roles from their ids. Note: Permission check is not added as its expected to be added at caller level.
	 * 
	 * @param ids
	 * @return
	 */
	@Transactional
	List<Role> getRolesFromIds(List<String> ids) {
		if (ids != null && ids.size() > 0) {
			TypedQuery<Role> qry = uaem.get().createNamedQuery("Role.getFromIds", Role.class);
			qry.setParameter("ids", ids);
			return qry.getResultList();
		}
		return null;
	}

	/**
	 * Retrieve list of roles Note: Permission check is not being done hear as different permission checks being added
	 * from all the callers
	 * 
	 * @param pageNSort
	 *            pagination object
	 * @param level
	 *            Any AuthLevel Position
	 * @param seldomain
	 *            selected domain
	 * @param search
	 *            search string
	 * @return
	 * @throws UserNotAutherisedException
	 */
	@Transactional
	List<Role> getRoles(PageNSort pageNSort, AuthLevel level, String seldomain, String search) {

		// of this method.
		QueryGenerator qg = new QueryGenerator(uaem.get());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("domain", seldomain);
		if (level != null) {
			params.put("level", level);
		}
		if (search != null && !search.isEmpty()) {
			params.put("search", "%" + search + "%");
		}
		TypedQuery<Role> qry = qg.createQuery("byApplication", Role.class, pageNSort, params);
		return qry.getResultList();
	}

	/**
	 * Retrieve list of roles for selected application
	 * 
	 * @param pageNSort
	 *            pagination object
	 * @param level
	 *            either APPLICATION | CONTEXT
	 * @param search
	 *            search string
	 * @return
	 * @throws UserNotAutherisedException
	 */
	@Transactional
	@RFServiceMethod
	@RequirePermissions(permissionExpression = Permissions.ROLE)
	public List<Role> getRolesByApplication(PageNSort pageNSort, AuthLevel level, String search) {
		return getRoles(pageNSort, level, domain.get(), search);
	}
}
