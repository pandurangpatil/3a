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
package com.agnie.useradmin.session.server.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;

public interface UserAccountMapper {

	/**
	 * Get logged in user details from current session id.
	 * 
	 * @param sessionId
	 *            current session id of logged in user.
	 * @return
	 */
	@Select("SELECT u.ID as Id, u.TITLE as Title, u.FIRSTNAME as FirstName, u.LASTNAME as LastName, u.DEFAULTLANGUAGE as Language, u.USERNAME as UserName, u.PROFILEIMAGE as UserImgUrl FROM USERAUTHSESSION us JOIN USER u ON u.id=us.USERID and us.ID=#{sessionId}")
	UserAccount getUserAccount(@Param("sessionId") String sessionId);

	/**
	 * Check if logged in user identified by the session id owns the domain.
	 * 
	 * @param sessionId
	 *            current session id of logged in user.
	 * @param domain
	 *            domain of the application whose ownership will be checked.
	 * @return
	 */
	@Select("SELECT aur.OWNER FROM USERAUTHSESSION us JOIN USER u ON u.ID = us.USERID AND us.ID = #{sessionId} JOIN USERAPPLICATIONREGISTRATION aur ON aur.USER_ID = u.ID AND aur.APP_ID = (SELECT ID FROM APPLICATION app WHERE app.DOMAIN = #{domain})")
	Integer doesUserOwnsTheDomain(@Param("sessionId") String sessionId, @Param("domain") String domain);

	/**
	 * Retrieve all application permissions of logged in user identified by sessionId for given domain.
	 * 
	 * @param sessionId
	 *            current session id of logged in user.
	 * @param domain
	 *            domain for which permissions need to be retrieved.
	 * @return
	 */
	@Select("SELECT P.CODE FROM USERAUTHSESSION us JOIN USER u ON u.ID = us.USERID AND us.ID = #{sessionId} JOIN USERAPPLICATIONREGISTRATION aur ON aur.USER_ID = u.ID AND aur.APP_ID = (SELECT ID FROM APPLICATION app WHERE app.DOMAIN = #{domain}) JOIN APPLICATIONROLE AR ON AR.USERAPPREGISTRATION_ID=aur.ID JOIN APPLICATIONROLE_ROLE ARR ON ARR.ApplicationRole_ID = AR.ID JOIN ROLE R ON R.ID = ARR.roles_ID JOIN ROLE_PERMISSION RP ON RP.Role_ID=R.ID JOIN PERMISSION P ON P.ID= RP.permissions_ID")
	List<String> getAllPermissionsByUserAndDomain(@Param("sessionId") String sessionId, @Param("domain") String domain);

	/**
	 * Check if logged in user identified by session id owns given context.
	 * 
	 * @param sessionId
	 *            Session Id of logged in user
	 * @param domain
	 *            Selected domain
	 * @param context
	 *            Selected context
	 * @return
	 */
	@Select("SELECT uacr.OWNER FROM USERAUTHSESSION us JOIN USER u ON u.ID = us.USERID AND us.ID = #{sessionId} JOIN USERAPPLICATIONREGISTRATION aur ON aur.USER_ID = u.ID AND aur.APP_ID = (SELECT ID FROM APPLICATION app WHERE app.DOMAIN = #{domain}) JOIN USERAPPLICATIONCTXREGISTRATION uacr ON uacr.USAPPLICATIONREGISTRATION_ID = aur.ID AND uacr.CONTEXT_ID = (SELECT ID FROM CONTEXT ctx WHERE ctx.NAME = #{context})")
	Integer doesUserOwnsTheContext(@Param("sessionId") String sessionId, @Param("domain") String domain, @Param("context") String context);

	/**
	 * Retrieve all context level permissions in the context of give application for a logged in user.
	 * 
	 * @param sessionId
	 *            current session id of logged in user
	 * @param domain
	 *            selected domain
	 * @param context
	 *            selected context.
	 * @return
	 */
	@Select("SELECT P.CODE FROM USERAUTHSESSION us JOIN USER u ON u.ID = us.USERID AND us.ID = #{sessionId} JOIN USERAPPLICATIONREGISTRATION aur ON aur.USER_ID = u.ID AND aur.APP_ID = (SELECT ID FROM APPLICATION app WHERE app.DOMAIN = #{domain}) JOIN USERAPPLICATIONCTXREGISTRATION uacr ON uacr.USAPPLICATIONREGISTRATION_ID = aur.ID AND uacr.CONTEXT_ID = (SELECT ID FROM CONTEXT ctx WHERE ctx.NAME = #{context}) JOIN CONTEXTROLE CR ON CR.USAPPLICATIONCTXREGISTRATION_ID = uacr.ID JOIN CONTEXTROLE_ROLE CRR ON CRR.ContextRole_ID = CR.ID JOIN ROLE R ON R.ID = CRR.roles_ID JOIN ROLE_PERMISSION RP ON RP.Role_ID = R.ID JOIN PERMISSION P ON P.ID = RP.permissions_ID")
	List<String> getAllPermissionsByUserDomainAndContext(@Param("sessionId") String sessionId, @Param("domain") String domain, @Param("context") String context);

}
