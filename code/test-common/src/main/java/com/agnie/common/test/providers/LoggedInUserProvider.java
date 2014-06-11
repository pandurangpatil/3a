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

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Pandurang Patil 10-Feb-2014
 * 
 */
public class LoggedInUserProvider implements Provider<UserAccount> {

	@Inject
	private LoggedInUserManager	mgr;

	public UserAccount get() {
		return mgr.getCurrentUser();
	}

}
