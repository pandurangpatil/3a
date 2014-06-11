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

import com.agnie.useradmin.contextmgr.client.presenter.ContextSettingsPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeskContextSettingsView extends Composite implements ContextSettingsView {

	interface MyUiBinder extends UiBinder<Widget, DeskContextSettingsView> {
	}

	private static MyUiBinder	uiBinder	= GWT.create(MyUiBinder.class);

	HTMLPanel					container;

	@UiField
	Label						context;

	// @UiField
	// TextBox newowner;
	//
	// @UiField
	// Button transferOwner;

	@Inject
	ContextSettingsPresenter	presenter;

	public DeskContextSettingsView() {
		container = (HTMLPanel) uiBinder.createAndBindUi(this);
		initWidget(container);
	}

	public void init(String ctx) {
		context.setText(ctx);
	}

	// @UiHandler("transferOwner")
	// public void transferOwner(ClickEvent event) {
	// if (Window.confirm(I18.messages.confirm_transfer())) {
	// presenter.transferOwnerShip(newowner.getText());
	// }
	// }

}
