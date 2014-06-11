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

import com.agnie.common.server.auth.ACLContext;

/**
 * This class should not be instantiated directly and used. It should be always need to used by getting it injected from
 * Guice injector.
 * 
 */
public class ACLCtxManager {

	private ACLContext	context;

	/**
	 * @return the context
	 */
	public ACLContext getContext() {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(ACLContext context) {
		this.context = context;
	}

}
