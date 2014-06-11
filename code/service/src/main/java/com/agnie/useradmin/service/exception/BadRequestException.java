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
package com.agnie.useradmin.service.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.agnie.useradmin.persistance.client.exception.UserAdminException;
import com.agnie.useradmin.service.client.entity.Error;
import com.agnie.useradmin.session.client.helper.UserSessionException;

public class BadRequestException extends WebApplicationException {

	private static final long	serialVersionUID	= 1L;

	public BadRequestException(UserAdminException exception) {
		// TODO: Need to add mechanism to read error messages from properties file with key as error code and respond
		// with code and message.
		super(Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(new GenericEntity<Error>(new Error(exception.getCode())) {
		}).build());
	}

	public BadRequestException(UserSessionException exception) {
		// TODO: Need to add mechanism to read error messages from properties file with key as error code and respond
		// with code and message.
		super(Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(new GenericEntity<Error>(new Error(exception.getCode())) {
		}).build());
	}

	public BadRequestException(Status status, Error error) {
		// TODO: Need to add mechanism to read error messages from properties file with key as error code and respond
		// with code and message.
		super(Response.status(status).type(MediaType.APPLICATION_JSON).entity(new GenericEntity<Error>(error) {
		}).build());
	}

}
