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
package com.agnie.useradmin.main.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import com.agnie.common.gwt.serverclient.client.renderer.Title;
import com.agnie.common.util.client.validator.ConstraintRegularExpressions;
import com.agnie.gwt.common.client.widget.CustomListBox;
import com.agnie.gwt.common.client.widget.FormFieldContainer;
import com.agnie.useradmin.common.client.renderer.TitleCell;
import com.agnie.useradmin.main.client.I18;
import com.agnie.useradmin.main.client.injector.MVPInjector;
import com.agnie.useradmin.main.client.mvp.ClientFactory;
import com.agnie.useradmin.main.client.presenter.DomainPresenter;
import com.agnie.useradmin.persistance.client.enums.RequestStatus;
import com.agnie.useradmin.persistance.shared.proxy.ApplicationPx;
import com.agnie.useradmin.persistance.shared.service.ApplicationManagerRequest;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DomainEditView extends Composite {

	interface MyUiBinder extends UiBinder<Widget, DomainEditView> {
	}

	private static MyUiBinder			uiBinder	= GWT.create(MyUiBinder.class);

	@UiField
	Button								update;
	@UiField
	Button								cancel;
	@UiField
	FormFieldContainer					domainContainer;
	@UiField
	FormFieldContainer					bussinessNameContainer;
	@UiField
	FormFieldContainer					homePageUrlContainer;
	@UiField
	FormFieldContainer					iconUrlContainer;
	@UiField
	FormFieldContainer					supportCntContainer;
	@UiField
	FormFieldContainer					defAppStatusContainer;
	@UiField
	FormFieldContainer					defContextStatusContainer;

	@UiField
	Label								domain;
	/*
	 * @UiField TextBox domain;
	 */
	@UiField
	TextBox								bussinessName;
	@UiField
	TextBox								homePageUrl;
	@UiField
	TextBox								iconUrl;
	@UiField
	TextBox								supportCnt;
	@UiField
	HTMLPanel							defAppStatCont;
	@UiField
	HTMLPanel							defContextStatCont;

	@Inject
	private ClientFactory				clientFactory;
	@Inject
	private DomainPresenter				presenter;
	@Inject
	private MVPInjector					injector;
	private CustomListBox<Title>		defAppStatus;
	private CustomListBox<Title>		defContextStatus;
	ApplicationPx						editpx;

	private List<HandlerRegistration>	handlerReg	= new ArrayList<HandlerRegistration>();

	public DomainEditView() {
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

		update.setText(I18.messages.update());
		cancel.setText(I18.messages.cancel());
	}

	private void addKeyPressHandlers() {
		KeyPressHandler handler = new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				clearHandlerRegistration();
				containerErrorFixed();
			}
		};

		// handlerReg.add(domain.addKeyPressHandler(handler));
		handlerReg.add(bussinessName.addKeyPressHandler(handler));
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

	public void setData(ApplicationPx appPx) {
		this.domain.setText(appPx.getDomain());
		this.bussinessName.setText(appPx.getBusinessName());
		this.homePageUrl.setText(appPx.getURL());
		this.iconUrl.setText(appPx.getIconURL());
		this.supportCnt.setText(appPx.getContactEmail());
		this.editpx = appPx;
		defAppStatus.setSelectedItem(appPx.getDefaultAppStatus(), true);
		defContextStatus.setSelectedItem(appPx.getDefaultCtxStatus(), true);
	}

	@UiHandler("update")
	public void update(ClickEvent event) {

		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		boolean domainChange = false;
		/*
		 * if (!(editpx.getDomain().equals(this.domain.getText()))) { domainChange = true;//To change url only if domain
		 * value changes }
		 */
		ApplicationPx appPx = amr.edit(editpx);
		// appPx.setDomain((this.domain.getText().isEmpty() ? null : this.domain.getText()));
		appPx.setBusinessName((this.bussinessName.getText().isEmpty() ? null : this.bussinessName.getText()));
		appPx.setIconURL((this.iconUrl.getText().isEmpty() ? null : this.iconUrl.getText()));
		appPx.setURL((this.homePageUrl.getText().isEmpty() ? null : this.homePageUrl.getText()));
		appPx.setContactEmail((this.supportCnt.getText().isEmpty() ? null : this.supportCnt.getText()));
		appPx.setDefaultAppStatus((RequestStatus) this.defAppStatus.getSelectedItem());
		appPx.setDefaultCtxStatus((RequestStatus) this.defContextStatus.getSelectedItem());
		containerErrorFixed();
		if (validate(appPx)) {
			presenter.editApp(amr, appPx, domainChange);
		}
	}

	@UiHandler("cancel")
	public void cancel(ClickEvent event) {
		getViewFactory().getDomainParentView().swithToReadView();
		containerErrorFixed();

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
					bussinessNameContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
				if ("iconURL".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					iconUrlContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
				if ("contactEmail".equalsIgnoreCase(constraintViolation.getPropertyPath().toString())) {
					supportCntContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}

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
		if (appPx.getContactEmail() != null && !appPx.getContactEmail().isEmpty() && !ConstraintRegularExpressions.validateEmail(appPx.getContactEmail())) {
			this.supportCntContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.invalidEmail(), false);
			valid = false;
		}
		return valid;
	}

	public void reset() {
		containerErrorFixed();
		addKeyPressHandlers();
	}

	/**
	 * To clear FormFieldContainer's error style
	 */
	private void containerErrorFixed() {
		// domainContainer.errorFixed();
		bussinessNameContainer.errorFixed();
		homePageUrlContainer.errorFixed();
		iconUrlContainer.errorFixed();
		supportCntContainer.errorFixed();
	}

	private DesktopViewFactory getViewFactory() {
		return injector.getDesktopViewFactory();
	}
}
