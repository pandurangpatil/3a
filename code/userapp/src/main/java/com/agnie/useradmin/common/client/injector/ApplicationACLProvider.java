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
package com.agnie.useradmin.common.client.injector;

import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.google.inject.Singleton;

@Singleton
public class ApplicationACLProvider {

	private AccessControlList	value;

	/**
	 * @return the value
	 */
	public AccessControlList get() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void set(AccessControlList value) {
		this.value = value;
	}

}
