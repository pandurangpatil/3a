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
package com.agnie.useradmin.contextmgr.client.presenter.shared.ui;

import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.useradmin.contextmgr.client.I18;
import com.agnie.useradmin.contextmgr.client.mvp.CtxMgrAppController;
import com.agnie.useradmin.contextmgr.client.mvp.PlaceToken;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ListMenu extends BasePageChangeHandler {
	@Inject
	CtxMgrAppController			appController;

	private static final String	MAIN_MENU_STYLE	= "menubar clearfix";

	public ListMenu() {
		super(MAIN_MENU_STYLE);
	}

	public static enum Tab implements TabInfo {
		USERS(I18.messages.users(), Permissions.CONTEXT_USER_MANAGER), CONTEXT_SETTINGS(I18.messages.context_settings(), Permissions.CONTEXT_SETTINGS);
		private String	label;
		private String	permission;
		private int		index	= -1;

		/**
		 * @param label
		 * @param permission
		 */
		private Tab(String label, String permission) {
			this.label = label;
			this.permission = permission;
		}

		/**
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}

		/**
		 * @return the permission
		 */
		public String getPermission() {
			return permission;
		}

		/**
		 * @return the index
		 */
		public int getIndex() {
			return index;
		}

		/**
		 * @param index
		 *            the index to set
		 */
		public void setIndex(int index) {
			this.index = index;
		}

		public static Tab getTabByIndex(int index) {
			for (Tab tab : values()) {
				if (tab.index == index)
					return tab;
			}
			return null;
		}

	}

	@Override
	protected void initialize() {
		Tab[] tabs = Tab.values();
		initialize(tabs, new SpecificTabClickHandler() {

			@Override
			public void onClick(ClickEvent clickEvent, int index) {
				GWT.log("ListMenu onClick Switch getTabByIndex==" + ListMenu.Tab.getTabByIndex(index));
				switch (ListMenu.Tab.getTabByIndex(index)) {

				case USERS: {
					appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.USERS));
					break;
				}
				case CONTEXT_SETTINGS: {
					appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.CTX_SETTINGS));
					break;
				}
				default:

					break;
				}
			}
		});
	}

}
