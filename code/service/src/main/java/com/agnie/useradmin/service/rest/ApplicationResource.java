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
package com.agnie.useradmin.service.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.agnie.useradmin.persistance.client.enums.RequestStatus;
import com.agnie.useradmin.persistance.client.enums.SortOrder;
import com.agnie.useradmin.persistance.client.exception.InvalidOperationException;
import com.agnie.useradmin.persistance.client.exception.UserAdminException;
import com.agnie.useradmin.persistance.server.dao.ApplicationManager;
import com.agnie.useradmin.persistance.server.dao.RoleManager;
import com.agnie.useradmin.persistance.server.entity.Role;
import com.agnie.useradmin.persistance.server.entity.UserApplicationRegistration;
import com.agnie.useradmin.persistance.server.listrequest.Page;
import com.agnie.useradmin.persistance.server.listrequest.PageNSort;
import com.agnie.useradmin.persistance.server.listrequest.Sort;
import com.agnie.useradmin.service.client.injector.RestConstants;
import com.agnie.useradmin.service.exception.MissingParameterException;
import com.agnie.useradmin.service.injector.UserAdminJerseyServletModule;
import com.agnie.useradmin.service.rest.bean.PaginatedEntity;
import com.google.inject.Inject;

@Path("/" + UserAdminJerseyServletModule.REST_BASE + "/" + ContextResource.APPLICATION_BASE)
public class ApplicationResource implements RestConstants {

	@Inject
	private RoleManager			rm;

	@Inject
	private ApplicationManager	am;

	/**
	 * Retrieve paginated list of users registered with selected application.
	 * 
	 * @param sortOrder
	 *            sort order either ASC or DESC
	 * @param sortColumn
	 *            sort column on which result will be sorted
	 * @param startIndex
	 *            start index of the record
	 * @param pagesize
	 *            no of records to fetch in given request
	 * @param status
	 *            filtered by registration status of the user
	 * @param search
	 *            search string to search records containing given string
	 * @return
	 */
	@Path(USERS)
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public PaginatedEntity<UserApplicationRegistration> getUsers(@DefaultValue("ASC") @QueryParam(PARAM_SORT_ORDER) SortOrder sortOrder,
			@DefaultValue("updateOn") @QueryParam(PARAM_SORT_COLUMN) String sortColumn, @DefaultValue("0") @QueryParam(PARAM_START_INDEX) int startIndex,
			@DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(PARAM_PAGE_SIZE) int pagesize, @QueryParam(PARAM_STATUS) RequestStatus status, @QueryParam(PARAM_SEARCH_KEY) String search) {

		PageNSort ps = new PageNSort(new Page(startIndex, pagesize), new Sort(sortColumn, sortOrder));
		List<UserApplicationRegistration> users = am.getUsersByApplication(ps, status, search);
		if (users != null) {
			ps.getPage().setPageSize(users.size());
		} else {
			ps.getPage().setPageSize(0);
		}
		return new PaginatedEntity<UserApplicationRegistration>(ps, users);

	}

	/**
	 * update given user's registration status with selected application.
	 * 
	 * @param appRegIds
	 *            user's registration id with selected application.
	 * @param status
	 * @throws UserAdminException
	 */
	@Path(USERS)
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public void updateStatus(List<String> appRegIds, @QueryParam(PARAM_STATUS) RequestStatus status) throws UserAdminException {
		if (status == null)
			throw new MissingParameterException(PARAM_STATUS);

		if (appRegIds != null && appRegIds.size() > 0)
			am.updateStatusByUserAppRegId(appRegIds, status);
	}

	/**
	 * Mark given user's registration with selected application as deleted.
	 * 
	 * @param appRegIds
	 *            user's registration id with selected application.
	 */
	@Path(USERS)
	@DELETE
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public void deleteUser(List<String> appRegIds) {
		if (appRegIds != null && appRegIds.size() > 0)
			am.markDisabledByUserAppRegId(appRegIds);
	}

	/**
	 * Retrieve user details from users registration id with selected application
	 * 
	 * @param appRegId
	 *            user's registration id with selected application.
	 * @return
	 */
	@Path(USERS + "/{" + PATH_PARAM_APP_REG_ID + "}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public UserApplicationRegistration getUser(@PathParam(PATH_PARAM_APP_REG_ID) String appRegId) {
		return am.getUserApplicationRegistration(appRegId);
	}

	/**
	 * Retrieve application roles assigned to given user identified by users registration id with selected application
	 * 
	 * @param appRegId
	 *            user's registration id with selected application.
	 * @return
	 */
	@Path(USERS + "/{" + PATH_PARAM_APP_REG_ID + "}/" + ROLES)
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Role> getRoles(@PathParam(PATH_PARAM_APP_REG_ID) String appRegId) {
		UserApplicationRegistration uar = am.getUserApplicationRegistration(appRegId);
		return (uar != null && uar.getApplicationRole() != null ? uar.getApplicationRole().getRoles() : new ArrayList<Role>());
	}

	/**
	 * Retrieve admin roles assigned to given user identified by users registration id with selected application.
	 * 
	 * @param appRegId
	 *            user's registration id with selected application.
	 * @return
	 */
	@Path(USERS + "/{" + PATH_PARAM_APP_REG_ID + "}/" + ADMIN_ROLES)
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Role> getAdminRoles(@PathParam(PATH_PARAM_APP_REG_ID) String appRegId) {
		UserApplicationRegistration uar = am.getUserApplicationRegistration(appRegId);

		return (uar != null && uar.getAdminRole() != null ? uar.getAdminRole().getRoles() : new ArrayList<Role>());
	}

	/**
	 * Overwrite application roles assigned to given user identified by users registration id with selected application.
	 * 
	 * @param roleIds
	 *            list of application role ids
	 * @param appRegId
	 *            user's registration id with selected application.
	 * @throws InvalidOperationException
	 */
	@Path(USERS + "/{" + PATH_PARAM_APP_REG_ID + "}/" + ROLES)
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public void updateRoles(List<String> roleIds, @PathParam(PATH_PARAM_APP_REG_ID) String appRegId) throws InvalidOperationException {
		UserApplicationRegistration uar = am.getUserApplicationRegistration(appRegId);
		am.updateUserApplicationRoleIds(uar, roleIds);
	}

	/**
	 * Overwrite application admin roles assigned to given user identified by users registration id with selected
	 * application.
	 * 
	 * @param roleIds
	 *            list of application admin role ids.
	 * @param appRegId
	 *            user's registration id with selected application.
	 * @throws InvalidOperationException
	 */
	@Path(USERS + "/{" + PATH_PARAM_APP_REG_ID + "}/" + ADMIN_ROLES)
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public void updateAdminRoles(List<String> roleIds, @PathParam(PATH_PARAM_APP_REG_ID) String appRegId) throws InvalidOperationException {
		UserApplicationRegistration uar = am.getUserApplicationRegistration(appRegId);
		am.updateUserApplicationAdminRoleIds(uar, roleIds);
	}

	/**
	 * Retrieve list of application level roles, in the context of selected application.
	 * 
	 * @param sortOrder
	 *            sort order either ASC or DESC
	 * @param sortColumn
	 *            sort column on which result will be sorted
	 * @param startIndex
	 *            start index of the record
	 * @param pagesize
	 *            no of records to fetch in given request
	 * @param search
	 *            search string to search records containing given string
	 * @return
	 */
	@Path(ROLES)
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public PaginatedEntity<Role> getApplicationRoles(@DefaultValue("ASC") @QueryParam(PARAM_SORT_ORDER) SortOrder sortOrder, @DefaultValue("name") @QueryParam(PARAM_SORT_COLUMN) String sortColumn,
			@DefaultValue("0") @QueryParam(PARAM_START_INDEX) int startIndex, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(PARAM_PAGE_SIZE) int pagesize, @QueryParam(PARAM_SEARCH_KEY) String search) {
		PageNSort ps = new PageNSort(new Page(startIndex, pagesize), new Sort(sortColumn, sortOrder));
		List<Role> roles = rm.getRolesByApplication(ps, AuthLevel.APPLICATION, search);
		if (roles != null) {
			ps.getPage().setPageSize(roles.size());
		} else {
			ps.getPage().setPageSize(0);
		}
		return new PaginatedEntity<Role>(ps, roles);
	}

	/**
	 * Retrieve list of application level admin roles.
	 * 
	 * @param sortOrder
	 *            sort order either ASC or DESC
	 * @param sortColumn
	 *            sort column on which result will be sorted
	 * @param startIndex
	 *            start index of the record
	 * @param pagesize
	 *            no of records to fetch in given request
	 * @param search
	 *            search string to search records containing given string
	 * @return
	 */
	@Path(ADMIN_ROLES)
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public PaginatedEntity<Role> getAdminApplicationRoles(@DefaultValue("ASC") @QueryParam(PARAM_SORT_ORDER) SortOrder sortOrder,
			@DefaultValue("name") @QueryParam(PARAM_SORT_COLUMN) String sortColumn, @DefaultValue("0") @QueryParam(PARAM_START_INDEX) int startIndex,
			@DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(PARAM_PAGE_SIZE) int pagesize, @QueryParam(PARAM_SEARCH_KEY) String search) {
		PageNSort ps = new PageNSort(new Page(startIndex, pagesize), new Sort(sortColumn, sortOrder));
		List<Role> roles = am.getAdminRoles(ps, search);
		if (roles != null) {
			ps.getPage().setPageSize(roles.size());
		} else {
			ps.getPage().setPageSize(0);
		}
		return new PaginatedEntity<Role>(ps, roles);
	}
}
