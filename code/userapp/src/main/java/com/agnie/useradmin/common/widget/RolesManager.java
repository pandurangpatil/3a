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
package com.agnie.useradmin.common.widget;

import java.util.ArrayList;
import java.util.List;

import com.agnie.gwt.common.client.widget.ListBox.GetText;
import com.agnie.gwt.common.client.widget.SelectUnselect;
import com.agnie.useradmin.persistance.shared.proxy.RolePx;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * Widget to manage roles by select or un-select from available list.
 * 
 */
public class RolesManager extends Composite {
	private static RolesManagerResources	resource	= RolesManagerResources.INSTANCE;

	static {
		resource.css().ensureInjected();
	}

	interface MyUiBinder extends UiBinder<Widget, RolesManager> {
	}

	private static MyUiBinder		uiBinder			= GWT.create(MyUiBinder.class);

	@UiField
	HTMLPanel						selUnselPan;
	@UiField
	Button							updateBtn;
	@UiField
	Button							cancelBtn;

	List<HandlerRegistration>		handlerRegistration	= new ArrayList<HandlerRegistration>();

	protected HTMLPanel				container;
	private SelectUnselect<RolePx>	selUnsel			= new SelectUnselect<RolePx>(new GetText<RolePx>() {
															public String getText(RolePx object) {
																if (object != null) {
																	return object.getName();
																}
																return "";
															}
														}, new ArrayList<RolePx>(), new ArrayList<RolePx>());	;

	public RolesManager() {
		container = (HTMLPanel) uiBinder.createAndBindUi(this);
		initWidget(container);

		initSelUnsel();
	}

	public Button getUpdateBtn() {
		return this.updateBtn;
	}

	public Button getCancelBtn() {
		return this.cancelBtn;
	}

	public void addUpdateClickHandler(ClickHandler handler) {
		handlerRegistration.add(this.updateBtn.addClickHandler(handler));
	}

	public void clearUpdateHandlers() {
		for (HandlerRegistration hr : handlerRegistration) {
			hr.removeHandler();
		}
		this.handlerRegistration.clear();
	}

	public void addCancelClickHandler(ClickHandler handler) {
		this.cancelBtn.addClickHandler(handler);
	}

	private void initSelUnsel() {
		selUnsel.addStyleName("clearfix");
		selUnsel.setButtonStyle(resource.css().toggleButton());
		selUnselPan.add(selUnsel);
	}

	public SelectUnselect<RolePx> getSelUnsel() {
		return this.selUnsel;
	}

	public static RolesManagerResources getResources() {
		return resource;
	}

}
