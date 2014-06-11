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
package com.agnie.useradmin.landing.client.ui;

import java.util.List;

import com.agnie.useradmin.persistance.shared.proxy.ApplicationPx;
import com.google.gwt.user.client.ui.IsWidget;

public interface LandingView extends IsWidget {

	void setAppList(List<ApplicationPx> appPx);

	void clearLandingView();
}
