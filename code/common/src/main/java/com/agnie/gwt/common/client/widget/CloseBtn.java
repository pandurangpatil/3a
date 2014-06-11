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
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * CloseBtn widget.
 * 
 */
public class CloseBtn extends Composite {
	private static CloseBtnResources	resource	= CloseBtnResources.INSTANCE;

	static {
		resource.css().ensureInjected();
	}

	interface MyUiBinder extends UiBinder<Widget, CloseBtn> {
	}

	private static MyUiBinder	uiBinder	= GWT.create(MyUiBinder.class);

	@UiField
	Image						closeBtn;

	protected HTMLPanel			container;

	public CloseBtn() {
		this(resource.css().closeWidget());
	}

	public CloseBtn(String styleClassName) {
		container = (HTMLPanel) uiBinder.createAndBindUi(this);
		container.addStyleName(styleClassName);
		initWidget(container);

		closeBtn.setUrl(GWT.getModuleBaseURL() + "images/transparent.png");
	}

	/**
	 * clickHandler for close button.
	 * 
	 * @param handler
	 *            click handler for close button.
	 */
	public void addClickHandler(ClickHandler handler) {
		this.closeBtn.addClickHandler(handler);
	}

	public static CloseBtnResources getResources() {
		return resource;
	}

}
