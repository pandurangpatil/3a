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
/**
 * 
 */
package com.agnie.common.gwt.serverclient.client.helper;

/**
 * 
 */
public class InvalidPermission extends RuntimeException {

	/**
     * 
     */
	private static final long	serialVersionUID	= 1L;

	/**
     * 
     */
	public InvalidPermission() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public InvalidPermission(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public InvalidPermission(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidPermission(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
