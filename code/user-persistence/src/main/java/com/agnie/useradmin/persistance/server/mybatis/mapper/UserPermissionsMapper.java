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
package com.agnie.useradmin.persistance.server.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserPermissionsMapper {

	/**
	 * Retrieve all application level admin permissions of logged in user identified by sessionId for given domain.
	 * 
	 * @param sessionId
	 *            current session id of logged in user.
	 * @param domain
	 *            domain for which admin permissions need to be retrieved.
	 * @return
	 */
	@Select("SELECT P.CODE FROM USERAUTHSESSION us JOIN UserAdmin.USER u ON u.ID = us.USERID AND us.ID = #{sessionId} JOIN USERAPPLICATIONREGISTRATION aur ON aur.USER_ID = u.ID AND aur.APP_ID = (SELECT ID FROM APPLICATION app WHERE app.DOMAIN = #{domain}) JOIN ADMINROLE AR ON AR.USERAPPREGISTRATION_ID=aur.ID JOIN UserAdmin.ADMINROLE_ROLE ARR ON ARR.AdminRole_ID = AR.ID JOIN UserAdmin.ROLE R ON R.ID = ARR.roles_ID JOIN ROLE_PERMISSION RP ON RP.Role_ID=R.ID JOIN PERMISSION P ON P.ID= RP.permissions_ID")
	List<String> getAllAdminPermissionsByUserAndDomain(@Param("sessionId") String sessionId, @Param("domain") String domain);

	/**
	 * Retrieve all context level admin permissions of logged in user identified by sessionId for given domain and
	 * selected application
	 * 
	 * @param sessionId
	 *            current session id of logged in user
	 * @param domain
	 *            selected domain
	 * @param context
	 *            selected context
	 * @return
	 */
	@Select("SELECT P.CODE FROM USERAUTHSESSION us JOIN UserAdmin.USER u ON u.ID = us.USERID AND us.ID = #{sessionId} JOIN USERAPPLICATIONREGISTRATION aur ON aur.USER_ID = u.ID AND aur.APP_ID = (SELECT ID FROM APPLICATION app WHERE app.DOMAIN = #{domain}) JOIN USERAPPLICATIONCTXREGISTRATION uacr ON uacr.USAPPLICATIONREGISTRATION_ID = aur.ID AND uacr.CONTEXT_ID = (SELECT ID FROM CONTEXT ctx WHERE ctx.NAME = #{context}) JOIN ADMINCONTEXTROLE ACR ON ACR.USAPPLICATIONCTXREGISTRATION_ID = uacr.ID JOIN ADMINCONTEXTROLE_ROLE ACRR ON ACRR.AdminContextRole_ID = ACR.ID    JOIN ROLE R ON R.ID = ACRR.roles_ID JOIN ROLE_PERMISSION RP ON RP.Role_ID = R.ID JOIN PERMISSION P ON P.ID = RP.permissions_ID")
	List<String> getAllAdminPermissionsByUserDomainAndContext(@Param("sessionId") String sessionId, @Param("domain") String domain, @Param("context") String context);
}
