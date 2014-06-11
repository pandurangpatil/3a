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
package com.agnie.common.util.client.tablefile;

import java.util.List;

/**
 * Generic wrapper on any data type which can also hold error and actual token string.
 * 
 * @param <T>
 */

public class TableType<T> {
	private T				value;
	private String			tokenValue;
	private List<String>	errors;

	/**
	 * Default constructor
	 */
	public TableType() {
	}

	/**
	 * @param value
	 */
	public TableType(T value) {
		super();
		this.value = value;
	}

	public void insertErrors(String value, List<String> errors) {
		this.tokenValue = value;
		this.errors = errors;
	}

	/**
	 * @return the tokenValue
	 */
	public String getTokenValue() {
		return tokenValue;
	}

	/**
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TableType [value=" + value + ", tokenValue=" + tokenValue + ", errors=" + errors + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errors == null) ? 0 : errors.hashCode());
		result = prime * result + ((tokenValue == null) ? 0 : tokenValue.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		TableType other = (TableType) obj;
		if (errors == null) {
			if (other.errors != null)
				return false;
		} else if (!errors.equals(other.errors))
			return false;
		if (tokenValue == null) {
			if (other.tokenValue != null)
				return false;
		} else if (!tokenValue.equals(other.tokenValue))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
