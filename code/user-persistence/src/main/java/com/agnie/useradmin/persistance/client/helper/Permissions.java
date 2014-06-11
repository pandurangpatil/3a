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
package com.agnie.useradmin.persistance.client.helper;

/**
 * All Permissions related constants in this class
 */
public class Permissions {

	// User related permissions

	public static final String	USER								= "perm_user";
	public static final String	CREATE_USER							= "perm_user_create";
	public static final String	VIEW_USER							= "perm_user_view";
	public static final String	DELETE_USER							= "perm_user_delete";
	// This is just a dummy permission. Only logged in user him self can register to any context.
	public static final String	REGISTER_TO_CTX						= "perm_user_reg_ctx";
	public static final String	EDIT_USER							= "perm_user_edit";
	public static final String	EDIT_USER_DETAIL					= "perm_user_edit_general";
	public static final String	RESET_USER_PASSWORD					= "perm_user_edit_resetpwd";

	public static final String	APPLICATION							= "perm_app";
	public static final String	CREATE_APPLICATION					= "perm_app_create";
	// Delete permission will not be present in the system only owner of the application will be able to delete the
	// application
	// Application / Domain level permissions
	public static final String	APPLICATION_OWNER					= "perm_app_owner";
	public static final String	DELETE_APPLICATION					= "perm_app_delete";
	public static final String	DOMAIN_SETTINGS						= "perm_app_edit_setting";
	public static final String	VIEW_APPLICATION_DETAILS			= "perm_app_edit_setting_view";
	public static final String	EDIT_APPLICATION_DETAILS			= "perm_app_edit_setting_edit_gen";
	public static final String	MANAGE_CONTEXT						= "perm_app_edit_ctxt";
	public static final String	CREATE_CONTEXT						= "perm_app_edit_ctxt_create";
	public static final String	LIST_CONTEXT						= "perm_app_edit_ctxt_list";
	public static final String	ADMIN_DELETE_CONTEXT				= "perm_app_edit_ctxt_delete";

	public static final String	ROLE								= "perm_app_edit_role";
	public static final String	CREATE_ROLE							= "perm_app_edit_role_create";
	public static final String	EDIT_ROLE							= "perm_app_edit_role_edit";
	public static final String	VIEW_ROLE							= "perm_app_edit_role_view";
	public static final String	DELETE_ROLE							= "perm_app_edit_role_delete";

	public static final String	PERMISSION							= "perm_app_edit_permission";
	public static final String	CREATE_PERMISSION					= "perm_app_edit_permission_create";
	public static final String	EDIT_PERMISSION						= "perm_app_edit_permission_edit";
	public static final String	VIEW_PERMISSION						= "perm_app_edit_permission_view";
	public static final String	DELETE_PERMISSION					= "perm_app_edit_permission_delete";

	public static final String	APPLICATION_USER_MANAGER			= "perm_app_edit_user";
	public static final String	APPLICATION_USER_REG				= "perm_app_edit_user_reg";
	public static final String	APPLICATION_USER_DEREG				= "perm_app_edit_user_dereg";
	public static final String	APPLICATION_USER_ROLE_MANAGER		= "perm_app_edit_user_rolemgmt";
	public static final String	APPLICATION_USER_ADMIN_ROLE_MANAGER	= "perm_app_edit_user_admin_rolemgmt";

	// Applcation's context level Permissions
	// Note: Please note that create context is not contextual level permission once context is created then context
	// level permissions comes into picture. Context view is not applicable for individual context level permission.
	public static final String	CONTEXT_OWNER						= "perm_app_edit_ctxt_owner";
	public static final String	CONTEXT								= "perm_app_edit_ctxt";
	public static final String	CONTEXT_SETTINGS					= "perm_app_edit_ctxt_edit_setting";
	public static final String	VIEW_CONTEXT						= "perm_app_edit_ctxt_edit_setting_view";
	public static final String	EDIT_CONTEXT						= "perm_app_edit_ctxt_edit_setting_edit";

	public static final String	DELETE_CONTEXT						= "perm_app_edit_ctxt_delete";
	public static final String	CONTEXT_USER_MANAGER				= "perm_app_edit_ctxt_edit_user";
	public static final String	CONTEXT_USER_REG					= "perm_app_edit_ctxt_edit_user_reg";
	public static final String	CONTEXT_USER_DEREG					= "perm_app_edit_ctxt_edit_user_dereg";
	public static final String	CONTEXT_USER_ROLE_MANAGER			= "perm_app_edit_ctxt_edit_user_rolemgmt";

	public static final String	CONTEXT_USER_ADMIN_ROLE_MANAGER		= "perm_app_edit_ctxt_edit_user_admin_rolemgmt";

}
