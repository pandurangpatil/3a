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
package com.agnie.common.helper;

import com.agnie.common.gwt.serverclient.client.helper.URLConfiguration;
import com.agnie.common.gwt.serverclient.client.helper.URLGenerator;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * @author Pandurang Patil 12-Feb-2014
 * 
 */
@Singleton
public class ServerURLConfiguration implements URLConfiguration {

	@Inject
	@Named(URLGenerator.USER_ADMIN_ROOT_ENDPOINT)
	private String	uaBaseURL;
	@Inject
	@Named(URLGenerator.RECAPTCHA_PUBLIC_KEY)
	private String	recapPublicKey;

	@Override
	public String get3ABaseURL() {
		return uaBaseURL;
	}

	@Override
	public String recaptchaPublicKey() {
		return recapPublicKey;
	}

}
