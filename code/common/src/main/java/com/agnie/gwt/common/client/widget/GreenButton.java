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
package com.agnie.gwt.common.client.widget;

import com.agnie.gwt.common.client.ui.FocusWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Green button is custom DIV tag button. So it does not support all the functionalities of normal button object.
 * <ul>
 * <li>One can set the tab index for this button</li>
 * <li>One cannot set this button in disabled state.</li>
 * </ul>
 * 
 * 
 */
public class GreenButton extends FocusWidget {

	private static GreenButtonResources	resource	= GreenButtonResources.INSTANCE;

	static {
		resource.css().ensureInjected();
	}

	interface MyUiBinder extends UiBinder<Widget, GreenButton> {
	}

	private static MyUiBinder	uiBinder	= GWT.create(MyUiBinder.class);

	@UiField
	protected Label				label;

	@UiField
	protected HTMLPanel			panel;

	public GreenButton(String label) {
		initWidget(uiBinder.createAndBindUi(this));
		setContainer(panel);
		panel.setStyleName(resource.css().greenButton());
		this.label.setText(label);
		this.label.setStyleName(resource.css().greenButtonLabel());
	}

	@UiFactory
	public static GreenButtonResources getResources() {
		return resource;
	}

	@UiFactory
	public static String getBasePath() {
		return GWT.getModuleBaseURL();
	}

	public void setText(String text) {
		label.setText(text);
	}

}
