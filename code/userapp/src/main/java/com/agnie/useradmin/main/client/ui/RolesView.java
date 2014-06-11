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

import com.agnie.gwt.common.client.mvp.MainView;
import com.agnie.gwt.common.client.rpc.AsyncDP;
import com.agnie.useradmin.common.client.base.CellTableView;
import com.agnie.useradmin.persistance.shared.proxy.PermissionPx;
import com.agnie.useradmin.persistance.shared.proxy.RolePx;
import com.google.gwt.user.client.ui.IsWidget;

public interface RolesView extends IsWidget, CellTableView, MainView {
	void initialize();

	void reset();

	void setRolesViewData(RolePx rolePx);

	void setRoleSelToEdit(boolean b);

	void setRolePxToEdit(RolePx selected);

	void refreshPage();

	void setDataProvider(AsyncDP<RolePx> dataProvider);

	void initSelUnsel();

	void setListPermissionPx(List<PermissionPx> permPxList);

}
