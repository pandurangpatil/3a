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
package com.agnie.useradmin.service.exception;

import com.agnie.useradmin.persistance.client.exception.UserAdminException;

public class MissingParameterException extends UserAdminException {

	private static final long	serialVersionUID	= 1L;

	@SuppressWarnings("unused")
	private String				missingParam;

	public MissingParameterException(String missingParam) {
		super(MISSING_PARAMETER);
		this.missingParam = missingParam;
	}
}
