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
package com.agnie.useradmin.persistance.shared.service;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

public interface MVPRequestFactory extends RequestFactory {

	ApplicationManagerRequest applicationManager();
	
	ContextManagerRequest contextManager();
	
	PermissionManagerRequest permissionManager();

	UserManagerRequest userManager();
	
	RoleManagerRequest roleManager();
	//
	// ApplicationRequest applicationRequest();
	//
	// UserRequest userRequest();
	//
	// PermissionRequest permissionRequest();
	//
	// UserManagerRequest userManager();
	//
	// RoleManagerRequest roleManager();
	//
	// RoleRequest roleRequest();
	//
	// ApplicationUserRoleRequest applicationUserRoleRequest();
	//
	// ApplicationRoleRequest applicationRoleRequest();
	//
	// AdminRoleRequest adminRoleRequest();
}
