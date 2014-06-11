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
import javax.ws.rs.POST;
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
import com.agnie.useradmin.persistance.server.dao.ContextManager;
import com.agnie.useradmin.persistance.server.dao.RoleManager;
import com.agnie.useradmin.persistance.server.entity.Context;
import com.agnie.useradmin.persistance.server.entity.Role;
import com.agnie.useradmin.persistance.server.entity.UserApplicationCtxRegistration;
import com.agnie.useradmin.persistance.server.listrequest.Page;
import com.agnie.useradmin.persistance.server.listrequest.PageNSort;
import com.agnie.useradmin.persistance.server.listrequest.Sort;
import com.agnie.useradmin.service.client.injector.RestConstants;
import com.agnie.useradmin.service.injector.UserAdminJerseyServletModule;
import com.agnie.useradmin.service.rest.bean.PaginatedEntity;
import com.google.inject.Inject;

@Path("/" + UserAdminJerseyServletModule.REST_BASE + "/" + ContextResource.CONTEXT_BASE)
public class ContextResource implements RestConstants {

	@Inject
	private RoleManager		rm;

	@Inject
	private ContextManager	cm;

	/**
	 * Create Context
	 * 
	 * @param context
	 *            Context
	 * @throws UserAdminException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public String create(Context context) throws UserAdminException {
		Context ctx = cm.create(context);
		return ctx.getId();
	}

	/**
	 * Retrieve paginated list of contexts created with selected application.
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
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public PaginatedEntity<Context> getContexts(@DefaultValue("ASC") @QueryParam(PARAM_SORT_ORDER) SortOrder sortOrder, @DefaultValue("name") @QueryParam(PARAM_SORT_COLUMN) String sortColumn,
			@DefaultValue("0") @QueryParam(PARAM_START_INDEX) int startIndex, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(PARAM_PAGE_SIZE) int pagesize, @QueryParam(PARAM_SEARCH_KEY) String search) {
		PageNSort ps = new PageNSort(new Page(startIndex, pagesize), new Sort(sortColumn, sortOrder));
		List<Context> contexts = cm.list(ps, search);
		if (contexts != null) {
			ps.getPage().setPageSize(contexts.size());
		} else {
			ps.getPage().setPageSize(0);
		}
		return new PaginatedEntity<Context>(ps, contexts);
	}

	/**
	 * Retrieve paginated list of users registered with selected context of selected application.
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
	public PaginatedEntity<UserApplicationCtxRegistration> getUsers(@DefaultValue("ASC") @QueryParam(PARAM_SORT_ORDER) SortOrder sortOrder,
			@DefaultValue("updateOn") @QueryParam(PARAM_SORT_COLUMN) String sortColumn, @DefaultValue("0") @QueryParam(PARAM_START_INDEX) int startIndex,
			@DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(PARAM_PAGE_SIZE) int pagesize, @QueryParam(PARAM_STATUS) RequestStatus status, @QueryParam(PARAM_SEARCH_KEY) String search) {

		PageNSort ps = new PageNSort(new Page(startIndex, pagesize), new Sort(sortColumn, sortOrder));
		List<UserApplicationCtxRegistration> users = cm.getUsersByContext(ps, status, search);
		if (users != null) {
			ps.getPage().setPageSize(users.size());
		} else {
			ps.getPage().setPageSize(0);
		}
		return new PaginatedEntity<UserApplicationCtxRegistration>(ps, users);

	}

	/**
	 * update given user's registration status with selected context of selected application.
	 * 
	 * @param ctxRegIds
	 *            user's registration id with selected context of selected application.
	 * @param status
	 */
	@Path(USERS)
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public void updateStatus(List<String> ctxRegIds, @QueryParam(PARAM_STATUS) RequestStatus status) {
		cm.updateStatusByUserAppCtxRegId(ctxRegIds, status);
	}

	/**
	 * Mark given user's registration with selected context of selected application as deleted.
	 * 
	 * @param ctxRegIds
	 *            user's registration id with selected context of selected application.
	 */
	@Path(USERS)
	@DELETE
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public void deleteUser(List<String> ctxRegIds) {
		cm.markDisabledByUserAppCtxRegId(ctxRegIds);
	}

	/**
	 * Retrieve user details from users registration id with selected context of selected application
	 * 
	 * @param ctxRegId
	 *            user's registration id with selected context of selected application.
	 * @return
	 */
	@Path(USERS + "/{" + PATH_PARAM_CTX_REG_ID + "}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public UserApplicationCtxRegistration getUser(@PathParam(PATH_PARAM_CTX_REG_ID) String ctxRegId) {
		return cm.getUserApplicationCtxRegistration(ctxRegId);
	}

	/**
	 * Retrieve context roles assigned to given user identified by users registration id with selected context of
	 * selected application
	 * 
	 * @param ctxRegId
	 *            user's registration id with selected context of selected application.
	 * @return
	 */
	@Path(USERS + "/{" + PATH_PARAM_CTX_REG_ID + "}/" + ROLES)
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Role> getRoles(@PathParam(PATH_PARAM_CTX_REG_ID) String ctxRegId) {
		UserApplicationCtxRegistration uacr = cm.getUserApplicationCtxRegistration(ctxRegId);
		return (uacr != null && uacr.getContextRole() != null ? uacr.getContextRole().getRoles() : new ArrayList<Role>());
	}

	/**
	 * Retrieve context admin roles assigned to given user identified by users registration id with selected context of
	 * selected application
	 * 
	 * @param ctxRegId
	 *            user's registration id with selected context of selected application.
	 * @return
	 */
	@Path(USERS + "/{" + PATH_PARAM_CTX_REG_ID + "}/" + ADMIN_ROLES)
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Role> getAdminRoles(@PathParam(PATH_PARAM_CTX_REG_ID) String ctxRegId) {
		UserApplicationCtxRegistration uacr = cm.getUserApplicationCtxRegistration(ctxRegId);
		return (uacr != null && uacr.getAdminContextRole() != null ? uacr.getAdminContextRole().getRoles() : new ArrayList<Role>());
	}

	/**
	 * Overwrite context roles assigned to given user identified by users registration id with selected context of
	 * selected application.
	 * 
	 * @param roleIds
	 *            list of application role ids
	 * @param ctxRegId
	 *            user's registration id with selected context of selected application.
	 * @throws InvalidOperationException
	 */
	@Path(USERS + "/{" + PATH_PARAM_CTX_REG_ID + "}/" + ROLES)
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public void updateRoles(List<String> roleIds, @PathParam(PATH_PARAM_CTX_REG_ID) String ctxRegId) throws InvalidOperationException {
		UserApplicationCtxRegistration uacr = cm.getUserApplicationCtxRegistration(ctxRegId);
		cm.updateUserApplicationRoleIds(uacr, roleIds);
	}

	/**
	 * Overwrite context admin roles assigned to given user identified by users registration id with selected context of
	 * selected application.
	 * 
	 * @param roleIds
	 *            list of application admin role ids.
	 * @param ctxRegId
	 *            user's registration id with selected context of selected application.
	 * @throws InvalidOperationException
	 */
	@Path(USERS + "/{" + PATH_PARAM_CTX_REG_ID + "}/" + ADMIN_ROLES)
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public void updateAdminRoles(List<String> roleIds, @PathParam(PATH_PARAM_CTX_REG_ID) String ctxRegId) throws InvalidOperationException {
		UserApplicationCtxRegistration uacr = cm.getUserApplicationCtxRegistration(ctxRegId);
		cm.updateUserConextAdminRoleIds(uacr, roleIds);
	}

	/**
	 * Retrieve list of Context level roles in the context of selected application.
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
	public PaginatedEntity<Role> getContextRoles(@DefaultValue("ASC") @QueryParam(PARAM_SORT_ORDER) SortOrder sortOrder, @DefaultValue("name") @QueryParam(PARAM_SORT_COLUMN) String sortColumn,
			@DefaultValue("0") @QueryParam(PARAM_START_INDEX) int startIndex, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(PARAM_PAGE_SIZE) int pagesize, @QueryParam(PARAM_SEARCH_KEY) String search) {
		PageNSort ps = new PageNSort(new Page(startIndex, pagesize), new Sort(sortColumn, sortOrder));
		List<Role> roles = rm.getRolesByApplication(ps, AuthLevel.CONTEXT, search);
		if (roles != null) {
			ps.getPage().setPageSize(roles.size());
		} else {
			ps.getPage().setPageSize(0);
		}
		return new PaginatedEntity<Role>(ps, roles);
	}

	/**
	 * Retrieve list of context level admin roles.
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
	public PaginatedEntity<Role> getAdminContextRoles(@DefaultValue("ASC") @QueryParam(PARAM_SORT_ORDER) SortOrder sortOrder, @DefaultValue("name") @QueryParam(PARAM_SORT_COLUMN) String sortColumn,
			@DefaultValue("0") @QueryParam(PARAM_START_INDEX) int startIndex, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(PARAM_PAGE_SIZE) int pagesize, @QueryParam(PARAM_SEARCH_KEY) String search) {
		PageNSort ps = new PageNSort(new Page(startIndex, pagesize), new Sort(sortColumn, sortOrder));
		List<Role> roles = cm.getAdminRoles(ps, search);
		if (roles != null) {
			ps.getPage().setPageSize(roles.size());
		} else {
			ps.getPage().setPageSize(0);
		}
		return new PaginatedEntity<Role>(ps, roles);
	}
}
