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
package com.agnie.useradmin.main.client.presenter.sahered.ui;

import com.agnie.gwt.common.client.widget.SearchBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class Menu extends Composite {
	interface MyUiBinder extends UiBinder<Widget, Menu> {
	}

	private static MyUiBinder	uiBinder	= GWT.create(MyUiBinder.class);

	@UiField
	HTMLPanel					tabbarPan;
	@UiField
	HTMLPanel					searchPan;
	@UiField
	DivElement					domain;

	public SearchBox			searchBox	= new SearchBox();

	public Menu() {
		initWidget(uiBinder.createAndBindUi(this));
		searchPan.add(searchBox);
	}

	public SearchBox getSearchBox() {
		return this.searchBox;
	}

	public HTMLPanel getTabbarPan() {
		return tabbarPan;
	}

	public void setDomain(String domain) {
		if (domain != null) {
			this.domain.setInnerText(domain);
		}
	}
}
