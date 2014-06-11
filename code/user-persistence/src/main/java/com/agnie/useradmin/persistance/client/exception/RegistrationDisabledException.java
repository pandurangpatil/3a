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

/**
 * @author Pandurang Patil 27-Feb-2014
 * 
 */
public class RegistrationDisabledException extends UserAdminException {

	public RegistrationDisabledException() {
		super(REG_DISABLED);
	}

	private static final long	serialVersionUID	= 1L;

}
