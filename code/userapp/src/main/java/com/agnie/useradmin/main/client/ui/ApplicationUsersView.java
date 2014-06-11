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

import com.agnie.gwt.common.client.rpc.AsyncDP;
import com.agnie.useradmin.common.client.base.CellTableView;
import com.agnie.useradmin.common.client.helper.RequestStatusWrapper;
import com.agnie.useradmin.persistance.shared.proxy.RolePx;
import com.agnie.useradmin.persistance.shared.proxy.UserApplicationRegistrationPx;
import com.google.gwt.user.client.ui.IsWidget;

public interface ApplicationUsersView extends IsWidget, CellTableView {
	void initialize();

	void setDataProvider(AsyncDP<UserApplicationRegistrationPx> dataProvider);

	void clearSelection();

	void refreshPage();

	RequestStatusWrapper getStatusFilter();

	void getUserAppRoles(UserApplicationRegistrationPx uar);

	void setRolePxList(List<RolePx> list);

	void setAdminRolePxList(List<RolePx> list);

	void setSelRolesList(List<RolePx> list);

	void resetAppUserView();

	void setSelAdminRolesList(List<RolePx> response);

	void getUserAppAdminRoles(UserApplicationRegistrationPx uar);
}
