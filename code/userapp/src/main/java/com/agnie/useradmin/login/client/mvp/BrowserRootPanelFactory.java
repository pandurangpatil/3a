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
package com.agnie.useradmin.login.client.mvp;

import com.google.gwt.user.client.ui.RootPanel;

public class BrowserRootPanelFactory implements RootPanelFactory {

	public RootPanel getHeaderPanel() {
		return RootPanel.get("header");
	}

	public RootPanel getFooterPanel() {
		return RootPanel.get("footer");
	}

	public RootPanel getContentPanel() {
		return RootPanel.get("content");
	}

	@Override
	public RootPanel getMainMenuPanel() {
		return RootPanel.get("main");
	}

	@Override
	public RootPanel getPageLoader() {
		return RootPanel.get("pageLoader");
	}

}
