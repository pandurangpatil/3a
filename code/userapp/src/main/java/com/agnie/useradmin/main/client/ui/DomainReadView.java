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

import com.agnie.gwt.common.client.widget.DialogBox;
import com.agnie.useradmin.common.widget.RolesManager;
import com.agnie.useradmin.main.client.I18;
import com.agnie.useradmin.main.client.presenter.DomainPresenter;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.shared.proxy.ApplicationPx;
import com.agnie.useradmin.persistance.shared.proxy.RolePx;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DomainReadView extends Composite {

	interface MyUiBinder extends UiBinder<Widget, DomainReadView> {
	}

	private static MyUiBinder	uiBinder	= GWT.create(MyUiBinder.class);
	MainPageResources			resource	= MainPageResources.INSTANCE;

	@UiField
	Image						editAppImg;
	@UiField
	Image						editAppRoles;
	@UiField
	Image						editContextRoles;

	@UiField
	HTMLPanel					defAppRolesCont;
	@UiField
	HTMLPanel					defContextRolesCont;

	@UiField
	Label						domain;
	@UiField
	Label						bussinessName;
	@UiField
	Label						homePageUrl;
	@UiField
	Label						iconUrl;
	@UiField
	Label						supportCnt;
	@UiField
	Label						defAppStatus;
	@UiField
	Label						defContextStatus;
	@UiField
	Label						apiAcessKey;

	@UiField
	Button						regenerateApiAcessKey;
	// @UiField
	// Button transferOwner;
	// @UiField
	// TextBox newowner;
	ApplicationPx				editpx;

	private DialogBox			editPopUp;
	private DomainEditView		dev;
	private RolesManager		appRolesManager;
	private RolesManager		ctxRolesManager;
	@Inject
	private DomainPresenter		presenter;
	private ListBox				defAppRolesList;
	private ListBox				defContextRolesList;
	private DialogBox			popUpPan;
	List<RolePx>				totalAppRoles;
	List<RolePx>				totalContextRoles;

	@Inject
	public DomainReadView(RolesManager appRolesManager, RolesManager ctxRolesManager, DomainEditView dev) {
		this.dev = dev;
		this.appRolesManager = appRolesManager;
		this.ctxRolesManager = ctxRolesManager;
		initWidget(uiBinder.createAndBindUi(this));
		editPencilImgsVisible(false);

		defAppRolesList = new ListBox(true);
		defAppRolesCont.add(defAppRolesList);

		defContextRolesList = new ListBox(true);
		defContextRolesCont.add(defContextRolesList);

		editPopUp = new DialogBox(I18.messages.editDomain(), dev);
		editPopUp.addCloseHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				editPopUp.hide();
			}
		});

		appRolesManager.getCancelBtn().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popUpPan.hide();
			}
		});

		appRolesManager.getUpdateBtn().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.updateDefAppRoles(DomainReadView.this.appRolesManager.getSelUnsel().getSelected());
			}
		});

		ctxRolesManager.getCancelBtn().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popUpPan.hide();
			}
		});

		ctxRolesManager.getUpdateBtn().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.updateDefCTXRoles(DomainReadView.this.ctxRolesManager.getSelUnsel().getSelected());
			}
		});
	}

	// @UiHandler("transferOwner")
	// public void transferOwnerShip(ClickEvent event) {
	// if (Window.confirm(I18.messages.confirm_transfer())) {
	// presenter.transferOwnerShip(newowner.getText());
	// }
	// }

	@UiHandler("regenerateApiAcessKey")
	public void regenerateApiAcessKey(ClickEvent event) {
		presenter.generateNewApiAccessKey(editpx);
	}

	public void editPencilImgsVisible(boolean visible) {
		editAppImg.setVisible(visible);
		editAppRoles.setVisible(visible);
		editContextRoles.setVisible(visible);
	}

	public void setTotalAppRoles(List<RolePx> appRoles) {
		this.totalAppRoles = appRoles;
	}

	public void setTotalContextRoles(List<RolePx> ctxRoles) {
		this.totalContextRoles = ctxRoles;
	}

	public void hidePopUp() {
		this.popUpPan.hide();
	}

	private void initPopUp(RolesManager rolesManager) {
		ClickHandler ch = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popUpPan.hide();
			}
		};
		popUpPan = new DialogBox(com.agnie.useradmin.main.client.I18.messages.manageUserRoles(), rolesManager);
		popUpPan.addCloseHandler(ch);
		popUpPan.hide();

	}

	public void setSelAppRolesList(List<RolePx> list) {
		if (list != null) {
			List<RolePx> avlRolesList = new ArrayList<RolePx>();
			for (RolePx rolePx : totalAppRoles) {
				if (!(list.contains(rolePx))) {
					avlRolesList.add(rolePx);
				}
			}
			this.appRolesManager.getSelUnsel().setAvailabelData(avlRolesList);
			this.appRolesManager.getSelUnsel().setSelectedData(list);
		} else {
			this.appRolesManager.getSelUnsel().setAvailabelData(totalAppRoles);
		}
	}

	public void setSelCTXRolesList(List<RolePx> list) {
		if (list != null) {
			List<RolePx> avlRolesList = new ArrayList<RolePx>();
			for (RolePx rolePx : totalContextRoles) {
				if (!(list.contains(rolePx))) {
					avlRolesList.add(rolePx);
				}
			}
			this.ctxRolesManager.getSelUnsel().setAvailabelData(avlRolesList);
			this.ctxRolesManager.getSelUnsel().setSelectedData(list);
		} else {
			this.ctxRolesManager.getSelUnsel().setAvailabelData(totalContextRoles);
		}
	}

	/**
	 * To add selAppRoles in read view
	 * 
	 * @param list
	 */
	public void setdefAppRolesList(List<RolePx> list) {
		if (list != null) {
			this.defAppRolesList.clear();
			for (RolePx rolePx : list) {
				this.defAppRolesList.addItem(rolePx.getName());
			}
		}
	}

	/**
	 * To add selContextRoles in read view
	 * 
	 * @param list
	 */
	public void setdefContextRolesList(List<RolePx> list) {
		if (list != null) {
			this.defContextRolesList.clear();
			for (RolePx rolePx : list) {
				this.defContextRolesList.addItem(rolePx.getName());
			}
		}
	}

	@UiHandler("editContextRoles")
	public void editContextRoles(ClickEvent event) {
		initPopUp(ctxRolesManager);
		popUpPan.show();
		popUpPan.center();
	}

	@UiHandler("editAppRoles")
	public void editAppRoles(ClickEvent event) {
		initPopUp(appRolesManager);
		popUpPan.show();
		popUpPan.center();
	}

	@UiHandler("editAppImg")
	public void editAppImg(ClickEvent event) {
		editPopUp.show();
		editPopUp.center();
		dev.bussinessName.setFocus(true);
	}

	public DialogBox getEditPopUp() {
		return this.editPopUp;
	}

	public void setData(ApplicationPx appPx) {
		dev.setData(appPx);
		this.editpx = appPx;
		domain.setTitle(appPx.getDomain());
		domain.setText(validateStringLength(appPx.getDomain()));
		bussinessName.setTitle(appPx.getBusinessName());
		bussinessName.setText(validateStringLength(appPx.getBusinessName()));
		homePageUrl.setTitle(appPx.getURL());
		homePageUrl.setText(validateStringLength(appPx.getURL()));
		iconUrl.setTitle(appPx.getIconURL());
		iconUrl.setText(validateStringLength(appPx.getIconURL()));
		supportCnt.setTitle(appPx.getContactEmail());
		supportCnt.setText(validateStringLength(appPx.getContactEmail()));
		apiAcessKey.setTitle(appPx.getApiAccessKey());
		apiAcessKey.setText(validateStringLength(appPx.getApiAccessKey()));
		defAppStatus.setText(appPx.getDefaultAppStatus().getLocalized());
		defContextStatus.setText(appPx.getDefaultCtxStatus().getLocalized());
		if (presenter.checkPermission(Permissions.EDIT_APPLICATION_DETAILS)) {
			regenerateApiAcessKey.setEnabled(true);
		} else {
			regenerateApiAcessKey.setEnabled(false);
		}
	}

	private String validateStringLength(String str) {
		String strV = "";
		if (str != null) {
			if (str.length() > 40) {
				String sub = str.substring(0, 36);
				strV = sub + "....";
			} else {
				strV = str;
			}
		}
		return strV;
	}

}
