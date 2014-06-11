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
package com.agnie.useradmin.session.client.helper;

import com.agnie.common.gwt.serverclient.client.enums.Cokie;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Singleton;

/**
 * @author Pandurang Patil 27-Feb-2014
 * 
 */
@Singleton
public class Helper {

	/**
	 * Note: this will only set cookie value to zero and don't initiate backend api call.
	 * 
	 */
	public void logout() {
		Cookies.removeCookie(Cokie.AUTH.getKey());
	}
}
