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
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * PageTitle with image.
 * 
 */
public class PageTitle extends Composite {
	private static PageTitleResources	resource	= PageTitleResources.INSTANCE;

	static {
		resource.css().ensureInjected();
	}

	interface MyUiBinder extends UiBinder<Widget, PageTitle> {
	}

	private static MyUiBinder	uiBinder	= GWT.create(MyUiBinder.class);

	@UiField
	protected SpanElement		pageTitle;
	@UiField
	HTMLPanel					titleImg;

	protected HTMLPanel			container;

	public PageTitle() {
		container = (HTMLPanel) uiBinder.createAndBindUi(this);
		initWidget(container);
	}

	public void setPageTitle(String title) {
		pageTitle.setInnerText(title);
	}

	@UiChild
	public void addTitleImage(Widget widget) {
		titleImg.add(widget);
	}

	public void setHeight(String height) {
		container.setHeight(height);
	}

	public void setSize(String width, String height) {
		container.setSize(width, height);
	}

	public static PageTitleResources getResources() {
		return resource;
	}

}
