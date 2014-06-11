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

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.useradmin.persistance.client.exception.UserAdminException;
import com.agnie.useradmin.persistance.server.auth.DomainAuthorizer;
import com.agnie.useradmin.persistance.server.dao.ContextManager;
import com.agnie.useradmin.persistance.server.dao.UserManager;
import com.agnie.useradmin.persistance.server.entity.Context;
import com.agnie.useradmin.persistance.server.entity.UserApplicationCtxRegistration;
import com.agnie.useradmin.persistance.server.entity.UserApplicationRegistration;
import com.agnie.useradmin.service.client.injector.RestConstants;
import com.agnie.useradmin.service.injector.UserAdminJerseyServletModule;
import com.agnie.useradmin.session.server.dao.UserSessionDao;
import com.agnie.useradmin.session.server.entity.UserPermissions;
import com.agnie.useradmin.session.server.injector.SessionServletModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

@Path("/" + UserAdminJerseyServletModule.REST_BASE + "/" + UserProfileResource.USER_PROF_BASE)
public class UserProfileResource implements RestConstants {

	@Inject
	private UserManager				um;
	@Inject
	private ContextManager			cm;

	@Inject
	@Named(SessionServletModule.CURRENT_USER)
	private Provider<UserAccount>	curntUser;
	@Inject
	private UserSessionDao			usd;
	@Inject
	@Named(DomainAuthorizer.SELECTED_DOMAIN)
	private Provider<String>		domain;
	@Inject
	@Named(SessionServletModule.SESSION_ID)
	private Provider<String>		sessionId;

	/**
	 * Retrieve logged in user details with users registration details with application in context
	 * 
	 * @return
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public UserApplicationRegistration getApplicationProfile() {
		UserApplicationRegistration uar = um.getUserAppReg(domain.get(), curntUser.get().getId());
		return uar;
	}

	@Path(CONTEXT)
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Context> getRegContexts() {

		return cm.getRegisteredContexts();
	}

	@Path(CONTEXT + "/{" + PATH_PARAM_CONTEXT + "}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public UserApplicationCtxRegistration getContextProfile(@PathParam(PATH_PARAM_CONTEXT) String context) {
		return um.getUserAppContextReg(domain.get(), context, curntUser.get().getId());
	}

	/**
	 * Register logged in user with given context of selected application.
	 * 
	 * @param context
	 * @return
	 * @throws UserAdminException
	 */
	@Path(CONTEXT + "/{" + PATH_PARAM_CONTEXT + "}")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public void contxtRegistration(@PathParam(PATH_PARAM_CONTEXT) String context) throws UserAdminException {

		um.registerWithContext(context, curntUser.get().getId());
	}

	/**
	 * Retrieve assigned permissions of logged in user to selected application.
	 * 
	 * @return
	 */
	@Path(PERMISSIONS)
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public UserPermissions getPermissions() {
		return usd.getPermissionsForDomain(domain.get(), sessionId.get());
	}

	/**
	 * Retrieve assigned permissions of logged in user to given context of selected application.
	 * 
	 * @param context
	 * @return
	 */
	@Path(CONTEXT + "/{" + PATH_PARAM_CONTEXT + "}/" + PERMISSIONS)
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public UserPermissions getContextPermissions(@PathParam(PATH_PARAM_CONTEXT) String context) {
		return usd.getPermissionsForContext(domain.get(), context, sessionId.get());
	}
}
