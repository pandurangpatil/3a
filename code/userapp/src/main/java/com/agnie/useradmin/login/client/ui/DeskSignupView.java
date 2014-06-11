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
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import com.agnie.common.gwt.serverclient.client.helper.URLConfiguration;
import com.agnie.common.util.client.validator.ConstraintRegularExpressions;
import com.agnie.gwt.common.client.helper.SHA256;
import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.gwt.common.client.widget.FormFieldContainer;
import com.agnie.gwt.common.client.widget.MessagePanel;
import com.agnie.gwt.common.client.widget.MessagePanel.MessageType;
import com.agnie.useradmin.common.client.helper.LoginQSProcessor;
import com.agnie.useradmin.login.client.I18;
import com.agnie.useradmin.login.client.injector.MVPInjector;
import com.agnie.useradmin.login.client.mvp.LoginAppController;
import com.agnie.useradmin.login.client.mvp.PlaceToken;
import com.agnie.useradmin.login.client.presenter.SignupPresenter;
import com.agnie.useradmin.persistance.client.service.dto.UserInfo;
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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sfeir.captcha.client.ui.Captcha;

@Singleton
public class DeskSignupView extends BaseViewImpl implements SignupView, I18 {

	@Inject
	private SignupPresenter		listner;
	@Inject
	private MVPInjector			injector;
	@Inject
	private LoginAppController	appController;
	@Inject
	private MessagePanel		messagePanel;
	@Inject
	private URLConfiguration	urlConfig;

	@UiField
	TextBox						titletb;
	@UiField
	TextBox						fnametb;
	@UiField
	TextBox						lnametb;
	@UiField
	TextBox						emailtb;
	@UiField
	TextBox						usernametb;
	@UiField
	PasswordTextBox				paswdtb;
	@UiField
	PasswordTextBox				confpaswdtb;
	@UiField
	Button						submitBtn;
	@UiField
	Button						cancelBtn;
	@UiField
	FormFieldContainer			titletbContainer;
	@UiField
	FormFieldContainer			fnametbContainer;
	@UiField
	FormFieldContainer			lnametbContainer;
	@UiField
	FormFieldContainer			emailtbContainer;
	@UiField
	FormFieldContainer			paswdtbContainer;
	@UiField
	FormFieldContainer			confpaswdtbContainer;
	@UiField
	FormFieldContainer			usernametbContainer;

	@UiField
	HTMLPanel					captchaContainer;
	Captcha						captcha;

	interface SignupUiBinder extends UiBinder<Widget, DeskSignupView> {
	}

	private static SignupUiBinder		uiBinder		= GWT.create(SignupUiBinder.class);

	private boolean						dirtyFlag		= false;
	private boolean						actionComplete	= false;
	private List<HandlerRegistration>	handlerReg		= new ArrayList<HandlerRegistration>();

	public DeskSignupView() {
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

		handlerReg.add(titletb.addKeyPressHandler(handler));
		handlerReg.add(fnametb.addKeyPressHandler(handler));
		handlerReg.add(lnametb.addKeyPressHandler(handler));
		handlerReg.add(emailtb.addKeyPressHandler(handler));
		handlerReg.add(usernametb.addKeyPressHandler(handler));
		handlerReg.add(paswdtb.addKeyPressHandler(handler));
		handlerReg.add(confpaswdtb.addKeyPressHandler(handler));
	}

	private void clearHandlerRegistration() {
		for (int i = 0; i < handlerReg.size(); i++) {
			handlerReg.get(i).removeHandler();
		}
		// before adding new set of handlers clearing existing list.
		handlerReg.clear();
	}

	@UiHandler("submitBtn")
	public void submit(ClickEvent event) {
		/**
		 * TODO: Add validation.
		 */
		UserInfo usi = new UserInfo();

		usi.setTitle(titletb.getText());
		usi.setFirstName(fnametb.getText());
		usi.setLastName(lnametb.getText());
		usi.setEmailId(emailtb.getText());
		usi.setUserName(usernametb.getText());
		usi.setPassword(paswdtb.getText());
		usi.setCurrentLogingDomain(LoginQSProcessor.getDomain());
		containerErrorFixed();
		if (validate(usi)) {
			usi.setCaptchaAns(captcha.validateCaptcha());
			usi.setPassword(SHA256.getSHA256Base64(paswdtb.getText()));
			listner.Signup(usi);
			actionComplete = true;
		}
	}

	private boolean validate(UserInfo usi) {
		boolean valid = true;

		javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<UserInfo>> violations = validator.validate(usi);

		if (violations.size() > 0) {
			valid = false;
			for (ConstraintViolation<UserInfo> constraintViolation : violations) {
				if ("title".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					titletbContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
				if ("firstName".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					fnametbContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
				if ("lastName".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					lnametbContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
				if ("emailId".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					emailtbContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
				if ("password".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					paswdtbContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
				if ("userName".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					usernametbContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
			}
		}

		String token = usi.getEmailId();
		if (!ConstraintRegularExpressions.validateEmail(token)) {
			emailtbContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.invalidEmail(), false);
			valid = false;
		}

		token = usi.getUserName();
		if (!ConstraintRegularExpressions.validateWhiteSpace(token)) {
			usernametbContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.noWhiteSpace(), false);
			valid = false;
		}
		token = paswdtb.getText();
		if (!ConstraintRegularExpressions.validateWhiteSpace(token)) {
			paswdtbContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.noWhiteSpace(), false);
			valid = false;
		}
		token = confpaswdtb.getText();
		if (paswdtb.getText() != null && !paswdtb.getText().equals(token)) {
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
		messagePanel.hide();
		titletbContainer.errorFixed();
		fnametbContainer.errorFixed();
		lnametbContainer.errorFixed();
		emailtbContainer.errorFixed();
		usernametbContainer.errorFixed();
		paswdtbContainer.errorFixed();
		confpaswdtbContainer.errorFixed();
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
	public void reset() {
		if (captcha == null) {
			captcha = new Captcha(urlConfig.recaptchaPublicKey());
			captchaContainer.add(captcha);
		}
		captcha.showLoader();
		titletb.setText("");
		fnametb.setText("");
		lnametb.setText("");
		emailtb.setText("");
		usernametb.setText("");
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
	public void setUserNameExistError() {
		usernametbContainer.setError(messages.user_exist(), false);
	}

	@Override
	public void setDefaultFocus() {
		this.usernametb.setFocus(true);
	}

	ViewFactory getViewFactory() {
		return injector.getDesktopViewFactory();
	}
}
