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

import com.agnie.common.util.client.validator.ConstraintRegularExpressions;
import com.agnie.gwt.common.client.helper.SHA256;
import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.gwt.common.client.widget.FormFieldContainer;
import com.agnie.gwt.common.client.widget.MessagePanel;
import com.agnie.gwt.common.client.widget.MessagePanel.MessageType;
import com.agnie.useradmin.login.client.I18;
import com.agnie.useradmin.login.client.injector.MVPInjector;
import com.agnie.useradmin.login.client.mvp.LoginAppController;
import com.agnie.useradmin.login.client.mvp.PlaceToken;
import com.agnie.useradmin.login.client.presenter.FPChangePwdPresenter;
import com.agnie.useradmin.persistance.client.service.dto.ForgotPasswordChallenge;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeskFPChangePasswordView extends BaseViewImpl implements FPChangePasswordView, I18 {
	@Inject
	private FPChangePwdPresenter	listner;
	@Inject
	private MVPInjector				injector;
	@Inject
	private LoginAppController		appController;
	@Inject
	private MessagePanel			messagePanel;
	@UiField
	Label							username;

	@UiField
	PasswordTextBox					paswdtb;

	@UiField
	PasswordTextBox					confpaswdtb;

	@UiField
	Button							sendBtn;

	@UiField
	Button							cancelBtn;

	@UiField
	FormFieldContainer				paswdtbContainer;

	@UiField
	FormFieldContainer				confpaswdtbContainer;

	interface FPCPUiBinder extends UiBinder<Widget, DeskFPChangePasswordView> {
	}

	private static FPCPUiBinder			uiBinder		= GWT.create(FPCPUiBinder.class);

	private boolean						dirtyFlag		= false;
	private boolean						actionComplete	= false;
	private List<HandlerRegistration>	handlerReg		= new ArrayList<HandlerRegistration>();

	public DeskFPChangePasswordView() {
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

		HandlerRegistration paswdtbHandlerReg = paswdtb.addKeyPressHandler(handler);
		HandlerRegistration confpaswdtbHandlerReg = confpaswdtb.addKeyPressHandler(handler);

		handlerReg.add(paswdtbHandlerReg);
		handlerReg.add(confpaswdtbHandlerReg);
	}

	private void clearHandlerRegistration() {
		for (int i = 0; i < handlerReg.size(); i++) {
			handlerReg.get(i).removeHandler();
		}
		// before adding new set of handlers clearing existing list.
		handlerReg.clear();
	}

	@Override
	public void setUserName(String userName) {
		this.username.setText(userName);
	}

	@UiHandler("sendBtn")
	public void submit(ClickEvent event) {
		containerErrorFixed();
		if (validate()) {
			listner.sendNewPassword(SHA256.getSHA256Base64(paswdtb.getText()));
			actionComplete = true;
		}
	}

	private boolean validate() {
		boolean valid = true;
		String token = paswdtb.getText();
		if (token == null || token.isEmpty()) {
			valid = false;
			paswdtbContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.required(), false);
		} else if (!ConstraintRegularExpressions.validateWhiteSpace(token)) {
			paswdtbContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.noWhiteSpace(), false);
			valid = false;
		}
		token = confpaswdtb.getText();
		if (token == null || token.isEmpty()) {
			valid = false;
			confpaswdtbContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.required(), false);
		} else if (paswdtb.getText() != null && !paswdtb.getText().equals(token)) {
			valid = false;
			messagePanel.show(true);
			messagePanel.setMessage(com.agnie.useradmin.common.client.I18.errorMessages.password_shld_match());
			messagePanel.setType(MessageType.ERROR);
			paswdtb.setText("");
			confpaswdtb.setText("");
		}
		return valid;
	}

	private void containerErrorFixed() {
		paswdtbContainer.errorFixed();
		confpaswdtbContainer.errorFixed();
	}

	@UiHandler("cancelBtn")
	public void cancel(ClickEvent event) {
		appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.LOGIN));
	}

	@Override
	public void init(ForgotPasswordChallenge challenge, String userid) {
		username.setText(userid);
		paswdtb.setText("");
		confpaswdtb.setText("");
	}

	@Override
	public void reset() {
		setDefaultFocus();
		paswdtb.setText("");
		confpaswdtb.setText("");

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
		this.paswdtb.setFocus(true);
	}

	ViewFactory getViewFactory() {
		return injector.getDesktopViewFactory();
	}
}
