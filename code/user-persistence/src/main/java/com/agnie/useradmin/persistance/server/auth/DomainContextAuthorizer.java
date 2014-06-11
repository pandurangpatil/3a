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
package com.agnie.useradmin.persistance.server.auth;

import org.aopalliance.intercept.MethodInvocation;

import com.agnie.common.server.auth.ACLContext;
import com.agnie.useradmin.session.server.auth.Authorizer;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

public class DomainContextAuthorizer extends Authorizer {
	public static final String		SELECTED_CONTEXT			= "sel-context";
	public static final String		SELECTED_DOMAIN_CONTEXT_ACL	= "selected-domain-context-acl";

	@Inject
	@Named(SELECTED_DOMAIN_CONTEXT_ACL)
	private Provider<ACLContext>	selectedDomainAclCtx;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		ACLContext aclCtx = selectedDomainAclCtx.get();
		return validate(invocation, aclCtx);
	}
}
