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

import com.agnie.gwt.common.client.mvp.MainView;
import com.agnie.gwt.common.client.rpc.AsyncDP;
import com.agnie.useradmin.common.client.base.CellTableView;
import com.agnie.useradmin.persistance.shared.proxy.PermissionPx;
import com.google.gwt.user.client.ui.IsWidget;

public interface PermissionsView extends IsWidget, CellTableView, MainView {

	void initialize();

	void reset();

	void refreshPage();

	void setDataProvider(AsyncDP<PermissionPx> permissionDP);

	void setPermissionsViewData(PermissionPx permPx);

	void setPermSelToEdit(boolean set);

	void setPerPxToEdit(PermissionPx perPxToEdit);
}
