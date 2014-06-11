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

import com.agnie.useradmin.session.server.injector.SessionServletModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

public class RequestTypeProvider implements Provider<RequestType> {
	@Inject
	@Named(SessionServletModule.REQUEST_TYPE_STR)
	Provider<String>	requestTypeStr;

	@Override
	public RequestType get() {
		if (requestTypeStr.get() == null || requestTypeStr.get().isEmpty()) {
			return null;
		} else {
			return RequestType.valueOf(requestTypeStr.get());
		}
	}

}
