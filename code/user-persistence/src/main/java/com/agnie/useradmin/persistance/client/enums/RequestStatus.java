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

import com.agnie.common.gwt.serverclient.client.renderer.Title;
import com.agnie.useradmin.persistance.client.service.I18;

public enum RequestStatus implements Title{
	// NOTE: Don't delete any of the value. If you really want to delete something a proper impact analysis should be
	// done. New values should be always added at the end. Don't change the order of existing values. If you change the
	// order, you have messed-up very badly.
	REQUESTED, PROVISIONAL, ACTIVE, DISABLED;

	public String getLocalized() {
		switch (this) {
		case ACTIVE:
			return I18.messages.active();
		case PROVISIONAL:
			return I18.messages.provisional();
		case REQUESTED:
			return I18.messages.requested();
		case DISABLED:
		default:
			return I18.messages.disabled();
		}
	}

	public static RequestStatus getEmum(String localized) {
		if (I18.messages.active().equals(localized)) {
			return ACTIVE;
		} else if (I18.messages.provisional().equals(localized)) {
			return PROVISIONAL;
		} else if (I18.messages.requested().equals(localized)) {
			return REQUESTED;
		} else {
			return DISABLED;
		}
	}

	@Override
	public String getTitle() {
		return getLocalized();
	}
}
