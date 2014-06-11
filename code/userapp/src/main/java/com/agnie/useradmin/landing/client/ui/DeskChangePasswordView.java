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
package com.agnie.useradmin.landing.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.common.util.client.validator.ConstraintRegularExpressions;
import com.agnie.gwt.common.client.helper.SHA256;
import com.agnie.gwt.common.client.widget.FormFieldContainer;
import com.agnie.gwt.common.client.widget.MessagePanel;
import com.agnie.gwt.common.client.widget.MessagePanel.MessageType;
import com.agnie.useradmin.landing.client.injector.MVPInjector;
import com.agnie.useradmin.landing.client.presenter.ChangePasswordPresenter;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeskChangePasswordView extends Composite implements ChangePasswordView {

	@Inject
	ChangePasswordPresenter	presenter;
	@Inject
	MVPInjector				injector;
	@Inject
	URLInfo					urlInfo;
	@Inject
	UserAdminURLGenerator	uaUrlGenerator;
	@Inject
	MessagePanel			messagePanel;
	@UiField
	FormFieldContainer		oldPassContainer;
	@UiField
	FormFieldContainer		paswdtbContainer;
	@UiField
	FormFieldContainer		confpaswdtbContainer;

	interface MyUiBinder extends UiBinder<Widget, DeskChangePasswordView> {
	}

	private static MyUiBinder			uiBinder		= GWT.create(MyUiBinder.class);

	@UiField
	PasswordTextBox						oldPass;
	@UiField
	PasswordTextBox						newPass;
	@UiField
	PasswordTextBox						confPass;

	@UiField
	Button								submitBtn;
	@UiField
	Button								cancelBtn;

	private boolean						dirtyFlag		= false;
	private boolean						actionComplete	= false;
	private List<HandlerRegistration>	handlerReg		= new ArrayList<HandlerRegistration>();

	public DeskChangePasswordView() {
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

		handlerReg.add(oldPass.addKeyPressHandler(handler));
		handlerReg.add(newPass.addKeyPressHandler(handler));
		handlerReg.add(confPass.addKeyPressHandler(handler));

	}

	private void clearHandlerRegistration() {
		for (int i = 0; i < handlerReg.size(); i++) {
			handlerReg.get(i).removeHandler();
		}
		// before adding new set of handlers clearing existing list.
		handlerReg.clear();
	}

	private boolean validate() {
		boolean valid = true;
		String token = oldPass.getText();
		if (token == null || token.isEmpty()) {
			valid = false;
			oldPassContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.required(), false);
		}

		token = newPass.getText();
		if (token == null || token.isEmpty()) {
			valid = false;
			paswdtbContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.required(), false);
		} else if (!ConstraintRegularExpressions.validateWhiteSpace(token)) {
			paswdtbContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.noWhiteSpace(), false);
			valid = false;
		}
		token = confPass.getText();
		if (token == null || token.isEmpty()) {
			valid = false;
			confpaswdtbContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.required(), false);
		} else if (newPass.getText() != null && !newPass.getText().equals(token)) {
			valid = false;
			messagePanel.show(true);
			messagePanel.setMessage(com.agnie.useradmin.common.client.I18.errorMessages.password_shld_match());
			messagePanel.setType(MessageType.ERROR);
			newPass.setText("");
			confPass.setText("");
		}
		return valid;
	}

	private void containerErrorFixed() {
		paswdtbContainer.errorFixed();
		confpaswdtbContainer.errorFixed();
		oldPassContainer.errorFixed();
	}

	public void reset() {
		oldPass.setText("");
		newPass.setText("");
		confPass.setText("");

		dirtyFlag = false;
		actionComplete = false;
		containerErrorFixed();
		addKeyPressHandlers();
	}

	@UiHandler("cancelBtn")
	public void cancel(ClickEvent event) {
		Window.Location.assign(uaUrlGenerator.getSourceUrl(urlInfo));
	}

	@UiHandler("submitBtn")
	public void submit(ClickEvent event) {
		containerErrorFixed();
		if (validate()) {
			presenter.resetPassword(SHA256.getSHA256Base64(oldPass.getText()), SHA256.getSHA256Base64(newPass.getText()));
			actionComplete = true;
		}
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
		this.oldPass.setFocus(true);
	}

	ViewFactory getViewFactory() {
		return injector.getDesktopViewFactory();
	}
}
