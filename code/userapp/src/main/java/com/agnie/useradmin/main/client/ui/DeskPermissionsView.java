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
import com.agnie.gwt.common.client.rpc.AsyncDP;
import com.agnie.gwt.common.client.widget.CustomListBox;
import com.agnie.gwt.common.client.widget.CustomListBox.ChangeHandler;
import com.agnie.gwt.common.client.widget.FormFieldContainer;
import com.agnie.useradmin.common.client.base.Sort;
import com.agnie.useradmin.main.client.I18;
import com.agnie.useradmin.main.client.injector.MVPInjector;
import com.agnie.useradmin.main.client.mvp.ClientFactory;
import com.agnie.useradmin.main.client.presenter.PermissionsPresenter;
import com.agnie.useradmin.main.client.presenter.sahered.ui.PermissionsCellTable;
import com.agnie.useradmin.main.client.ui.DesktopViewFactory.AuthLevelWrapper;
import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.shared.proxy.PermissionPx;
import com.agnie.useradmin.persistance.shared.service.PermissionManagerRequest;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
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
public class DeskPermissionsView extends Composite implements PermissionsView {

	interface MyUiBinder extends UiBinder<Widget, DeskPermissionsView> {
	}

	interface MyStyle extends CssResource {
		String rgtMargin();
	}

	@UiField
	MyStyle								style;

	private static MyUiBinder			uiBinder			= GWT.create(MyUiBinder.class);

	@UiField
	HTMLPanel							userTblBtnPan;
	@UiField
	HTMLPanel							userTblPan;

	@UiField
	HTMLPanel							delAddPermBtnPan;

	Button								delete				= new Button();
	Button								addPermissions		= new Button();

	@UiField
	Button								save;

	@UiField
	HTMLPanel							createPermission;
	@UiField
	TextBox								name;
	@UiField
	TextBox								code;
	@UiField
	TextArea							description;
	@UiField
	HTMLPanel							levelContainer;
	@UiField
	FormFieldContainer					nameContainer;
	@UiField
	FormFieldContainer					codeContainer;
	private CustomListBox<Title>		levelFilter;

	HTMLPanel							container;

	public PermissionsCellTable			pct;
	@Inject
	private PermissionsPresenter		presenter;
	@Inject
	private ClientFactory				clientFactory;
	@Inject
	private MVPInjector					injector;
	private boolean						dirtyFlag			= false;
	private boolean						actionComplete		= false;
	private List<HandlerRegistration>	handlerReg			= new ArrayList<HandlerRegistration>();
	private PermissionPx				perPxToEdit;
	private boolean						permSelectedToEdit	= false;

	@Inject
	public DeskPermissionsView(PermissionsCellTable pct) {
		this.pct = pct;
		container = (HTMLPanel) uiBinder.createAndBindUi(this);
		initWidget(container);
		save.setEnabled(false);

		userTblPan.add(pct);
	}

	private void initDelBtn() {
		delete.setText(I18.messages.delete());
		delete.addStyleName("red-button");
		delete.addStyleName(style.rgtMargin());
		delAddPermBtnPan.add(delete);
		delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final List<String> selPermIdList = new ArrayList<String>();
				Set<PermissionPx> selPerPx = pct.getSelectionModel().getSelectedSet();
				selPermIdList.clear();
				for (PermissionPx permissionPx : selPerPx) {
					selPermIdList.add(permissionPx.getId());
				}
				if (!(selPermIdList.isEmpty())) {
					presenter.deletePermissions(selPermIdList);
				}
			}
		});
	}

	private void initAddPermBtn() {
		addPermissions.setText(I18.messages.addPermission());
		addPermissions.addStyleName("green-button");
		addPermissions.addStyleName(style.rgtMargin());
		delAddPermBtnPan.add(addPermissions);
		addPermissions.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DeskPermissionsView.this.reset();
			}
		});
	}

	@UiHandler("save")
	public void save(ClickEvent event) {
		PermissionManagerRequest pmr = clientFactory.getRequestFactory().permissionManager();
		PermissionPx permPx;
		if (permSelectedToEdit) {
			permPx = pmr.edit(this.getPerPxToEdit());
			this.setPermSelToEdit(false);
		} else {
			permPx = pmr.create(PermissionPx.class);
		}

		permPx.setName(DeskPermissionsView.this.name.getText());
		permPx.setDescription(DeskPermissionsView.this.description.getText());
		permPx.setCode(DeskPermissionsView.this.code.getText());
		AuthLevelWrapper level = (AuthLevelWrapper) levelFilter.getSelectedItem();
		if (AuthLevel.CONTEXT.equals(level.getLevel())) {
			permPx.setLevel(AuthLevel.CONTEXT);
		} else {
			permPx.setLevel(AuthLevel.APPLICATION);
		}
		errorFixed();
		if (validatePresmissions(permPx)) {
			presenter.savePermission(permPx, pmr);
			errorFixed();
		}
	}

	private boolean validatePresmissions(PermissionPx permPx) {
		javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<PermissionPx>> violations = validator.validate(permPx);
		boolean valid = true;
		if (violations.size() > 0) {
			valid = false;
			for (ConstraintViolation<PermissionPx> constraintViolation : violations) {
				if (("name").equalsIgnoreCase((constraintViolation.getPropertyPath().toString()))) {
					nameContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
				if (("code").equalsIgnoreCase((constraintViolation.getPropertyPath().toString()))) {
					codeContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
			}
		}
		if (permPx.getCode() != null && !permPx.getCode().isEmpty() && !ConstraintRegularExpressions.validateWhiteSpace(permPx.getCode())) {
			valid = false;
			this.codeContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.noWhiteSpace(), false);
		}
		return valid;
	}

	public void setPermSelToEdit(boolean set) {
		this.permSelectedToEdit = set;
	}

	public void setPerPxToEdit(PermissionPx perPxToEdit) {
		this.perPxToEdit = perPxToEdit;
	}

	public PermissionPx getPerPxToEdit() {
		return this.perPxToEdit;
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
		handlerReg.add(code.addKeyPressHandler(handler));
		handlerReg.add(description.addKeyPressHandler(handler));

	}

	private void clearHandlerRegistration() {
		for (int i = 0; i < handlerReg.size(); i++) {
			handlerReg.get(i).removeHandler();
		}
		// before adding new set of handlers clearing existing list.
		handlerReg.clear();
	}

	public void errorFixed() {
		nameContainer.errorFixed();
		codeContainer.errorFixed();
	}

	/**
	 * to reset view data must be called in respective presenter.
	 */
	public void reset() {
		this.setDefaultFocus();
		errorFixed();
		name.setText("");
		code.setText("");
		description.setText("");

		dirtyFlag = false;
		actionComplete = false;
		errorFixed();
		addKeyPressHandlers();
		permSelectedToEdit = false;
		perPxToEdit = null;
	}

	public void setPermissionsViewData(PermissionPx permPx) {
		this.setDefaultFocus();
		this.name.setText(permPx.getName());
		this.description.setText(permPx.getDescription());
		this.code.setText(permPx.getCode());
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

	public void setDataProvider(AsyncDP<PermissionPx> dataProvider) {
		pct.setDataProvider(dataProvider);
	}

	public void clearSelection() {
		pct.clearSelection();
	}

	public void refreshPage() {
		pct.refresh();
	}

	public void initialize() {
		pct.initialize();
		delAddPermBtnPan.clear();
		if (levelFilter == null) {
			levelFilter = getViewFactory().getLevelFilter();
		}
		levelContainer.clear();
		levelContainer.add(levelFilter);
		levelFilter.clearHandler();
		levelFilter.addChangeHandler(levelChangeHandler);
		if (presenter.checkPermission(Permissions.DELETE_PERMISSION)) {
			initDelBtn();
		}
		if (presenter.checkPermission(Permissions.CREATE_PERMISSION)) {
			initAddPermBtn();
		}
		if ((presenter.checkPermission(Permissions.EDIT_PERMISSION)) && (presenter.checkPermission(Permissions.CREATE_PERMISSION))) {
			save.setEnabled(true);
		}
	}

	public Sort getSortInfo() {
		return pct.getSortColumn();
	}

	@Override
	public void setDefaultFocus() {
		this.name.setFocus(true);
	}

	private ViewFactory getViewFactory() {
		return injector.getDesktopViewFactory();
	}

	private ChangeHandler	levelChangeHandler	= new ChangeHandler() {

													@Override
													public void onChange() {
														refreshPage();
														reset();
													}

												};
}
