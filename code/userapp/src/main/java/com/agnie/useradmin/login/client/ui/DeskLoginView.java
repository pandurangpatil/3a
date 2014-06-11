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

import com.agnie.gwt.common.client.helper.SHA256;
import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.gwt.common.client.widget.LabelPasswordBox;
import com.agnie.gwt.common.client.widget.LabelTextBox;
import com.agnie.gwt.common.client.widget.MessagePanel;
import com.agnie.gwt.common.client.widget.MessagePanel.MessageType;
import com.agnie.useradmin.common.client.helper.LoginQSProcessor;
import com.agnie.useradmin.login.client.injector.ApplicationProvider;
import com.agnie.useradmin.login.client.injector.MVPInjector;
import com.agnie.useradmin.login.client.mvp.LoginAppController;
import com.agnie.useradmin.login.client.mvp.PlaceToken;
import com.agnie.useradmin.login.client.presenter.LoginPresenter;
import com.agnie.useradmin.persistance.client.service.I18;
import com.agnie.useradmin.persistance.client.service.dto.Application;
import com.agnie.useradmin.persistance.client.service.dto.Credential;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeskLoginView extends BaseViewImpl implements LoginView, I18 {

	@Inject
	private LoginPresenter		listner;
	@Inject
	private MVPInjector			injector;
	@Inject
	private LoginAppController	appController;
	@Inject
	private ApplicationProvider	applicationProvider;
	@Inject
	private MessagePanel		messagePanel;

	interface LoginUiBinder extends UiBinder<Widget, DeskLoginView> {
	}

	private static LoginUiBinder	uiBinder					= GWT.create(LoginUiBinder.class);

	HTMLPanel						contentLPanel;

	@UiField
	DivElement						loginmsg;

	@UiField
	LabelTextBox					userNameTB;

	@UiField
	LabelPasswordBox				passwordTB;

	@UiField
	CheckBox						checkToRemember;

	@UiField
	Button							loginBtn;

	@UiField
	Anchor							signupBtn;

	@UiField
	Anchor							forgetPwd;

	HandlerRegistration				defaultSubmitHandler;
	boolean							authenticationInProgress	= false;

	public DeskLoginView() {
		contentLPanel = (HTMLPanel) uiBinder.createAndBindUi(this);
		initWidget(contentLPanel);
	}

	@Override
	public void initLabels() {
		Application app = applicationProvider.get();
		if (app == null) {
			Window.alert("There is something wrong");
		}
		// if (app.getDetailsURL() != null && !app.getDetailsURL().isEmpty()) {
		// appInfoFrame.setUrl(app.getDetailsURL());
		// }
		loginmsg.setInnerText(messages.loginmsg(app.getDomain()));
	}

	/**
	 * Checking for required field.
	 * 
	 * @return
	 */
	public boolean validate() {
		if (userNameTB.getValue().isEmpty()) {
			userNameTB.setErrorMessage(com.agnie.useradmin.login.client.I18.messages.reqField(), false);
			return false;
		}
		if (passwordTB.getValue().isEmpty()) {
			passwordTB.setErrorMessage(com.agnie.useradmin.login.client.I18.messages.reqField(), false);
			return false;
		}
		return true;
	}

	@UiHandler("loginBtn")
	void handleLoginClick(ClickEvent e) {
		clearErrorMessage();
		loginBtn.setEnabled(false);

		// Because of some reason when login submit event is fired when user press enter button (event handled through
		// NativePreviewHandler) it fires three times. Which results in multiple calls being made to backend, which in
		// turn results into backend error. Thats where this boolean flag has been introduced to not make a call if it
		// is already been made.
		if (validate() && !authenticationInProgress) {
			authenticationInProgress = true;
			Credential cred = new Credential();
			cred.setUsername(userNameTB.getText());
			String salt = "" + Random.nextDouble();
			cred.setPassword(SHA256.getSHA256Base64(SHA256.getSHA256Base64(passwordTB.getText()) + salt));
			cred.setDomain(LoginQSProcessor.getDomain());
			if (messages.register().equals(loginBtn.getText())) {
				listner.register(cred, salt);
			} else {
				listner.authenticate(cred, salt, checkToRemember.getValue());
			}
		} else {
			loginBtn.setEnabled(true);
		}
	}

	@UiHandler("signupBtn")
	void handleSignupClick(ClickEvent e) {

		appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.SIGN_UP));
	}

	@UiHandler("forgetPwd")
	void handleForgetPwdClick(ClickEvent e) {
		appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.FGT_PWD));
	}

	public void reset() {
		loginBtn.setEnabled(true);
		authenticationInProgress = false;
		clearPassword();
	}

	public void clearPassword() {
		passwordTB.setText("");
	}

	public void clearErrorMessage() {
		userNameTB.getErrorPan().hide();
		passwordTB.getErrorPan().hide();
	}

	@Override
	public void registerView(String username) {
		messagePanel.show(false);
		loginBtn.setEnabled(true);
		messagePanel.setMessage(messages.invalidDomainAuth());
		messagePanel.setType(MessageType.WARNING);
		userNameTB.setText(username);
		userNameTB.setEnabled(false);
		loginBtn.setText(messages.register());
		passwordTB.setFocus(true);
	}

	ViewFactory getViewFactory() {
		return injector.getDesktopViewFactory();
	}

	@Override
	public boolean shouldWeProceed() {
		return true;
	}

	@Override
	public void setDefaultFocus() {
		if (messages.register().equals(loginBtn.getText())) {
			passwordTB.setFocus(true);
		} else {
			userNameTB.setFocus(true);
		}
		defaultSubmitHandler = Event.addNativePreviewHandler(submitHandler);
	}

	private NativePreviewHandler	submitHandler	= new NativePreviewHandler() {

														@Override
														public void onPreviewNativeEvent(NativePreviewEvent event) {
															if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
																handleLoginClick(null);
															}
														}
													};
}
