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
package com.agnie.useradmin.persistance.server.common.digester;

import org.apache.commons.beanutils.Converter;

import com.agnie.useradmin.persistance.client.enums.AuthLevel;

public class AuthLevelConvert implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public Object convert(Class type, Object value) {
		if (value == null)
			return null;
		if (type == null)
			return null;
		if (value instanceof String)
			return AuthLevel.valueOf((String) value);
		return null;
	}

}
