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

import com.agnie.gwt.common.client.widget.MenuPan;
import com.agnie.useradmin.persistance.client.helper.UserAdminClientURLInfo;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLInfo;
import com.agnie.useradmin.persistance.shared.proxy.ApplicationPx;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * ApplicationWidget is widget to show UserRegistered applications.
 * 
 * 
 */
public class ApplicationWidget extends Composite implements ContextMenuHandler {
	private static ApplicationWidgetResources	resource	= ApplicationWidgetResources.INSTANCE;
	private MenuPan								contextMenu	= new MenuPan();
	static {
		resource.css().ensureInjected();
	}

	interface MyUiBinder extends UiBinder<Widget, ApplicationWidget> {
	}

	private static MyUiBinder	uiBinder			= GWT.create(MyUiBinder.class);

	@UiField
	DivElement					titleDiv;
	@UiField
	Image						img;

	MenuBar						popupMenuBar1To3	= new MenuBar(true);
	MenuItem					openItem;
	MenuItem					manageItem;
	ApplicationPx				appPx;
	UserAdminURLInfo			params				= new UserAdminClientURLInfo();

	UserAdminURLGenerator		uaurlGenerator		= new UserAdminURLGenerator();

	public ApplicationWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void onContextMenu(ContextMenuEvent event) {
		// stop the browser from opening the context menu
		event.preventDefault();
		event.stopPropagation();

		this.contextMenu.setPopupPosition(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
		this.contextMenu.show();
	}

	public void hideMenuPan() {
		this.contextMenu.hide();
	}

	/**
	 * ToSetAppWidget commands on rightClickMenu
	 * 
	 * @param domainI
	 * @param domUrlI
	 * @param isAdmin
	 */
	private void setAppWidgetCommands(final String domainI, final String domUrlI, boolean isAdmin) {
		/* ContextMenu related code starts here */
		this.contextMenu.hide();
		this.contextMenu.setAutoHideEnabled(true);
		addDomHandler(this, ContextMenuEvent.getType());

		Command open = new Command() {

			@Override
			public void execute() {
				hideMenuPan();
				if (domUrlI != null) {

					Window.open(uaurlGenerator.getApplicationUrl(domUrlI, params), "_blank", null);
				} else {
					Window.alert("Sorry opening application is failed");
				}
			}
		};
		openItem = new MenuItem("Open", true, open);
		popupMenuBar1To3.addItem(openItem);

		Command manage = new Command() {

			@Override
			public void execute() {
				hideMenuPan();
				GWT.log("In appWidget manage");
				if (domainI != null) {
					Window.open(uaurlGenerator.getAppManageUrl(domainI, params), "_blank", null);
				}
			}
		};
		manageItem = new MenuItem("Manage ", true, manage);
		if (isAdmin) {
			popupMenuBar1To3.addItem(manageItem);
		}

		popupMenuBar1To3.setVisible(true);
		contextMenu.container.add(popupMenuBar1To3);

	}

	/**
	 * To create ApplicationWidget from given(server) ApplicationPx
	 * 
	 * @param appPx
	 */

	public void setAppWidgetData(ApplicationPx appPx) {
		this.appPx = appPx;
		if (appPx.getIconURL() != null && !(appPx.getIconURL().isEmpty())) {
			this.img.addErrorHandler(new ErrorHandler() {

				@Override
				public void onError(ErrorEvent event) {
					ApplicationWidget.this.setImageUrl(GWT.getHostPageBaseURL() + "images/application-icon-big.png");
				}
			});
			this.setImageUrl(appPx.getIconURL());
		} else {
			this.setImageUrl(GWT.getHostPageBaseURL() + "images/application-icon-big.png");
		}
		this.titleDiv.setTitle(appPx.getDomain());
		this.setAppWidgetTitle(validateStringLength(appPx.getDomain()));

		this.setAppWidgetCommands(appPx.getDomain(), appPx.getURL(), appPx.isAdmin());
	}

	private String validateStringLength(String str) {
		String strV = "";
		if (str.length() > 20) {
			String sub = str.substring(0, 16);
			strV = sub + "....";
		} else {
			strV = str;
		}
		return strV;
	}

	/**
	 * To set 'Application Widget Image' Url,Image must be 150px 100px
	 * 
	 * @param url
	 */
	public void setImageUrl(String url) {
		this.img.setUrl(url);
	}

	/**
	 * To set ApplicationWidget title( url)
	 * 
	 * @param titleUrl
	 */
	public void setAppWidgetTitle(String titleUrl) {
		this.titleDiv.setInnerText(titleUrl);
	}

	@UiFactory
	public static ApplicationWidgetResources getResources() {
		return resource;
	}

}
