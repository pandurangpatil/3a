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
package com.agnie.common.gwt.serverclient.client.helper;

/**
 * @author Pandurang Patil 12-Feb-2014
 * 
 */
public class ClientURLConfiguration implements URLConfiguration {
	/**
	 * It is required to include config.jsp inside main / landing jsp page of your application. Which will make sure
	 * required javascript values are injected in main page.
	 * 
	 * @param key
	 * @return
	 */
	private native String getConstValue(String key)/*-{
													return $wnd.constants[key];
													}-*/;

	@Override
	public String get3ABaseURL() {
		return getConstValue(URLGenerator.USER_ADMIN_ROOT_ENDPOINT);
	}

	@Override
	public String recaptchaPublicKey() {
		return getConstValue(URLGenerator.RECAPTCHA_PUBLIC_KEY);
	}

}
