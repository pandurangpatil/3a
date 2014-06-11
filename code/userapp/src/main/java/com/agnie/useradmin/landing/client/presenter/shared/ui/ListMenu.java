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
package com.agnie.useradmin.landing.client.presenter.shared.ui;

import com.agnie.gwt.common.client.widget.SearchBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class ListMenu extends Composite {
	interface MyUiBinder extends UiBinder<Widget, ListMenu> {
	}

	private static MyUiBinder	uiBinder	= GWT.create(MyUiBinder.class);

	@UiField
	HTMLPanel					tabbarPan;
	@UiField
	HTMLPanel					searchPan;

	public SearchBox			searchBox	= new SearchBox();

	public ListMenu() {
		initWidget(uiBinder.createAndBindUi(this));
		searchPan.add(searchBox);
		searchBox.addSearchImgClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("do you searching for " + searchBox.getValue());
			}
		});
	}

	public HTMLPanel getTabbarPan() {
		return tabbarPan;
	}
}
