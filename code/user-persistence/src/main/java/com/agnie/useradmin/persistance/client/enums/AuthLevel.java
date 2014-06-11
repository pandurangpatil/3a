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
package com.agnie.useradmin.persistance.client.enums;

public enum AuthLevel {
	// NOTE: Don't delete any of the value. If you really want to delete something a proper impact analysis should be
	// done. New values should be always added at the end. Don't change the order of existing values. If you change the
	// order, you have messed-up very badly.
	ADMIN_APP, ADMIN_CTX, APPLICATION, CONTEXT;
}
