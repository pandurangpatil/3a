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

import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.useradmin.main.client.I18;
import com.agnie.useradmin.main.client.mvp.MainAppController;
import com.agnie.useradmin.main.client.mvp.PlaceToken;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ListMenu extends BasePageChangeHandler {

	@Inject
	MainAppController			appController;

	private static final String	MAIN_MENU_STYLE	= "menubar clearfix";

	public ListMenu() {
		super(MAIN_MENU_STYLE);
	}

	public static enum Tab implements TabInfo {
		USERS(I18.messages.users(), Permissions.APPLICATION_USER_MANAGER), ROLE(I18.messages.roles(), Permissions.ROLE), PERMISSION(I18.messages.permissions(), Permissions.PERMISSION), DOMAIN_SETTINGS(
				I18.messages.domainSettings(), Permissions.DOMAIN_SETTINGS), CONTEXT(I18.messages.contexts(), Permissions.MANAGE_CONTEXT);
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

				case USERS:
					appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.APP_USERS));
					break;
				case ROLE:
					appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.ROLES));
					break;
				case PERMISSION:
					appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.PERMISSION));
					break;
				case DOMAIN_SETTINGS:
					appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.DOMAIN_SETTINGS));
					break;
				case CONTEXT:
					appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.CTXS));
					break;
				default:

					break;
				}
			}
		});
	}

}
