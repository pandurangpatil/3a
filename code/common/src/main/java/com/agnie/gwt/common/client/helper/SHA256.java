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
package com.agnie.gwt.common.client.helper;

/**
 * To encrypt the given string with SHA256 Base 64 encryption. To make use of this class one need to include util.js in
 * corresponding module's entry point web file (i. e. .html or .jsp etc). Location to include will be the <base path of
 * the entry point module>/js/util.js
 * 
 */
public class SHA256 {
	public static native String getSHA256Base64(String data) /*-{
																var s = $wnd.b64_sha256(data);
																return s;
																}-*/;

}
