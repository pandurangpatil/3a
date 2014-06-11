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
package com.agnie.useradmin.session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.inject.Provider;

/**
 * 
 * 
 */
public class ConnectionProvider implements Provider<Connection> {

	@Override
	public Connection get() {
		try {
			Connection conn = null;
			// TODO: need to replace hard coded credentials with some easily modifiable mechanism
			conn = DriverManager.getConnection("jdbc:mysql://localhost/UserAdminTest", "useradmin", "3adb");
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
