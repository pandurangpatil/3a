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

import java.util.List;

import com.agnie.useradmin.main.client.presenter.DomainPresenter;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.shared.proxy.ApplicationPx;
import com.agnie.useradmin.persistance.shared.proxy.RolePx;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeskDomainParentView extends Composite implements DomainParentView {
	@Inject
	private DomainReadView	drv;
	@Inject
	private DomainEditView	dev;
	@Inject
	private DomainPresenter	presenter;

	interface MyUiBinder extends UiBinder<Widget, DeskDomainParentView> {
	}

	private static MyUiBinder	uiBinder	= GWT.create(MyUiBinder.class);

	@UiField
	HTMLPanel					domainViewContainer;

	public ApplicationPx		appPx;

	public DeskDomainParentView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void init(ApplicationPx appPx) {
		if (presenter.checkPermission(Permissions.EDIT_APPLICATION_DETAILS)) {
			drv.editPencilImgsVisible(true);
		}
		this.appPx = appPx;
		this.domainViewContainer.clear();
		this.domainViewContainer.add(drv);
		drv.setData(appPx);
		drv.getEditPopUp().hide();
		dev.reset();
	}

	public HTMLPanel getDomainViewContainer() {
		return this.domainViewContainer;
	}

	@Override
	public boolean shouldWeProceed() {
		return true;
	}

	@Override
	public void setDefaultFocus() {
		// It is not applicable as DomainEditView is pop-up panel
	}

	@Override
	public void setData(ApplicationPx appPx) {
		drv.setData(appPx);
	}

	@Override
	public void setTotalAppRoles(List<RolePx> appRoles) {
		drv.setTotalAppRoles(appRoles);
	}

	@Override
	public void setSelAppRolesList(List<RolePx> list) {
		drv.setSelAppRolesList(list);
	}

	@Override
	public void setTotalContextRoles(List<RolePx> ctxRoles) {
		drv.setTotalContextRoles(ctxRoles);
	}

	@Override
	public void setSelCTXRolesList(List<RolePx> list) {
		drv.setSelCTXRolesList(list);
	}

	@Override
	public void setdefAppRolesList(List<RolePx> list) {
		drv.setdefAppRolesList(list);
	}

	@Override
	public void setdefContextRolesList(List<RolePx> list) {
		drv.setdefContextRolesList(list);
	}

	@Override
	public void hideReadView() {
		drv.hidePopUp();
	}

	@Override
	public void swithToReadView() {
		this.domainViewContainer.clear();
		this.domainViewContainer.add(drv);
		drv.getEditPopUp().hide();
	}

}
