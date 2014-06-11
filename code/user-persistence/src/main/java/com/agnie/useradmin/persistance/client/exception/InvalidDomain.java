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
 * to throw exception in case given domain url is invalid or the given domain is not registered with user admin
 * 
 */
public class InvalidDomain extends UserAdminException {

	private static final long	serialVersionUID	= 6651001041607163357L;

	public InvalidDomain() {
		super(INVALID_DOMAIN);
	}
}
