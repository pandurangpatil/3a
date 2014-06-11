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
package com.agnie.useradmin.landing.client.presenter.shared.ui;

import com.agnie.gwt.common.client.widget.TabBar;
import com.agnie.useradmin.landing.client.mvp.ClientFactory;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public abstract class BasePageChangeHandler {
	protected ClientFactory	clientFactory;
	protected TabBar		tabBar;
	private boolean			init	= false;

	/**
	 * @param clientFactory2
	 */
	public BasePageChangeHandler(ClientFactory clientFactory, String menuStyle) {
		this.clientFactory = clientFactory;
		tabBar = new TabBar(menuStyle);
	}

	public TabBar getTabBar() {
		if (!init) {
			initialize();
			init = true;
		}
		return tabBar;
	}

	protected abstract void initialize();

	protected void initialize(TabInfo[] tabs, SpecificTabClickHandler clickHandler) {
		for (int index = 0; index < tabs.length; index++) {
			tabBar.addTab(tabs[index].getLabel());
			tabs[index].setIndex(index);
			tabBar.getTab(index).addClickHandler(new TabClickHandler(index, clickHandler));
		}
	}

	public void selectTab(int index) {
		tabBar.selectTab(index);
	}

	private class TabClickHandler implements ClickHandler {

		private int						index;
		private SpecificTabClickHandler	handler;

		/**
		 * @param index
		 * @param handler
		 */
		TabClickHandler(int index, SpecificTabClickHandler handler) {
			this.index = index;
			this.handler = handler;
		}

		public void onClick(ClickEvent event) {
			handler.onClick(event, index);
		}
	}

	/**
	 * Every Menu wrapper will implement this interface to handle the specific tab clicks on that menu.
	 * 
	 */
	protected static interface SpecificTabClickHandler {
		void onClick(ClickEvent clickEvent, int index);
	}

	protected static interface TabInfo {
		/**
		 * @return the label
		 */
		public String getLabel();

		/**
		 * @return the permission
		 */
		public String getPermission();

		/**
		 * @return the index
		 */
		public int getIndex();

		/**
		 * @param index
		 *            the index to set
		 */
		public void setIndex(int index);
	}
}
