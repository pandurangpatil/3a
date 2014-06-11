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

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.agnie.useradmin.persistance.client.service.dto.Application;

public interface ApplicationMapper {
	@Select("SELECT URL,DOMAIN as Domain, ICONURL as IconURL FROM APPLICATION where DOMAIN=#{domain}")
	public Application getApplicationDetails(@Param("domain") String domain);

	@Select("SELECT APIACCESSKEY FROM APPLICATION where DOMAIN=#{domain}")
	public String getAPIAccessKey(@Param("domain") String domain);
}
