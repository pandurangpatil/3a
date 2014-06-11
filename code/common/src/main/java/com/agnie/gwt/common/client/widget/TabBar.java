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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is generic tab panel which is bare structure. It will take the shape as per css applied.
 * 
 * <p>
 * <div class="tabBox"> <div class="tabs"> <div class="inactive"> <a class="gwt-Anchor" href="javascript:;">Home</a>
 * </div> <div class="active"> <a class="gwt-Anchor" href="javascript:;">User Manager</a> </div> </div> </div>
 * </p>
 * 
 * You can create different set of styles to have different look and feel for your tab. e.g. you can create vertical tab
 * panel by creating such kind of css.
 * 
 * default css is "tabBox". for complete set refer tab.css inside same package.
 */
public class TabBar extends Composite implements HasBeforeSelectionHandlers<Integer>, HasSelectionHandlers<Integer> {

	private static TabBarResources	resource	= TabBarResources.INSTANCE;

	static {
		resource.css().ensureInjected();
	}

	public interface Tab extends HasAllKeyHandlers, HasClickHandlers {
	}

	private class ClickDelegatePanel extends Composite implements Tab {

		private FocusPanel	focusablePanel;
		private boolean		enabled	= true;

		ClickDelegatePanel(Widget child) {

			focusablePanel = new FocusPanel();

			focusablePanel.setWidget(child);
			initWidget(focusablePanel);
			focusablePanel.setStyleName(resource.css().inactive());
			sinkEvents(Event.ONCLICK | Event.ONKEYDOWN);
		}

		public HandlerRegistration addClickHandler(ClickHandler handler) {
			return addHandler(handler, ClickEvent.getType());
		}

		public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
			return addHandler(handler, KeyDownEvent.getType());
		}

		public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
			return addDomHandler(handler, KeyPressEvent.getType());
		}

		public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
			return addDomHandler(handler, KeyUpEvent.getType());
		}

		public SimplePanel getFocusablePanel() {
			return focusablePanel;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

	}

	private SimplePanel	container;
	private HTMLPanel	panel;
	private Widget		selectedTab;
	private String		mainStyleName;

	public TabBar() {
		this(resource.css().tabBox());
	}

	/**
	 * Creates an empty tab bar.
	 */
	public TabBar(String styleClassName) {
		container = new SimplePanel();
		panel = new HTMLPanel("");
		initWidget(container);
		sinkEvents(Event.ONCLICK);
		mainStyleName = styleClassName;
		container.addStyleName(mainStyleName);
		panel.addStyleName(resource.css().tabs());
		container.add(panel);
	}

	public void setMainStyleClassName(String styleClassName) {
		container.removeStyleName(mainStyleName);
		mainStyleName = styleClassName;
		container.addStyleName(mainStyleName);
	}

	public HandlerRegistration addBeforeSelectionHandler(BeforeSelectionHandler<Integer> handler) {
		return addHandler(handler, BeforeSelectionEvent.getType());
	}

	public HandlerRegistration addSelectionHandler(SelectionHandler<Integer> handler) {
		return addHandler(handler, SelectionEvent.getType());
	}

	/**
	 * Adds a new tab with the specified text.
	 * 
	 * @param text
	 *            the new tab's text
	 */
	public void addTab(String text) {
		Anchor item;
		item = new Anchor(text);

		item.setWordWrap(false);
		ClickDelegatePanel delWidget = new ClickDelegatePanel(item);

		panel.add(delWidget);
	}

	/**
	 * Gets the tab that is currently selected.
	 * 
	 * @return the selected tab
	 */
	public int getSelectedTab() {
		if (selectedTab == null) {
			return -1;
		}
		return panel.getWidgetIndex(selectedTab);
	}

	/**
	 * Gets the given tab.
	 * 
	 * This method is final because the Tab interface will expand. Therefore it is highly likely that subclasses which
	 * implemented this method would end up breaking.
	 * 
	 * @param index
	 *            the tab's index
	 * @return the tab wrapper
	 */
	public final Tab getTab(int index) {
		if (index >= getTabCount()) {
			return null;
		}
		ClickDelegatePanel p = (ClickDelegatePanel) panel.getWidget(index);
		return p;
	}

	/**
	 * Gets the number of tabs present.
	 * 
	 * @return the tab count
	 */
	public int getTabCount() {
		return panel.getWidgetCount();
	}

	/**
	 * Check if a tab is enabled or disabled. If disabled, the user cannot select the tab.
	 * 
	 * @param index
	 *            the index of the tab
	 * @return true if the tab is enabled, false if disabled
	 */
	public boolean isTabEnabled(int index) {
		assert (index >= 0) && (index < getTabCount()) : "Tab index out of bounds";
		ClickDelegatePanel delPanel = (ClickDelegatePanel) panel.getWidget(index);
		return delPanel.isEnabled();
	}

	/**
	 * Removes the tab at the specified index.
	 * 
	 * @param index
	 *            the index of the tab to be removed
	 */
	public void removeTab(int index) {
		checkTabIndex(index);

		Widget toRemove = panel.getWidget(index);
		if (toRemove == selectedTab) {
			selectedTab = null;
		}
		panel.remove(toRemove);
	}

	/**
	 * Programmatically selects the specified tab and fires events.
	 * 
	 * @param index
	 *            the index of the tab to be selected
	 * @return <code>true</code> if successful, <code>false</code> if the change is denied by the
	 *         {@link BeforeSelectionHandler}.
	 */
	public boolean selectTab(int index) {
		return selectTab(index, true);
	}

	/**
	 * Programmatically selects the specified tab. Use index -1 to specify that no tab should be selected.
	 * 
	 * @param index
	 *            the index of the tab to be selected
	 * @param fireEvents
	 *            true to fire events, false not to
	 * @return <code>true</code> if successful, <code>false</code> if the change is denied by the
	 *         {@link BeforeSelectionHandler}.
	 */
	public boolean selectTab(int index, boolean fireEvents) {
		checkTabIndex(index);

		if (fireEvents) {
			BeforeSelectionEvent<?> event = BeforeSelectionEvent.fire(this, index);
			if (event != null && event.isCanceled()) {
				return false;
			}
		}

		// Check for -1.
		setSelectionStyle(selectedTab, false);
		if (index == -1) {
			selectedTab = null;
			return true;
		}

		selectedTab = panel.getWidget(index);
		setSelectionStyle(selectedTab, true);
		if (fireEvents) {
			SelectionEvent.fire(this, index);
		}
		return true;
	}

	/**
	 * Enable or disable a tab. When disabled, users cannot select the tab.
	 * 
	 * @param index
	 *            the index of the tab to enable or disable
	 * @param enabled
	 *            true to enable, false to disable
	 */
	public void setTabEnabled(int index, boolean enabled) {
		assert (index >= 0) && (index < getTabCount()) : "Tab index out of bounds";

		// // Style the wrapper
		ClickDelegatePanel delPanel = (ClickDelegatePanel) panel.getWidget(index);
		delPanel.setEnabled(enabled);
		// Need to Decide on what style to apply in case of diabled tab.
		// setStyleName(delPanel.getElement(), "gwt-TabBarItem-disabled",
		// !enabled);
		// setStyleName(delPanel.getElement().getParentElement(),
		// "gwt-TabBarItem-wrapper-disabled", !enabled);
	}

	/**
	 * Sets a tab's text contents.
	 * 
	 * @param index
	 *            the index of the tab whose text is to be set
	 * @param text
	 *            the object's new text
	 */
	public void setTabText(int index, String text) {
		assert (index >= 0) && (index < getTabCount()) : "Tab index out of bounds";

		ClickDelegatePanel delPanel = (ClickDelegatePanel) panel.getWidget(index);
		SimplePanel focusablePanel = delPanel.getFocusablePanel();

		((Anchor) focusablePanel.getWidget()).setText(text);
	}

	private void checkTabIndex(int index) {
		if ((index < -1) || (index >= getTabCount())) {
			throw new IndexOutOfBoundsException();
		}
	}

	public String getTabText(int index) {
		if (index >= getTabCount()) {
			return null;
		}
		ClickDelegatePanel delPanel = (ClickDelegatePanel) panel.getWidget(index);
		SimplePanel focusablePanel = delPanel.getFocusablePanel();
		Widget widget = focusablePanel.getWidget();
		if (widget instanceof Anchor) {
			return ((Anchor) widget).getText();
		} else {
			// This will be a focusable panel holding a user-supplied widget.
			return focusablePanel.getElement().getParentElement().getInnerHTML();
		}
	}

	private void setSelectionStyle(Widget item, boolean selected) {
		if (item != null) {
			if (selected) {
				item.removeStyleName(resource.css().inactive());
				item.addStyleName(resource.css().active());
			} else {
				item.removeStyleName(resource.css().active());
				item.addStyleName(resource.css().inactive());
			}
		}
	}

	public void removeAll() {
		int count = getTabCount();
		for (int index = 0; index < count; index++) {
			removeTab(0);
		}
	}
}
