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
package com.agnie.useradmin.session.server.auth;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import com.agnie.common.server.auth.ACLContext;
import com.agnie.useradmin.session.client.helper.UserNotAutherisedException;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * A generic implementation of Authoriser to check for required permission on logged in user.
 */
public class Authorizer implements MethodInterceptor {
	private static org.apache.log4j.Logger	logger		= Logger.getLogger(Authorizer.class);

	public final static String				APP_ACL_CTX	= "app-acl-ctx";

	@Inject
	@Named(APP_ACL_CTX)
	private Provider<ACLContext>			appAclCtx;

	private PermissionExpressionEvaluator	pee			= new PermissionExpressionEvaluator();

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		ACLContext aclCtx = appAclCtx.get();
		return validate(invocation, aclCtx);
	}

	protected Object validate(MethodInvocation invocation, ACLContext aclCtx) throws Throwable {
		if (aclCtx != null) {
			RequirePermissions rps = invocation.getMethod().getAnnotation(RequirePermissions.class);
			if (rps == null) {
				// No annotation is applied that means to call this method user don't require any permission.
				return invocation.proceed();
			} else {
				String permExp = rps.permissionExpression();
				aclCtx.clear();
				if (pee.evaluatePermissionExp(permExp, aclCtx)) {
					return invocation.proceed();
				} else {
					throw new UserNotAutherisedException(aclCtx.getCheckPerms());
				}
			}
		} else {
			logger.error("ACLContext is not set");
			throw new UserNotAutherisedException();
		}
	}
}
