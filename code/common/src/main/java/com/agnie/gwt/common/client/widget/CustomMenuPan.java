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

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * CustomMenuPan widget.
 * 
 */
public class CustomMenuPan extends Composite implements ContextMenuHandler {
	private static CustomMenuPanResources	resource	= CustomMenuPanResources.INSTANCE;
	private MenuPan							contextMenu	= new MenuPan();
	static {
		resource.css().ensureInjected();
	}

	interface MyUiBinder extends UiBinder<Widget, CustomMenuPan> {
	}

	private static MyUiBinder	uiBinder	= GWT.create(MyUiBinder.class);

	@UiField
	SpanElement					title;

	public HTMLPanel			container;

	public CustomMenuPan() {
		this(resource.css().customMenuPan());
	}

	MenuBar			popupMenuBar1To3	= new MenuBar(true);
	MenuBar			popupMenuBar4To5	= new MenuBar(true);
	List<Account>	accList				= new ArrayList<Account>();

	public CustomMenuPan(String styleClassName) {
		container = (HTMLPanel) uiBinder.createAndBindUi(this);
		container.addStyleName(styleClassName);
		initWidget(container);
		this.setCustomMenuTitle("Right click Test !");
		/* ContextMenu related code starts here */
		this.contextMenu.hide();
		this.contextMenu.setAutoHideEnabled(true);
		addDomHandler(this, ContextMenuEvent.getType());
		
		createDummyAccounts();

		Command c1 = new Command() {

			@Override
			public void execute() {
				Window.alert("Command 1 executed.");
				container.add(accList.get(0));
			}
		};
		Command c2 = new Command() {

			@Override
			public void execute() {
				Window.alert("Command 2 executed.");
				container.add(accList.get(1));
			}
		};
		Command c3 = new Command() {

			@Override
			public void execute() {
				Window.alert("Command 3 executed.");
				container.add(accList.get(2));
			}
		};
		Command c4 = new Command() {

			@Override
			public void execute() {
				Window.alert("Command 4 executed.");
				container.add(accList.get(3));
			}
		};
		Command c5 = new Command() {

			@Override
			public void execute() {
				Window.alert("Command 5 executed.");
				container.add(accList.get(4));
			}
		};
		MenuItem fItem = new MenuItem("Command1", true, c1);
		MenuItem sItem = new MenuItem("Command2 ", true, c2);
		MenuItem tItem = new MenuItem("Command3 ", true, c3);
		MenuItem frItem = new MenuItem("Command4 ", true, c4);
		MenuItem fiItem = new MenuItem("Command5 ", true, c5);

		popupMenuBar1To3.addItem(fItem);
		popupMenuBar1To3.addItem(sItem);
		popupMenuBar1To3.addItem(tItem);

		popupMenuBar4To5.addItem(frItem);
		popupMenuBar4To5.addItem(fiItem);

		popupMenuBar1To3.setVisible(true);
		popupMenuBar4To5.setVisible(true);
		contextMenu.container.add(popupMenuBar1To3);
		contextMenu.addMenuSeparator();
		contextMenu.container.add(popupMenuBar4To5);
	}

	public void onContextMenu(ContextMenuEvent event) {
		// stop the browser from opening the context menu
		event.preventDefault();
		event.stopPropagation();

		this.contextMenu.setPopupPosition(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
		this.contextMenu.show();
	}

	public void setCustomMenuTitle(String title) {
		this.title.setInnerText(title);
	}

	private void createDummyAccounts() {
		for(int index=0;index<5;index++){
		UserAccount ua = new UserAccount();
		ua.setFirstName("firstName"+index);
		ua.setLastName("lastName"+index);
		accList.get(index).setUserAcc(ua);
		}
	}
}
