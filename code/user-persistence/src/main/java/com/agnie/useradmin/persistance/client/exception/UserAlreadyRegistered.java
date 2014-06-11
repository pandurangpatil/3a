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
package com.agnie.useradmin.persistance.client.exception;

import com.agnie.useradmin.persistance.client.enums.RequestStatus;

/**
 * @author Pandurang Patil 27-Feb-2014
 * 
 */
public class UserAlreadyRegistered extends UserAdminException {

	private RequestStatus	status;

	public UserAlreadyRegistered(RequestStatus status) {
		super(USER_ALREADY_REG);
		this.status = status;
	}

	private static final long	serialVersionUID	= 1L;

	/**
	 * @return the status
	 */
	public RequestStatus getStatus() {
		return status;
	}

}
