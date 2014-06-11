/*******************************************************************************
 * © 2014 Copyright Agnie Technologies
 *   
 * NOTICE: All information contained herein is, and remains the property of Agnie Technologies and its suppliers, if
 * any. The intellectual and technical concepts contained herein are proprietary to Agnie Technologies and its suppliers
 * and may be covered by Indian and Foreign Patents, patents in process, and are protected by trade secret or copyright
 * law. Dissemination of this information or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Agnie Technologies.
 ******************************************************************************/
package com.agnie.common.test.providers;

import com.google.inject.Provider;

/**
 * 
 * This provider has been specifically been created to facilitate setting required String value for testing purpose.
 * This class shouldn't be used in main code. Usage is limited to single threaded environment. If its being used in
 * multi threaded environment then behaviour is not guaranteed.
 * 
 * @author Pandurang Patil 10-Feb-2014
 * 
 */
public class StringProvider implements Provider<String> {

	private StringManager	mgr;

	public StringProvider(StringManager mgr) {
		this.mgr = mgr;
	}

	public String get() {
		return mgr.getValue();
	}

}
