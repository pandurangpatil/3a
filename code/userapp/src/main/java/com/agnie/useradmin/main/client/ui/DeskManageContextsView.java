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

import com.agnie.common.util.client.validator.ConstraintRegularExpressions;
import com.agnie.gwt.common.client.rpc.AsyncDP;
import com.agnie.gwt.common.client.widget.FormFieldContainer;
import com.agnie.useradmin.common.client.base.Sort;
import com.agnie.useradmin.main.client.I18;
import com.agnie.useradmin.main.client.mvp.ClientFactory;
import com.agnie.useradmin.main.client.presenter.ManageContextsPresenter;
import com.agnie.useradmin.main.client.presenter.sahered.ui.ManageContextsCellTable;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.shared.proxy.ContextPx;
import com.agnie.useradmin.persistance.shared.service.ContextManagerRequest;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeskManageContextsView extends Composite implements ManageContextsView {
	interface MyUiBinder extends UiBinder<Widget, DeskManageContextsView> {
	}

	private static MainPageResources	resource	= MainPageResources.INSTANCE;

	interface MyStyle extends CssResource {
		String rgtMargin();
	}

	@UiField
	MyStyle								style;

	private static MyUiBinder			uiBinder		= GWT.create(MyUiBinder.class);

	HTMLPanel							container;

	@UiField
	HTMLPanel							contextTblPan;
	@UiField
	HTMLPanel							createBtnPan;
	@UiField
	HTMLPanel							saveDelBtnPan;

	Button								delete			= new Button();
	Button								save			= new Button();

	Button								createContext	= new Button();

	@UiField
	TextBox								name;
	@UiField
	TextArea							description;
	@UiField
	FormFieldContainer					nameContainer;

	private boolean						dirtyFlag		= false;
	private boolean						actionComplete	= false;
	private List<HandlerRegistration>	handlerReg		= new ArrayList<HandlerRegistration>();

	@Inject
	private ClientFactory				clientFactory;
	@Inject
	private ManageContextsPresenter		presenter;
	ManageContextsCellTable				mcct;
	private ContextPx					contextSelToDelete;

	@Inject
	public DeskManageContextsView(ManageContextsCellTable mcct) {
		this.mcct = mcct;
		container = (HTMLPanel) uiBinder.createAndBindUi(this);
		initWidget(container);
		contextTblPan.add(mcct);
		this.createContext.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DeskManageContextsView.this.reset();
			}
		});
		this.save.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ContextManagerRequest cmr = clientFactory.getRequestFactory().contextManager();
				ContextPx contextPx = cmr.create(ContextPx.class);
				contextPx.setName(DeskManageContextsView.this.name.getText());
				contextPx.setDescription(DeskManageContextsView.this.description.getText());
				nameContainer.errorFixed();
				if (validateContexts(contextPx)) {
					presenter.createContext(cmr, contextPx);
				}
			}
		});
		this.delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.deleteContext(getContextToDelete());
			}
		});
	}

	private void initCreateCtxBtn() {
		this.createContext.setText(I18.messages.createContext());
		this.createContext.addStyleName("green-button");
		createBtnPan.add(createContext);
	}

	private void initSaveBtn() {
		this.save.setText(I18.messages.save());
		this.save.addStyleName("green-button");
		this.save.addStyleName(style.rgtMargin());
		this.saveDelBtnPan.add(save);
	}

	private void initDelBtn() {
		this.delete.setText(I18.messages.delete());
		this.delete.addStyleName("red-button");
	}

	private boolean validateContexts(ContextPx contPx) {
		javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<ContextPx>> violations = validator.validate(contPx);
		if (violations.size() == 0) {
			if (ConstraintRegularExpressions.validateWhiteSpace(contPx.getName())) {
				return true;
			} else {
				this.nameContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.noWhiteSpace(), false);
				return false;
			}

		} else {
			for (ConstraintViolation<ContextPx> constraintViolation : violations) {
				if ((I18.messages.name().toUpperCase()).equals((constraintViolation.getPropertyPath().toString().toUpperCase())))
					nameContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
			}
			return false;
		}
	}

	public void setContextToDelete(ContextPx context) {
		this.contextSelToDelete = context;
	}

	public ContextPx getContextToDelete() {
		return this.contextSelToDelete;
	}

	public void deleteBtnVisible(boolean visible) {
		this.delete.setVisible(visible);
	}

	public void enableSaveButton(boolean enable) {
		if (!enable) {
			this.save.setEnabled(enable);
			this.save.removeStyleName("green-button");
			this.save.addStyleName(resource.css().disableSave());
		} else {
			this.save.setEnabled(enable);
			this.save.removeStyleName(resource.css().disableSave());
			this.save.addStyleName("green-button");
		}
	}

	public void setManContextViewData(ContextPx contPx) {
		this.name.setText(contPx.getName());
		this.description.setText(contPx.getDescription());
	}

	private void addKeyPressHandlers() {
		KeyPressHandler handler = new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				dirtyFlag = true;
				actionComplete = false;/* TODO:submit button absent here */
				clearHandlerRegistration();
			}
		};

		handlerReg.add(name.addKeyPressHandler(handler));
		handlerReg.add(description.addKeyPressHandler(handler));

	}

	private void clearHandlerRegistration() {
		for (int i = 0; i < handlerReg.size(); i++) {
			handlerReg.get(i).removeHandler();
		}
		// before adding new set of handlers clearing existing list.
		handlerReg.clear();
	}

	/**
	 * to reset view data must be called in respective presenter.<br>
	 * before adding this view to webPage.
	 */
	public void reset() {
		this.setDefaultFocus();
		nameContainer.errorFixed();
		name.setText("");
		description.setText("");

		enableSaveButton(true);
		deleteBtnVisible(false);
		dirtyFlag = false;
		actionComplete = false;
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

	public void setDataProvider(AsyncDP<ContextPx> dataProvider) {
		mcct.setDataProvider(dataProvider);
	}

	public void clearSelection() {
		mcct.clearSelection();
	}

	public void refreshPage() {
		mcct.refresh();
	}

	public void initialize() {
		mcct.initialize();
		this.createBtnPan.clear();
		this.saveDelBtnPan.clear();
		if (presenter.checkPermission(Permissions.CREATE_CONTEXT)) {
			initCreateCtxBtn();
			initSaveBtn();
		}
		if (presenter.checkPermission(Permissions.ADMIN_DELETE_CONTEXT)) {
			initDelBtn();
		}
	}

	public Sort getSortInfo() {
		return mcct.getSortColumn();
	}

	@Override
	public void setDefaultFocus() {
		this.name.setFocus(true);
	}

}
