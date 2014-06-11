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
/**
 * 
 */
package com.agnie.useradmin.persistance.server.auth;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.useradmin.persistance.server.entity.User;
import com.agnie.useradmin.session.server.auth.Authorizer;
import com.agnie.useradmin.session.server.injector.SessionServletModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * User Authorizer will use Application level ACL context to authorize user
 */
public class UserAuthorizer extends Authorizer {
	@Inject
	@Named(SessionServletModule.CURRENT_USER)
	Provider<UserAccount>	curntUser;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if (curntUser.get() != null) {
			Method method = invocation.getMethod();
			Object[] parms = invocation.getArguments();
			Annotation[][] paramAnnotations = method.getParameterAnnotations();
			if (paramAnnotations != null && paramAnnotations.length > 0) {
				int paramIndex = 0;
				for (; paramIndex < paramAnnotations.length; paramIndex++) {
					for (Annotation annotation : paramAnnotations[paramIndex]) {
						if (annotation instanceof UserData) {
							String changeUserId = null;
							if (parms[paramIndex] instanceof User) {
								changeUserId = ((User) parms[paramIndex]).getId();
							} else if (parms[paramIndex] instanceof String) {
								changeUserId = (String) parms[paramIndex];
							}
							if (curntUser.get().getId().equals(changeUserId) || (curntUser.get().getUserName() != null && curntUser.get().getUserName().equals(changeUserId))) {
								// Logged in user is changing his/her own data. So permission check will be bypassed and
								// user will be allowed to change his own data.
								return invocation.proceed();
							}
							break;
						}
					}
				}
			}
		}
		return super.invoke(invocation);
	}
}
