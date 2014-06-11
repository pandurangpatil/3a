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
package com.agnie.useradmin.login.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.gwt.common.client.widget.FormFieldContainer;
import com.agnie.useradmin.login.client.mvp.LoginAppController;
import com.agnie.useradmin.login.client.mvp.PlaceToken;
import com.agnie.useradmin.login.client.presenter.ForgotPasswordPresenter;
import com.agnie.useradmin.persistance.client.service.I18;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeskForgotPasswordView extends BaseViewImpl implements ForgotPasswordView, I18 {

	@Inject
	private ForgotPasswordPresenter	listner;
	@Inject
	private LoginAppController		appController;

	@UiField
	TextBox							username;
	@UiField
	Button							sendBtn;
	@UiField
	Button							cancelBtn;
	@UiField
	FormFieldContainer				usernameContainer;

	interface ForgtPwdUiBinder extends UiBinder<Widget, DeskForgotPasswordView> {
	}

	private static ForgtPwdUiBinder		uiBinder		= GWT.create(ForgtPwdUiBinder.class);

	private boolean						dirtyFlag		= false;
	private boolean						actionComplete	= false;
	private List<HandlerRegistration>	handlerReg		= new ArrayList<HandlerRegistration>();

	public DeskForgotPasswordView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private void addKeyPressHandlers() {
		KeyPressHandler handler = new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				dirtyFlag = true;
				clearHandlerRegistration();
			}
		};
		HandlerRegistration usernameHandlerReg = username.addKeyPressHandler(handler);
		handlerReg.add(usernameHandlerReg);
	}

	private void clearHandlerRegistration() {
		for (int i = 0; i < handlerReg.size(); i++) {
			handlerReg.get(i).removeHandler();
		}
		// before adding new set of handlers clearing existing list.
		handlerReg.clear();
	}

	@UiHandler("sendBtn")
	public void submit(ClickEvent event) {
		containerErrorFixed();
		if (validate()) {
			listner.sendForgotPasswordReq(this.username.getText());
			actionComplete = true;
		}
	}

	private boolean validate() {
		boolean valid = true;
		String token = username.getText();
		if (token == null || token.isEmpty()) {
			valid = false;
			usernameContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.required(), false);
		}
		return valid;
	}

	private void containerErrorFixed() {
		usernameContainer.errorFixed();
	}

	@UiHandler("cancelBtn")
	public void cancel(ClickEvent event) {
		appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.LOGIN));
	}

	@Override
	public void showCaptcha() {
		// JavaScriptCommon.showRecaptha();
	}

	@Override
	public void init() {
		username.setText("");
	}

	@Override
	public void reset() {
		username.setText("");

		dirtyFlag = false;
		actionComplete = false;
		containerErrorFixed();
		addKeyPressHandlers();
	}

	@Override
	public boolean shouldWeProceed() {
		if (!actionComplete) {
			if (dirtyFlag) {
				return Window.confirm("Do you want to proceed");
			} else {
				return (true);
			}
		} else {
			return (true);
		}
	}

	@Override
	public void setDefaultFocus() {
		this.username.setFocus(true);
	}
}
