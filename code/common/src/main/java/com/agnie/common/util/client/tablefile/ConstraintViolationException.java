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

import java.util.List;

public class ConstraintViolationException extends RuntimeException {

	private String				header;
	private List<String>		failedConstraints;

	/**
     * 
     */
	private static final long	serialVersionUID	= 1L;

	public ConstraintViolationException(String header, List<String> failedConstraints) {
		this.header = header;
		this.failedConstraints = failedConstraints;
	}

	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @return the failedConstraints
	 */
	public List<String> getFailedConstraints() {
		return failedConstraints;
	}

	public String getFailedConstraintsStr() {
		StringBuffer resp = new StringBuffer();
		if (failedConstraints != null) {
			boolean first = true;
			for (String ann : failedConstraints) {
				if (!first) {
					resp.append(", ");
				} else {
					first = false;
				}
				resp.append(ann);
			}
		}
		return resp.toString();
	}

}
