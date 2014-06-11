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
import com.agnie.useradmin.persistance.shared.proxy.ApplicationPx;
import com.agnie.useradmin.persistance.shared.proxy.RolePx;
import com.google.gwt.user.client.ui.IsWidget;

public interface DomainParentView extends IsWidget, MainView {

	void init(ApplicationPx response);

	void setData(ApplicationPx appPx);

	void setTotalAppRoles(List<RolePx> appRoles);

	void setSelAppRolesList(List<RolePx> list);

	void setTotalContextRoles(List<RolePx> ctxRoles);

	void setSelCTXRolesList(List<RolePx> list);

	void setdefAppRolesList(List<RolePx> list);

	void setdefContextRolesList(List<RolePx> list);

	void hideReadView();

	void swithToReadView();
}
