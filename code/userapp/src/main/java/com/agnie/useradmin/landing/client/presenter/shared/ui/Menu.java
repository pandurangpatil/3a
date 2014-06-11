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
package com.agnie.useradmin.landing.client.presenter.shared.ui;

import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.useradmin.landing.client.mvp.LandingAppController;
import com.agnie.useradmin.landing.client.mvp.PlaceToken;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class Menu extends Composite {
	@Inject
	LandingAppController	appController;

	interface MyUiBinder extends UiBinder<Widget, Menu> {
	}

	private static MyUiBinder	uiBinder	= GWT.create(MyUiBinder.class);

	@UiField
	public HTMLPanel			CreateNewSociety;

	HTMLPanel					container;

	public Menu() {
		container = (HTMLPanel) uiBinder.createAndBindUi(this);
		initWidget(container);
		CreateNewSociety.sinkEvents(Event.ONCLICK);
		CreateNewSociety.addHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.CREATE));
			}
		}, ClickEvent.getType());
	}

	public void showMessage(String msg) {
		container.add(new HTML(msg));
	}

	public boolean allowUserToProceed() {
		return true;
	}

	public boolean resotre() {
		return false;
	}

}
