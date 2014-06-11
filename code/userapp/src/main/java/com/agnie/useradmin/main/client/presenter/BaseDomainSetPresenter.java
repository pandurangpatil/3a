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
package com.agnie.useradmin.main.client.presenter;

import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.gwt.common.client.widget.BreadCrumbPanel;
import com.agnie.useradmin.main.client.I18;
import com.agnie.useradmin.main.client.mvp.PlaceToken;
import com.agnie.useradmin.main.client.presenter.sahered.ui.ListMenu;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;

public abstract class BaseDomainSetPresenter extends BasePresenter {
	ClickHandler	domSetBreadCrumbClickhandler	= new ClickHandler() {

														@Override
														public void onClick(ClickEvent event) {
															appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.DOMAIN_SETTINGS));
														}
													};

	public boolean go() {
		if (super.go() && appACLProvider.get() != null && appACLProvider.get().check(Permissions.DOMAIN_SETTINGS)) {

			BreadCrumbPanel breadCrumbPanel = viewFactory.getBreadCrumbPanel();
			breadCrumbPanel.addBreadCrumb(I18.messages.domainSettings());
			breadCrumbPanel.getBreadCrumb(1).addClickHandler(domSetBreadCrumbClickhandler);
			viewFactory.getListMenu().selectTab(ListMenu.Tab.DOMAIN_SETTINGS.getIndex());
			return true;
		} else {
			Scheduler.get().scheduleDeferred(new Command() {
				public void execute() {
					appController.getPlaceManager().changePlace(new Place<PlaceToken>(appController.getDefaultPlace()));
				}
			});
			return false;
		}
	}

}
