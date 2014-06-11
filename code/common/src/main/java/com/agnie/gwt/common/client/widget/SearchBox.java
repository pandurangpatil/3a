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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * Search Box
 * 
 */
public class SearchBox extends Composite {
	private static SearchBoxResources	resource	= SearchBoxResources.INSTANCE;

	static {
		resource.css().ensureInjected();
	}

	interface MyUiBinder extends UiBinder<Widget, SearchBox> {
	}

	private static MyUiBinder	uiBinder	= GWT.create(MyUiBinder.class);

	@UiField
	LabelTextBox				search;
	@UiField
	HTMLPanel					inputWidgetContainer;

	Widget						widget;
	protected HTMLPanel			container;
	public Image				img			= new Image();
	private List<HandlerRegistration>	handlers	= new ArrayList<HandlerRegistration>();

	public SearchBox() {
		container = (HTMLPanel) uiBinder.createAndBindUi(this);
		initWidget(container);
		img.setResource(resource.searchImg());
		addInputWidget(img);
	}

	public void addSearchImgClickHandler(ClickHandler handler) {
		this.handlers.add(this.img.addClickHandler(handler));
	}
	
	public void clearSearchImgClkHandlers() {
		for (HandlerRegistration handler : this.handlers) {
			handler.removeHandler();
		}
		this.handlers.clear();
	}

	public void setLabel(String label) {
		search.setLabel(label);
	}

	@UiChild
	public void addInputWidget(Widget widget) {
		if (widget != null) {
			inputWidgetContainer.add(widget);
		}
	}

	public void setHeight(String height) {
		container.setHeight(height);
	}

	public void setSize(String width, String height) {
		container.setSize(width, height);
	}

	public String getValue() {
		return search.getValue();
	}

	public static SearchBoxResources getResources() {
		return resource;
	}

}
