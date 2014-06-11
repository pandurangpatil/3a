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
package com.agnie.common.util.client.tablefile;

/**
 * 
 */
public class InvalidColumnValueException extends RuntimeException {

	/**
     *
     */
	private static final long	serialVersionUID	= 1L;
	private String				header;
	private String				invalidValue;

	public InvalidColumnValueException(String header, String invalidValue, Throwable cause) {
		this(header, invalidValue, null, cause);
	}

	public InvalidColumnValueException(String header, String invalidValue) {
		this(header, invalidValue, null, null);
	}

	/**
	 * @param header
	 * @param invalidValue
	 * @param msg
	 */
	public InvalidColumnValueException(String header, String invalidValue, String msg) {
		this(header, invalidValue, msg, null);
	}

	/**
	 * @param header
	 * @param lineNumber
	 * @param invalidValue
	 */
	public InvalidColumnValueException(String header, String invalidValue, String msg, Throwable cause) {
		super(msg, cause);
		this.header = header;
		this.invalidValue = invalidValue;
	}

	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @return the invalidValue
	 */
	public String getInvalidValue() {
		return invalidValue;
	}

}
