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

import org.aopalliance.intercept.MethodInvocation;

import com.agnie.common.server.auth.ACLContext;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * A generic implementation of Authoriser to check for required permission on logged in user in given context.
 */
public class ContextAuthorizer extends Authorizer {

	public final static String		CONTEXT			= "context";
	public final static String		CONTEXT_ACL_CTX	= "context-acl-ctx";

	@Inject
	@Named(CONTEXT_ACL_CTX)
	private Provider<ACLContext>	contextAclCtx;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		ACLContext aclCtx = contextAclCtx.get();
		return validate(invocation, aclCtx);
	}

}
