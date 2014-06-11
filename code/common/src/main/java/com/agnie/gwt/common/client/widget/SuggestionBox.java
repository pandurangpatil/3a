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

import java.util.List;

import com.agnie.gwt.common.client.I18;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * Simple suggestion box When end user press any key in search field ,suggestion list get displayed.
 * 
 */
public class SuggestionBox extends Composite {
	private static SuggestionBoxResources	resource	= SuggestionBoxResources.INSTANCE;

	static {
		resource.css().ensureInjected();
	}

	interface MyUiBinder extends UiBinder<Widget, SuggestionBox> {
	}

	private static MyUiBinder	uiBinder	= GWT.create(MyUiBinder.class);

	@UiField
	TextBox						search;
	@UiField
	CellList<String>			list;

	ListDataProvider<String>	dp			= new ListDataProvider<String>();
	TextCell					textCell	= new TextCell();

	protected HTMLPanel			container;

	public SuggestionBox() {
		container = (HTMLPanel) uiBinder.createAndBindUi(this);
		initWidget(container);
		list.setVisible(false);

		Label label = new Label(I18.messages.noData());
		list.setEmptyListWidget(label);

		dp.addDataDisplay(list);

		search.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				list.setVisible(true);
			}
		});

		list.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		// Add a selection model to handle user selection.
		final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
		list.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				String selected = selectionModel.getSelectedObject();
				if (selected != null) {
					Window.alert("You selected: " + selected);
				}
			}
		});

	}

	/**
	 * Sets data to suggestion list
	 * 
	 * @param list
	 * 
	 */
	public void setData(List<String> list) {
		dp.setList(list);
	}

	public static SuggestionBoxResources getResources() {
		return resource;
	}

	@UiFactory
	public CellList<String> getCellList() {
		list = new CellList<String>(textCell);
		return list;
	}

}
