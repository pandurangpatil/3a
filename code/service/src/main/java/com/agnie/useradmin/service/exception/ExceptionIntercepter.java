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

import javax.ws.rs.core.Response.Status;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import com.agnie.useradmin.service.client.entity.Error;
import com.agnie.useradmin.persistance.client.exception.UserAdminException;
import com.agnie.useradmin.session.client.helper.UserNotAutherisedException;
import com.agnie.useradmin.session.client.helper.UserSessionException;

public class ExceptionIntercepter implements MethodInterceptor {
	private static Logger	logger	= Logger.getLogger(ExceptionIntercepter.class);

	@Override
	public Object invoke(MethodInvocation method) throws Throwable {
		try {
			return method.proceed();
		} catch (UserAdminException ex) {
			logger.error(ex);
			throw new BadRequestException(ex);
		} catch (UserSessionException ex) {
			logger.error(ex);
			throw new BadRequestException(ex);
		} catch (UserNotAutherisedException ex) {
			logger.error(ex);
			throw new BadRequestException(Status.UNAUTHORIZED, new Error(ex.getCode()));
		} catch (Exception ex) {
			logger.error(ex);
			throw new BadRequestException(Status.INTERNAL_SERVER_ERROR, new Error(UserAdminException.INTERNAL_SERVER_ERROR));
		}

	}

}
