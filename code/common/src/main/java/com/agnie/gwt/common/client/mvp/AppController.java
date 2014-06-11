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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AppController<PLACE extends Enum<PLACE>> implements ValueChangeHandler<String> {

	private Class<PLACE>		placeType;
	private Place<PLACE>		lastPlace;
	private PlaceManager<PLACE>	placeMgr;

	public AppController(Class<PLACE> placeType) {
		this.placeType = placeType;
		History.addValueChangeHandler(this);
		placeMgr = new PlaceManager<PLACE>(this, placeType);
	}

	public PlaceManager<PLACE> getPlaceManager() {
		return placeMgr;
	}

	public void go() {
		if ("".equals(History.getToken())) {
			History.newItem(new Place<PLACE>(getDefaultPlace()).toString());
		} else {
			History.fireCurrentHistoryState();
		}
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		if (checkIfWeCanProceed()) {
			String token = event.getValue();
			GWT.log("AppContorller on value change Stringtoken==" + token);
			if (token != null) {
				Presenter presenter = getPresenterForPlace(placeMgr.getTokenToPlace(placeType, token));
				if (presenter != null) {
					presenter.go();
					presenter.postRender();
				}
			}
		} else {
			// Just roll back the history token to the one from which page change action is initiated.
			History.newItem(lastPlace.toString(), false);
		}
	}

	protected boolean checkIfWeCanProceed() {
		HTMLPanel contentPanel = getMainContentRootPanel();
		if (contentPanel != null) {
			for (int index = 0; index < contentPanel.getWidgetCount(); index++) {
				Widget widget = contentPanel.getWidget(index);
				if (widget instanceof MainView) {
					MainView priviousDisplay = (MainView) widget;
					boolean resp = priviousDisplay.shouldWeProceed();
					if (!resp)
						return false;
				}
			}
		}
		return true;
	}

	public void setLastPlace(Place<PLACE> place) {
		this.lastPlace = place;
	}

	protected abstract HTMLPanel getMainContentRootPanel();

	protected abstract PLACE getDefaultPlace();

	protected abstract Presenter getPresenterForPlace(Place<PLACE> place);

}
