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
import com.agnie.gwt.common.client.rpc.AsyncDP;
import com.agnie.gwt.common.client.widget.CustomListBox;
import com.agnie.gwt.common.client.widget.CustomListBox.ChangeHandler;
import com.agnie.gwt.common.client.widget.FormFieldContainer;
import com.agnie.gwt.common.client.widget.ListBox.GetText;
import com.agnie.gwt.common.client.widget.SelectUnselect;
import com.agnie.useradmin.common.client.base.Sort;
import com.agnie.useradmin.main.client.I18;
import com.agnie.useradmin.main.client.injector.MVPInjector;
import com.agnie.useradmin.main.client.mvp.ClientFactory;
import com.agnie.useradmin.main.client.presenter.RolesPresenter;
import com.agnie.useradmin.main.client.presenter.sahered.ui.RolesCellTable;
import com.agnie.useradmin.main.client.ui.DesktopViewFactory.AuthLevelWrapper;
import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.shared.proxy.PermissionPx;
import com.agnie.useradmin.persistance.shared.proxy.RolePx;
import com.agnie.useradmin.persistance.shared.service.RoleManagerRequest;
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
public class DeskRolesView extends Composite implements RolesView {

	interface MyUiBinder extends UiBinder<Widget, DeskRolesView> {
	}

	interface MyStyle extends CssResource {
		String rgtMargin();
	}

	@UiField
	MyStyle									style;

	MainPageResources						resource			= MainPageResources.INSTANCE;
	private static MyUiBinder				uiBinder			= GWT.create(MyUiBinder.class);

	Button									delete				= new Button();
	Button									addRoles			= new Button();
	@UiField
	Button									save;
	@UiField
	HTMLPanel								rolesTblPan;
	@UiField
	HTMLPanel								createRole;
	@UiField
	HTMLPanel								permCont;
	@UiField
	HTMLPanel								delAddBtnPan;

	@UiField
	TextBox									name;
	@UiField
	TextArea								description;

	@UiField
	FormFieldContainer						nameContainer;
	@UiField
	FormFieldContainer						permissionsContainer;
	@UiField
	HTMLPanel								levelContainer;

	HTMLPanel								container;
	private RolesCellTable					rct;
	@Inject
	private RolesPresenter					presenter;
	@Inject
	private ClientFactory					clientFactory;
	@Inject
	private MVPInjector						injector;

	private List<PermissionPx>				totalPermissions	= new ArrayList<PermissionPx>();
	private SelectUnselect<PermissionPx>	selUnsel;

	private boolean							dirtyFlag			= false;
	private boolean							actionComplete		= false;
	private List<HandlerRegistration>		handlerReg			= new ArrayList<HandlerRegistration>();

	private RolePx							rolePxToEdit;
	private boolean							roleSelectedToEdit	= false;
	private CustomListBox<Title>			levelFilter;

	@Inject
	public DeskRolesView(RolesCellTable rct) {
		this.rct = rct;
		container = (HTMLPanel) uiBinder.createAndBindUi(this);
		initWidget(container);
		save.setEnabled(false);

		initSelUnsel();
		rolesTblPan.add(rct);
	}

	public void initSelUnsel() {
		selUnsel = new SelectUnselect<PermissionPx>(new GetText<PermissionPx>() {
			public String getText(PermissionPx object) {
				if (object != null) {
					return object.getName();
				}
				return "";
			}
		}, new ArrayList<PermissionPx>(), new ArrayList<PermissionPx>());

		selUnsel.addStyleName("clearfix");
		selUnsel.setButtonStyle(resource.css().toggleButton());
		permCont.add(selUnsel);
	}

	private void initDelBtn() {
		delete.setText(I18.messages.delete());
		delete.addStyleName("red-button ");
		delete.addStyleName(style.rgtMargin());
		delAddBtnPan.add(delete);
		delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<RolePx> selRolePx = DeskRolesView.this.rct.getSelectionModel().getSelectedSet();
				final List<String> selRoleIdList = new ArrayList<String>();
				selRoleIdList.clear();
				for (RolePx rolePx : selRolePx) {
					selRoleIdList.add(rolePx.getId());
				}
				if (!(selRoleIdList.isEmpty())) {
					presenter.deletRole(selRoleIdList);
				}
			}
		});
	}

	private void initAddRoleBtn() {
		addRoles.setText(I18.messages.addRole());
		addRoles.addStyleName("green-button");
		delAddBtnPan.add(addRoles);
		addRoles.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				reset();
			}
		});
	}

	@UiHandler("save")
	public void save(ClickEvent event) {
		RoleManagerRequest rmr = clientFactory.getRequestFactory().roleManager();
		RolePx rolePx;
		if (roleSelectedToEdit) {
			rolePx = rmr.edit(getRolePxToEdit());
			setRoleSelToEdit(false);
		} else {
			rolePx = rmr.create(RolePx.class);
		}

		rolePx.setName(name.getText());
		rolePx.setDescription(description.getText());
		AuthLevelWrapper level = (AuthLevelWrapper) levelFilter.getSelectedItem();
		if (AuthLevel.CONTEXT.equals(level.getLevel())) {
			rolePx.setLevel(AuthLevel.CONTEXT);
		} else {
			rolePx.setLevel(AuthLevel.APPLICATION);
		}
		rolePx.setPermissions(this.selUnsel.getSelected());
		errorFixed();
		if (validatRoles(rolePx)) {
			presenter.saveRole(rmr, rolePx);
		}
	}

	private boolean validatRoles(RolePx rolePx) {
		boolean response = true;
		if (rolePx.getPermissions().size() <= 0) {
			permissionsContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.permissionsLength(), false);
			response = false;
		}
		javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<RolePx>> violations = validator.validate(rolePx);
		if (violations.size() > 0) {
			response = false;
			for (ConstraintViolation<RolePx> constraintViolation : violations) {
				if (("name").equalsIgnoreCase((constraintViolation.getPropertyPath().toString()))) {
					nameContainer.setError(com.agnie.useradmin.common.client.I18.errorMessages.getString(constraintViolation.getMessage()), false);
				}
			}
		}
		return response;
	}

	public List<PermissionPx> cloneList(List<PermissionPx> permPxList) {
		List<PermissionPx> cloned = new ArrayList<PermissionPx>();
		if (!(permPxList.isEmpty())) {
			for (PermissionPx permissionPx : permPxList) {
				cloned.add(permissionPx);
			}
		}
		return cloned;
	}

	public void setListPermissionPx(List<PermissionPx> permPxList) {
		this.totalPermissions = permPxList;
		this.selUnsel.setAvailabelData(cloneList(permPxList));
	}

	public void setRoleSelToEdit(boolean set) {
		this.roleSelectedToEdit = set;
	}

	public void setRolePxToEdit(RolePx rolePxToEdit) {
		this.rolePxToEdit = rolePxToEdit;
	}

	public RolePx getRolePxToEdit() {
		return this.rolePxToEdit;
	}

	public void setRolesViewData(RolePx rolePx) {
		this.setDefaultFocus();
		this.name.setText(rolePx.getName());
		this.description.setText(rolePx.getDescription());
		this.selUnsel.setSelectedData(rolePx.getPermissions());
		this.selUnsel.setAvailabelData(getPermissionsInEdit(rolePx));
	}

	private List<PermissionPx> getPermissionsInEdit(RolePx rolePx) {
		List<PermissionPx> permAvlInEdit = new ArrayList<PermissionPx>();
		for (PermissionPx perPx : totalPermissions) {
			if (!(rolePx.getPermissions().contains(perPx))) {
				permAvlInEdit.add(perPx);
			}
		}
		return permAvlInEdit;
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

		HandlerRegistration nameHandlerReg = name.addKeyPressHandler(handler);
		HandlerRegistration descriptionHandlerReg = description.addKeyPressHandler(handler);

		handlerReg.add(nameHandlerReg);
		handlerReg.add(descriptionHandlerReg);

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
		permissionsContainer.errorFixed();
	}

	public void reset() {
		this.setDefaultFocus();

		errorFixed();
		name.setText("");
		description.setText("");
		selUnsel.setSelectedData(new ArrayList<PermissionPx>());
		selUnsel.setAvailabelData(cloneList(totalPermissions));
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

	public void setDataProvider(AsyncDP<RolePx> dataProvider) {
		rct.setDataProvider(dataProvider);
	}

	public void clearSelection() {
		rct.clearSelection();
	}

	public void refreshPage() {
		rct.refresh();
	}

	public void initialize() {
		rct.initialize();
		delAddBtnPan.clear();
		if (levelFilter == null) {
			levelFilter = getViewFactory().getLevelFilter();
		}
		levelContainer.clear();
		levelContainer.add(levelFilter);
		levelFilter.clearHandler();
		levelFilter.addChangeHandler(levelChangeHandler);
		if (presenter.checkPermission(Permissions.DELETE_ROLE)) {
			initDelBtn();
		}
		if (presenter.checkPermission(Permissions.CREATE_ROLE)) {
			initAddRoleBtn();
		}
		if ((presenter.checkPermission(Permissions.EDIT_ROLE)) && (presenter.checkPermission(Permissions.CREATE_ROLE))) {
			save.setEnabled(true);
		}
	}

	private ChangeHandler	levelChangeHandler	= new ChangeHandler() {

													@Override
													public void onChange() {
														presenter.getPermissions();
														refreshPage();
														reset();
													}

												};

	public Sort getSortInfo() {
		return rct.getSortColumn();
	}

	@Override
	public void setDefaultFocus() {
		this.name.setFocus(true);
	}

	private ViewFactory getViewFactory() {
		return injector.getDesktopViewFactory();
	}

}
