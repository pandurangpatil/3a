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
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.common.util.client.validator.ConstraintRegularExpressions;
import com.agnie.gwt.common.client.widget.FormFieldContainer;
import com.agnie.useradmin.landing.client.mvp.ClientFactory;
import com.agnie.useradmin.landing.client.presenter.UpdateProfilePresenter;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.persistance.shared.proxy.UserPx;
import com.agnie.useradmin.persistance.shared.service.UserManagerRequest;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeskUpdateProfileView extends Composite implements UpdateProfileView {

	@Inject
	UpdateProfilePresenter	presenter;

	@Inject
	ClientFactory			clientFactory;

	@Inject
	URLInfo					urlInfo;

	interface MyUiBinder extends UiBinder<Widget, DeskUpdateProfileView> {
	}

	private static MyUiBinder			uiBinder		= GWT.create(MyUiBinder.class);

	@UiField
	TextBox								title;
	@UiField
	TextBox								firstName;
	@UiField
	TextBox								lastName;
	@UiField
	TextBox								profImg;
	@UiField
	Label								userName;
	@UiField
	Label								email;

	@UiField
	Button								submitBtn;
	@UiField
	Button								cancelBtn;

	@UiField
	FormFieldContainer					titleContainer;
	@UiField
	FormFieldContainer					firstNameContainer;
	@UiField
	FormFieldContainer					lastNameContainer;
	@UiField
	FormFieldContainer					profImgContainer;

	private boolean						dirtyFlag		= false;
	private boolean						actionComplete	= false;
	private List<HandlerRegistration>	handlerReg		= new ArrayList<HandlerRegistration>();

	private UserPx						uPx;
	@Inject
	private UserAdminURLGenerator		uaUrlGenerator;

	public DeskUpdateProfileView() {
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

		handlerReg.add(title.addKeyPressHandler(handler));
		handlerReg.add(firstName.addKeyPressHandler(handler));
		handlerReg.add(lastName.addKeyPressHandler(handler));

	}

	private void clearHandlerRegistration() {
		for (int i = 0; i < handlerReg.size(); i++) {
			handlerReg.get(i).removeHandler();
		}
		// before adding new set of handlers clearing existing list.
		handlerReg.clear();
	}

	public void reset() {
		setDefaultFocus();
		title.setText("");
		firstName.setText("");
		lastName.setText("");
		userName.setText("");
		email.setText("");
		profImg.setText("");

		dirtyFlag = false;
		actionComplete = false;
		containerErrorFixed();
		addKeyPressHandlers();
	}

	@UiHandler("cancelBtn")
	public void cancel(ClickEvent event) {
		Window.Location.assign(uaUrlGenerator.getSourceUrl(urlInfo));
	}

	/*
	 * TODO:validation public boolean validateFields(){
	 * if(("".equals(this.title.getText()))||(this.title.getText()!=null)){ return false; } return true; }
	 */

	@UiHandler("submitBtn")
	public void submit(ClickEvent event) {
		UserManagerRequest umr = clientFactory.getRequestFactory().userManager();
		UserPx uPxToModify = umr.edit(this.uPx);

		uPxToModify.setTitle((this.title.getText().isEmpty() ? null : this.title.getText()));
		uPxToModify.setFirstName((this.firstName.getText().isEmpty() ? null : this.firstName.getText()));
		uPxToModify.setLastName((this.lastName.getText().isEmpty() ? null : this.lastName.getText()));
		uPxToModify.setProfileImage((this.profImg.getText().isEmpty() ? null : this.profImg.getText()));
		/*
		 * TODO: uPxToModify.setDefaultLanguage(defaultLanguage);
		 * 
		 * uPxToModify.setStatus(status);
		 */
		containerErrorFixed();
		if (validate(uPxToModify)) {
			presenter.updateUserProfile(umr, uPxToModify);
			actionComplete = true;
		}
	}

	private boolean validate(UserPx uPxToModify) {
		javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<UserPx>> violations = validator.validate(uPxToModify);
		boolean valid = true;
		if (violations.size() > 0) {
			valid = false;
			for (ConstraintViolation<UserPx> constraintViolation : violations) {
				if ("title".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					titleContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
				if ("firstName".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					firstNameContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
				if ("lastName".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					lastNameContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
				if ("profileImage".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					profImgContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
			}
		}
		if (uPxToModify.getProfileImage() != null && !uPxToModify.getProfileImage().isEmpty() && !ConstraintRegularExpressions.validateUrl(uPxToModify.getProfileImage())) {
			this.profImgContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.invalidURL(), false);
			valid = false;
		}
		return valid;
	}

	private void containerErrorFixed() {
		titleContainer.errorFixed();
		firstNameContainer.errorFixed();
		lastNameContainer.errorFixed();
		profImgContainer.errorFixed();
	}

	@Override
	public void setUserDetails(UserPx uPx) {
		this.uPx = uPx;
		if (uPx.getTitle() != null) {
			this.title.setText(uPx.getTitle());
		}
		if (uPx.getFirstName() != null) {
			this.firstName.setText(uPx.getFirstName());
		}
		if (uPx.getLastName() != null) {
			this.lastName.setText(uPx.getLastName());
		}
		if (uPx.getUserName() != null) {
			this.userName.setText(uPx.getUserName());
		}
		if (uPx.getProfileImage() != null) {
			this.profImg.setText(uPx.getProfileImage());
		}
		if (uPx.getEmailId() != null) {
			this.email.setText(uPx.getEmailId());
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
		this.title.setFocus(true);
	}

}
