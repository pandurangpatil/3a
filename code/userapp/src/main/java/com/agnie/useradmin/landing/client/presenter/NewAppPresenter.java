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
package com.agnie.useradmin.landing.client.presenter;

import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.gwt.common.client.widget.MessagePanel.MessageType;
import com.agnie.useradmin.landing.client.I18;
import com.agnie.useradmin.landing.client.mvp.PlaceToken;
import com.agnie.useradmin.landing.client.ui.NewAppView;
import com.agnie.useradmin.persistance.shared.proxy.ApplicationPx;
import com.agnie.useradmin.persistance.shared.service.ApplicationManagerRequest;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

@Singleton
public class NewAppPresenter extends LandingBasePresenter {
	NewAppView	view;

	@Override
	public boolean go() {
		super.go();
		RootPanel contentPanel = clientFactory.getRootPanelFactory().getContentPanel();
		contentPanel.clear();

		HTMLPanel centerPanel = viewFactory.getCenterContentPanel();
		centerPanel.clear();

		view = viewFactory.getCreateNewAppView();
		messagePanel.hide();
		centerPanel.add(messagePanel);
		view.reset();
		centerPanel.add(view);
		contentPanel.add(centerPanel);
		// getConnectedProfiles();
		return true;
	}

	public void createNewApp(final ApplicationManagerRequest amr, final ApplicationPx appPx) {
		amr.createApplication(appPx).fire(new Receiver<ApplicationPx>() {

			@Override
			public void onSuccess(ApplicationPx response) {
				messagePanel.show(true);
				messagePanel.setMessage(I18.messages.newAppCreated());
				messagePanel.setType(MessageType.INFORMATION);
				appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.LANDING));
			}

			@Override
			public void onFailure(ServerFailure error) {
				messagePanel.show(true);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
				messagePanel.setType(MessageType.ERROR);
			}
		});
	}

	public void checkIfAvilable(String domain) {
		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		amr.isAvilable(domain).fire(new Receiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				viewFactory.getCreateNewAppView().isAvilableResp(response);
			}

			@Override
			public void onFailure(ServerFailure error) {
				viewFactory.getCreateNewAppView().isAvilableResp(false);
				messagePanel.show(true);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
				messagePanel.setType(MessageType.ERROR);
			}
		});
	}

	@Override
	public void postRender() {
		view.setDefaultFocus();
	}

}
