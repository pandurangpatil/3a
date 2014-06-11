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

import com.agnie.common.gwt.serverclient.client.helper.URLConfiguration;
import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.common.gwt.serverclient.client.renderer.Title;
import com.agnie.common.util.client.validator.ConstraintRegularExpressions;
import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.gwt.common.client.widget.CustomListBox;
import com.agnie.gwt.common.client.widget.FormFieldContainer;
import com.agnie.useradmin.common.client.renderer.TitleCell;
import com.agnie.useradmin.landing.client.Messages;
import com.agnie.useradmin.landing.client.injector.MVPInjector;
import com.agnie.useradmin.landing.client.mvp.ClientFactory;
import com.agnie.useradmin.landing.client.mvp.LandingAppController;
import com.agnie.useradmin.landing.client.mvp.PlaceToken;
import com.agnie.useradmin.landing.client.presenter.NewAppPresenter;
import com.agnie.useradmin.persistance.client.enums.RequestStatus;
import com.agnie.useradmin.persistance.shared.proxy.ApplicationPx;
import com.agnie.useradmin.persistance.shared.service.ApplicationManagerRequest;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeskNewAppView extends Composite implements NewAppView {
	@Inject
	MVPInjector				injector;

	@Inject
	NewAppPresenter			presenter;

	@Inject
	ClientFactory			clientFactory;

	@Inject
	LandingAppController	appController;

	@Inject
	URLConfiguration		urlConfig;

	@Inject
	Messages				messages;

	@Inject
	URLInfo					params;

	interface MyUiBinder extends UiBinder<Widget, DeskNewAppView> {
	}

	private static MyUiBinder			uiBinder					= GWT.create(MyUiBinder.class);
	@UiField
	TextBox								domain;
	@UiField
	TextBox								bussName;
	@UiField
	TextBox								homePageUrl;
	@UiField
	TextBox								iconUrl;
	@UiField
	TextBox								supportCnt;

	// @UiField
	// HTMLPanel custProfileCont;
	@UiField
	HTMLPanel							defAppStatCont;
	@UiField
	HTMLPanel							defContextStatCont;
	@UiField
	Button								submitBtn;
	@UiField
	Button								cancelBtn;

	@UiField
	FormFieldContainer					bussNameContainer;
	@UiField
	FormFieldContainer					domainContainer;
	@UiField
	FormFieldContainer					homePageUrlContainer;
	@UiField
	FormFieldContainer					iconUrlContainer;
	@UiField
	FormFieldContainer					supportCntContainer;
	// @UiField
	// FormFieldContainer custProfileContainer;

	private boolean						dirtyFlag					= false;
	private boolean						actionComplete				= false;
	private List<HandlerRegistration>	handlerReg					= new ArrayList<HandlerRegistration>();

	private CustomListBox<Title>		defAppStatus;
	private CustomListBox<Title>		defContextStatus;
	// private CustomListBox<Title> customerProfiles;

	private boolean						domainAvailable				= false;
	private boolean						domainCheckUnderProgress	= false;

	public DeskNewAppView() {
		initWidget(uiBinder.createAndBindUi(this));

		List<Title> reqStatus = new ArrayList<Title>();
		for (RequestStatus status : RequestStatus.values()) {
			reqStatus.add(status);
		}
		defAppStatus = new CustomListBox<Title>(new TitleCell());
		defAppStatus.setList(reqStatus);
		defAppStatus.setSelectedItem(RequestStatus.ACTIVE, true);
		defAppStatCont.add(defAppStatus);

		defContextStatus = new CustomListBox<Title>(new TitleCell());
		defContextStatus.setList(reqStatus);
		defContextStatus.setSelectedItem(RequestStatus.ACTIVE, true);
		defContextStatCont.add(defContextStatus);

	}

	private void addKeyPressHandlers() {
		KeyPressHandler handler = new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				dirtyFlag = true;
				clearHandlerRegistration();
			}
		};

		handlerReg.add(domain.addKeyPressHandler(handler));
		handlerReg.add(bussName.addKeyPressHandler(handler));
		handlerReg.add(homePageUrl.addKeyPressHandler(handler));
		handlerReg.add(iconUrl.addKeyPressHandler(handler));
		handlerReg.add(supportCnt.addKeyPressHandler(handler));
	}

	private void clearHandlerRegistration() {
		for (int i = 0; i < handlerReg.size(); i++) {
			handlerReg.get(i).removeHandler();
		}
		// before adding new set of handlers clearing existing list.
		handlerReg.clear();
	}

	public void reset() {
		domain.setText("");
		bussName.setText("");
		homePageUrl.setText("");
		iconUrl.setText("");
		supportCnt.setText("");
		dirtyFlag = false;
		actionComplete = false;
		domainAvailable = false;
		containerErrorFixed();
		addKeyPressHandlers();
	}

	/**
	 * To clear FormFieldContainer's error style
	 */
	private void containerErrorFixed() {
		domainContainer.errorFixed();
		bussNameContainer.errorFixed();
		homePageUrlContainer.errorFixed();
		iconUrlContainer.errorFixed();
		supportCntContainer.errorFixed();
	}

	@UiHandler("domain")
	public void onBlurHandler(BlurEvent event) {
		String domainName = this.domain.getText();
		if (domainName != null && !domainName.isEmpty() && !domainCheckUnderProgress) {
			domainCheckUnderProgress = true;
			presenter.checkIfAvilable(domainName);
		}
	}

	@UiHandler("cancelBtn")
	public void cancel(ClickEvent event) {
		appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.LANDING));
	}

	@UiHandler("submitBtn")
	public void submit(ClickEvent event) {

		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		ApplicationPx appPx = amr.create(ApplicationPx.class);
		appPx.setDomain((this.domain.getText().isEmpty() ? null : this.domain.getText()));
		appPx.setBusinessName((this.bussName.getText().isEmpty() ? null : this.bussName.getText()));
		appPx.setIconURL((this.iconUrl.getText().isEmpty() ? null : this.iconUrl.getText()));
		appPx.setURL((this.homePageUrl.getText().isEmpty() ? null : this.homePageUrl.getText()));
		appPx.setContactEmail((this.supportCnt.getText().isEmpty() ? null : this.supportCnt.getText()));
		appPx.setDefaultAppStatus((RequestStatus) this.defAppStatus.getSelectedItem());
		appPx.setDefaultCtxStatus((RequestStatus) this.defContextStatus.getSelectedItem());
		// if (this.customerProfiles != null) {
		// appPx.setCustomerProfileId(((Customer) this.customerProfiles.getSelectedItem()).getId());
		// }
		containerErrorFixed();
		if (validate(appPx)) {
			presenter.createNewApp(amr, appPx);
			actionComplete = true;
		}
	}

	private boolean validate(ApplicationPx appPx) {
		javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<ApplicationPx>> violations = validator.validate(appPx);
		boolean valid = true;
		if (violations.size() > 0) {
			valid = false;
			for (ConstraintViolation<ApplicationPx> constraintViolation : violations) {
				if ("domain".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					domainContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
				if ("URL".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					homePageUrlContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
				if ("businessName".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					bussNameContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
				if ("iconURL".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					iconUrlContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
				if ("contactEmail".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					supportCntContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
				// if ("customerProfileId".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
				// custProfileContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()),
				// false);
				// }
			}
		}
		if (appPx.getURL() != null && !appPx.getURL().isEmpty() && !ConstraintRegularExpressions.validateUrl(appPx.getURL())) {
			this.homePageUrlContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.invalidURL(), false);
			valid = false;
		}
		if (appPx.getIconURL() != null && !appPx.getIconURL().isEmpty() && !ConstraintRegularExpressions.validateUrl(appPx.getIconURL())) {
			this.iconUrlContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.invalidURL(), false);
			valid = false;
		}
		if (appPx.getIconURL() != null && !appPx.getIconURL().isEmpty() && !ConstraintRegularExpressions.validateWhiteSpace(appPx.getIconURL())) {
			this.iconUrlContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.noWhiteSpace(), false);
			valid = false;
		}
		if (appPx.getDomain() != null && !appPx.getDomain().isEmpty() && !ConstraintRegularExpressions.validateWhiteSpace(appPx.getDomain())) {
			this.domainContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.noWhiteSpace(), false);
			valid = false;
		}
		if (!domainAvailable && domain.getText() != null && !(domain.getText().isEmpty())) {
			this.domainContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.domain_not_available(), false);
			valid = false;
		}
		if (appPx.getContactEmail() != null && !appPx.getContactEmail().isEmpty() && !ConstraintRegularExpressions.validateEmail(appPx.getContactEmail())) {
			this.supportCntContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.invalidEmail(), false);
			valid = false;
		}
		return valid;
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
		this.domain.setFocus(true);
	}

	@Override
	public void isAvilableResp(Boolean resp) {
		domainCheckUnderProgress = false;
		domainAvailable = resp;
		if (!resp) {
			this.domainContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.domain_not_available(), false);
		} else {
			domainContainer.errorFixed();
		}
	}
}
