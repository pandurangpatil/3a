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
package com.agnie.useradmin.persistance.server.util;

public class Constants {
	public static enum SessionParams {
		USERID("userid"), CURRENT_DOMAIN("currentDomain"), CURRENT_ACL("crntACL"), CURRENT_USER("crntUser"), USERADMIN_ACL("useradminACL");
		private String	key;

		private SessionParams(String key) {
			this.key = key;
		}

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		@Override
		public String toString() {
			return key;
		}
	}
}
