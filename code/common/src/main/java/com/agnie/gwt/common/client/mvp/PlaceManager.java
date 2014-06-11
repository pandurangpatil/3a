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
package com.agnie.gwt.common.client.mvp;

import com.google.gwt.user.client.History;

public class PlaceManager<PLACE extends Enum<PLACE>> {

	private AppController<PLACE>	appController;
	private Class<PLACE>			placeType;

	public PlaceManager(AppController<PLACE> appController, Class<PLACE> placeType) {
		this.appController = appController;
		this.placeType = placeType;
	}

	public void changePlace(Place<PLACE> place) {
		String token = History.getToken();
		Place<PLACE> oldPlace;
		if (token == null || token.isEmpty()) {
			oldPlace = new Place<PLACE>(appController.getDefaultPlace());
		}
		oldPlace = getTokenToPlace(placeType, token);
		appController.setLastPlace(oldPlace);
		History.newItem(place.toString());
	}

	public Place<PLACE> getTokenToPlace(Class<PLACE> placeType, String token) {
		Place<PLACE> place = null;
		try {
			if (token != null && token.contains(":")) {
				String[] frTokens = token.split(":");

				place = new Place<PLACE>(Enum.valueOf(placeType, frTokens[0]));
				place.setParameters(frTokens[1]);
			} else {
				place = new Place<PLACE>(Enum.valueOf(placeType, token));
			}
		} catch (Exception ex) {
			place = new Place<PLACE>(appController.getDefaultPlace());
		}
		return place;
	}
}
