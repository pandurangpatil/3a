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
package com.agnie.useradmin.landing.client.mvp;

public enum PlaceToken {
	// Note: if place Token UPDATE_PROFILE & CHANGE_PASS need to be renamed for any reason. You need to change
	// respective string inside, com.agnie.common.gwt.serverclient.client.helper.URLGenerator class.
	LANDING, UPDATE_PROFILE, CHANGE_PASS, CREATE;
}
