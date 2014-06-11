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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FormPanel extends Composite {
	private static FormPanelResources	resource	= FormPanelResources.INSTANCE;

	static {
		resource.css().ensureInjected();
	}

	interface MyUiBinder extends UiBinder<Widget, FormPanel> {
	}

	private static MyUiBinder	uiBinder	= GWT.create(MyUiBinder.class);

	public FormPanel() {
		this(resource.css().formPanel());
	}

	@UiField
	Label				label;
	@UiField
	HTMLPanel			formContainer;

	protected HTMLPanel	container;

	public FormPanel(String styleClassName) {
		container = (HTMLPanel) uiBinder.createAndBindUi(this);
		container.addStyleName(styleClassName);
		initWidget(container);
	}

	public void setFormPanWidth(String width) {
		this.container.setWidth(width);
	}

	public void setFormPanHeight(String height) {
		this.container.setHeight(height);
	}

	public void setFormContWidth(String width) {
		this.formContainer.setWidth(width);
	}

	public void setFormContHeight(String height) {
		this.formContainer.setHeight(height);
	}

	public void setLabel(String label) {
		this.label.setText(label);
	}

	@UiChild
	public void addFormContainer(Widget formContainer) {
		if (formContainer != null) {
			this.formContainer.add(formContainer);
		}
	}

	@UiFactory
	public static FormPanelResources getResources() {
		return resource;
	}
}
