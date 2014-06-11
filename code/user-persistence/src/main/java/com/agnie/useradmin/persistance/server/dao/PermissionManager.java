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
import com.agnie.useradmin.persistance.server.entity.Permission;
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
public class PermissionManager extends BaseUserAdminService {
	@SuppressWarnings("unused")
	private static org.apache.log4j.Logger	logger	= Logger.getLogger(RoleManager.class);

	@Inject
	@Named(DomainAuthorizer.SELECTED_DOMAIN)
	private Provider<String>				domain;

	/**
	 * Create permission.
	 * 
	 * @param permission
	 *            permission to create.
	 * @return
	 * @throws UserNotAutherisedException
	 */
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.CREATE_PERMISSION + " || " + Permissions.EDIT_PERMISSION)
	@RFServiceMethod
	public Permission save(Permission permission) throws UserNotAutherisedException {
		if (AuthLevel.ADMIN_APP.equals(permission.getLevel()) || AuthLevel.ADMIN_CTX.equals(permission.getLevel())) {
			throw new UserNotAutherisedException();
		}
		if (domain != null && domain.get() != null) {
			ApplicationManager am = injector.getInstance(ApplicationManager.class);
			if (permission.getApplication() == null) {
				Application app = am.getApplicationByDomainName(domain.get());
				permission.setApplication(app);
			}
			if (permission.getLevel() == null) {
				permission.setLevel(AuthLevel.APPLICATION);
			}
			inject(permission);
			uaem.get().persist(permission);
			return permission;
		}
		return null;
	}

	/**
	 * Retrieve permissions by permission id.
	 * 
	 * @param permissionId
	 *            guid of the permission.
	 * @return
	 * @throws UserNotAutherisedException
	 */
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.PERMISSION)
	@RFServiceMethod
	public Permission getPermissionById(String permissionId) throws UserNotAutherisedException {
		if (permissionId != null && !(permissionId.isEmpty())) {
			return uaem.get().find(Permission.class, permissionId);
		}
		return null;
	}

	/**
	 * Remove list of permissions.
	 * 
	 * @param permissions
	 *            List of permisisons.
	 * @return
	 */
	@Transactional
	public Boolean removePermissions(List<Permission> permissions) {
		if (permissions != null && permissions.size() > 0) {
			List<String> permIds = new ArrayList<String>();
			for (Permission perm : permissions) {
				permIds.add(perm.getId());
			}
			// Note: No permission check has been applied on removePermissions as it is internally calling
			// removePermissionsByIds and we have applied permission check for the same.
			return removePermissionsByIds(permIds);
		}
		return true;
	}

	/**
	 * Remove list of permissions from their respective ids.
	 * 
	 * @param ids
	 *            list of permission ids.
	 * @return
	 * @throws UserNotAutherisedException
	 */
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.DELETE_PERMISSION)
	@RFServiceMethod
	public Boolean removePermissionsByIds(List<String> ids) throws UserNotAutherisedException {
		// TODO: Add a check before removing any permission if that is not getting used any where.
		if (ids != null && ids.size() > 0) {
			TypedQuery<Permission> qry = uaem.get().createNamedQuery("Permission.delete", Permission.class);
			qry.setParameter("ids", ids);
			qry.executeUpdate();
		}
		return true;
	}

	/**
	 * 
	 * @param pageNSort
	 * @param level
	 * @param search
	 * @return
	 * @throws UserNotAutherisedException
	 */
	@Transactional
	@RequirePermissions(permissionExpression = Permissions.PERMISSION)
	@RFServiceMethod
	public List<Permission> getPermissionsByApplication(PageNSort pageNSort, AuthLevel level, String search) throws UserNotAutherisedException {
		QueryGenerator qg = new QueryGenerator(uaem.get());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("domain", domain.get());
		if (level != null) {
			params.put("level", level);
		}
		if (search != null && !search.isEmpty()) {
			params.put("search", "%" + search + "%");
		}
		TypedQuery<Permission> qry = qg.createQuery("byApplication", Permission.class, pageNSort, params);
		return qry.getResultList();
	}

}
