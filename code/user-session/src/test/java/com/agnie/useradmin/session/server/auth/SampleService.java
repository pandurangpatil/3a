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

import com.google.inject.Singleton;

@RistrictedResource
@Singleton
public class SampleService {

	@RequirePermissions(permissionExpression = "perm_yahoo_test && perm_google_test")
	public String testOne(String param) {
		return "Hello " + param;
	}

	@RequirePermissions(permissionExpression = "perm_yahoo_no_test && perm_google_test")
	public String testTwo(String param) {
		return "Hello " + param;
	}

	public String testThree(String param) {
		return "Hello " + param;
	}

}
