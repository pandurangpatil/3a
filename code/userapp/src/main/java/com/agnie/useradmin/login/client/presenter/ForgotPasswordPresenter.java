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
package com.agnie.useradmin.login.client.presenter;

import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.gwt.common.client.widget.MessagePanel.MessageType;
import com.agnie.useradmin.login.client.I18;
import com.agnie.useradmin.login.client.mvp.PlaceToken;
import com.agnie.useradmin.login.client.ui.ForgotPasswordView;
import com.agnie.useradmin.persistance.client.exception.UserNotFoundException;
import com.agnie.useradmin.persistance.client.service.AuthenticateServiceAsync;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Singleton;

@Singleton
public class ForgotPasswordPresenter extends BasePresenter {
	ForgotPasswordView	view;

	@Override
	public boolean go() {
		super.go();
		RootPanel contentPanel = clientFactory.getRootPanelFactory().getContentPanel();
		contentPanel.clear();

		HTMLPanel centerPanel = viewFactory.getCenterContentPanel();
		centerPanel.clear();

		view = viewFactory.getForgotPasswordView();
		messagePanel.hide();
		view.reset();
		centerPanel.add(messagePanel);
		centerPanel.add(view);
		contentPanel.add(centerPanel);
		return true;
	}

	public void sendForgotPasswordReq(String userid) {
		AuthenticateServiceAsync as = clientFactory.getAuthenticateService();
		String existingUrl = Window.Location.getHref();
		existingUrl = existingUrl.substring(0, existingUrl.indexOf('#'));
		as.forgotPassword(userid, existingUrl, new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					messagePanel.show(true);
					messagePanel.setMessage(I18.messages.forgetPassReqSent());
					messagePanel.setType(MessageType.INFORMATION);
					appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.LOGIN));
				} else {
					messagePanel.show(true);
					messagePanel.setMessage(I18.messages.invalidUsername());
					messagePanel.setType(MessageType.ERROR);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof UserNotFoundException) {
					messagePanel.show(true);
					messagePanel.setMessage(I18.messages.invalidUsername());
					messagePanel.setType(MessageType.ERROR);
				} else {
					messagePanel.show(true);
					messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
					messagePanel.setType(MessageType.ERROR);
				}
			}
		});
	}

	@Override
	public void postRender() {
		view.setDefaultFocus();
	}
}
