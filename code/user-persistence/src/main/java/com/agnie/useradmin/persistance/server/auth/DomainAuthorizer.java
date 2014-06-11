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

public class DomainAuthorizer extends Authorizer {
	public static final String		SELECTED_DOMAIN		= "sel-domain";
	public static final String		SELECTED_DOMAIN_ACL	= "selected-domain-acl";
	@Inject
	@Named(SELECTED_DOMAIN_ACL)
	private Provider<ACLContext>	selectedDomainAcl;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		ACLContext aclCtx = selectedDomainAcl.get();
		return validate(invocation, aclCtx);
	}
}
