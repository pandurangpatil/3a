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
package com.agnie.useradmin.contextmgr.client.ui;

import java.util.List;

import com.agnie.gwt.common.client.rpc.AsyncDP;
import com.agnie.useradmin.common.client.base.CellTableView;
import com.agnie.useradmin.common.client.helper.RequestStatusWrapper;
import com.agnie.useradmin.persistance.shared.proxy.RolePx;
import com.agnie.useradmin.persistance.shared.proxy.UserApplicationCtxRegistrationPx;
import com.google.gwt.user.client.ui.IsWidget;

public interface ContextUsersView extends IsWidget, CellTableView {

	public void initialize();

	void setDataProvider(AsyncDP<UserApplicationCtxRegistrationPx> dataProvider);

	void clearSelection();

	void refreshPage();

	RequestStatusWrapper getStatusFilter();

	void getUserCTXRoles(UserApplicationCtxRegistrationPx uar);

	void setRolePxList(List<RolePx> list);

	void setSelRolesList(List<RolePx> list);

	void resetCTXUserView();

	void setAdminRolePxList(List<RolePx> response);

	void setSelAdminRolesList(List<RolePx> list);

	void getUserCTXAdminRoles(UserApplicationCtxRegistrationPx uar);
}
