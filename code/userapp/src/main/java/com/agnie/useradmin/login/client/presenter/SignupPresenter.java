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
import com.agnie.useradmin.login.client.ui.SignupView;
import com.agnie.useradmin.persistance.client.exception.InvalidCaptcha;
import com.agnie.useradmin.persistance.client.exception.InvalidDomain;
import com.agnie.useradmin.persistance.client.exception.InvalidUserInfoException;
import com.agnie.useradmin.persistance.client.exception.UserNameExistException;
import com.agnie.useradmin.persistance.client.service.AuthenticateServiceAsync;
import com.agnie.useradmin.persistance.client.service.dto.UserInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Singleton;

@Singleton
public class SignupPresenter extends BasePresenter implements I18 {
	SignupView	view;

	@Override
	public boolean go() {
		super.go();
		RootPanel contentPanel = clientFactory.getRootPanelFactory().getContentPanel();
		contentPanel.clear();

		HTMLPanel centerPanel = viewFactory.getCenterContentPanel();
		centerPanel.clear();

		view = viewFactory.getSignupView();
		messagePanel.hide();
		view.reset();
		centerPanel.add(messagePanel);
		centerPanel.add(view);
		contentPanel.add(centerPanel);
		return true;
	}

	public void Signup(UserInfo usi) {
		AuthenticateServiceAsync as = clientFactory.getAuthenticateService();
		String existingUrl = Window.Location.getHref();
		existingUrl = existingUrl.substring(0, existingUrl.indexOf('#'));
		as.signup(usi, existingUrl, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				messagePanel.show(true);
				messagePanel.setMessage(messages.signup_success());
				messagePanel.setType(MessageType.INFORMATION);
				appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.LOGIN));
			}

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvalidUserInfoException) {
					messagePanel.show(true);
					messagePanel.setMessage(messages.invalid_user_info());
					messagePanel.setType(MessageType.ERROR);
				} else if (caught instanceof UserNameExistException) {
					viewFactory.getSignupView().setUserNameExistError();
				} else if (caught instanceof InvalidDomain) {
					messagePanel.show(true);
					messagePanel.setMessage(messages.invalid_domain());
					messagePanel.setType(MessageType.ERROR);
				} else if (caught instanceof InvalidCaptcha) {
					messagePanel.show(true);
					messagePanel.setMessage(messages.invalid_captcha());
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
