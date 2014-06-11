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

import java.util.ArrayList;
import java.util.List;

import com.agnie.useradmin.persistance.shared.proxy.ApplicationPx;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class DeskLandingView extends Composite implements LandingView {

	interface MyUiBinder extends UiBinder<Widget, DeskLandingView> {
	}

	private static MyUiBinder		uiBinder	= GWT.create(MyUiBinder.class);

	HTMLPanel						container;

	private Grid					grid;
	private List<ApplicationWidget>	appList;

	public DeskLandingView() {
		container = (HTMLPanel) uiBinder.createAndBindUi(this);
		initWidget(container);
	}

	/**
	 * To set ApplicationWidget List from given(server) ApplicationPx List
	 * 
	 * @param appPx
	 */
	@Override
	public void setAppList(List<ApplicationPx> appPx) {
		appList = new ArrayList<ApplicationWidget>();
		for (int i = 0; i < appPx.size(); i++) {
			ApplicationWidget aw = new ApplicationWidget();
			aw.setAppWidgetData(appPx.get(i));
			appList.add(aw);
		}
		addAppsToView(appList);
	}

	private void addAppsToView(List<ApplicationWidget> appList) {

		int count = 0;
		int appListSize = appList.size();
		int noOfRow = (appListSize / 4) + 1;
		grid = new Grid(noOfRow, 4);
		for (int row = 0; row < noOfRow; ++row) {
			for (int col = 0; col < 4; ++col) {
				if (count >= appListSize)
					break;
				grid.setWidget(row, col, appList.get(count));
				count++;
			}
		}

		this.container.add(grid);
	}

	/**
	 * To set LandingView variables at initial value
	 */
	@Override
	public void clearLandingView() {
		this.container.clear();
		if (grid != null) {
			grid.clear();
		}

		if (appList != null) {
			appList.clear();
		}
	}
}
