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

import com.agnie.gwt.common.client.rpc.AsyncDP;
import com.agnie.gwt.common.client.rpc.MultiSelectEventTranslator;
import com.agnie.useradmin.common.client.ApplicationConfig;
import com.agnie.useradmin.common.client.base.Sort;
import com.agnie.useradmin.main.client.I18;
import com.agnie.useradmin.main.client.injector.MVPInjector;
import com.agnie.useradmin.main.client.ui.ViewFactory;
import com.agnie.useradmin.persistance.client.enums.SortOrder;
import com.agnie.useradmin.persistance.shared.proxy.ContextPx;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ManageContextsCellTable extends Composite {
	interface MyUiBinder extends UiBinder<Widget, ManageContextsCellTable> {
	}

	private static MyUiBinder				uiBinder	= GWT.create(MyUiBinder.class);
	@UiField
	CellTable<ContextPx>					table;

	@UiField
	SimplePager								pager;

	AsyncDP<ContextPx>						dataProvider;
	private MultiSelectionModel<ContextPx>	multiSelectionModel;
	@Inject
	MVPInjector								injector;

	public ManageContextsCellTable() {
		initWidget(uiBinder.createAndBindUi(this));
		multiSelectionModel = new MultiSelectionModel<ContextPx>(keyProvider);
		table.setPageSize(ApplicationConfig.getInstance().getPageSize());

		Column<ContextPx, String> name = new Column<ContextPx, String>(new TextCell()) {
			@Override
			public String getValue(ContextPx object) {
				return object.getName();
			}
		};
		table.addColumn(name, I18.messages.name());

		Column<ContextPx, String> description = new Column<ContextPx, String>(new TextCell()) {
			@Override
			public String getValue(ContextPx object) {
				return object.getDescription();
			}
		};
		table.addColumn(description, I18.messages.description());

		VerticalPanel panel = new VerticalPanel();
		Label label = new Label(I18.messages.no_data());
		panel.add(label);
		panel.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_CENTER);
		table.setEmptyTableWidget(label);
		table.setSelectionModel(multiSelectionModel,
				DefaultSelectionEventManager.<ContextPx> createCustomManager(new MultiSelectEventTranslator<ContextPx>(new MultiSelectEventTranslator.RowSelectCommand<ContextPx>() {
					ContextPx	selected;

					@Override
					public void execute() {
						getViewFactory().getManageContextsView().setManContextViewData(selected);
						getViewFactory().getManageContextsView().enableSaveButton(false);
						getViewFactory().getManageContextsView().deleteBtnVisible(true);
						getViewFactory().getManageContextsView().setContextToDelete(selected);
					}

					@Override
					public void setSelected(ContextPx selected) {
						this.selected = selected;
					}
				})));

		pager.setRangeLimited(true);
		pager.setDisplay(table);
		AsyncHandler columnSortHandler = new AsyncHandler(table);
		table.addColumnSortHandler(columnSortHandler);
	}

	public void setDataProvider(AsyncDP<ContextPx> dataProvider) {
		if (this.dataProvider != null) {
			this.dataProvider.removeDataDisplay(table);
		}
		this.dataProvider = dataProvider;
		this.dataProvider.addDataDisplay(table);
	}

	public void initialize() {
		refreshTable();
	}

	private void refreshTable() {
		table.setRowCount(0, false);
		Range resetrange = new Range(0, table.getPageSize());
		table.setVisibleRangeAndClearData(resetrange, true);
	}

	public void refresh() {
		table.setVisibleRangeAndClearData(table.getVisibleRange(), true);
	}

	public void clearSelection() {
		multiSelectionModel.clear();
	}

	public Sort getSortColumn() {
		ColumnSortList sortList = table.getColumnSortList();
		if (sortList != null && sortList.size() > 0) {
			ColumnSortInfo col = sortList.get(0);

			return new Sort(col.getColumn().getDataStoreName(), (col.isAscending() ? SortOrder.ASC : SortOrder.DESC));
		}
		return null;
	}

	public MultiSelectionModel<ContextPx> getSelectionModel() {
		return this.multiSelectionModel;
	}

	public CellTable<ContextPx> getTable() {
		return this.table;
	}

	ViewFactory getViewFactory() {
		return injector.getDesktopViewFactory();
	}

	public static ProvidesKey<ContextPx>	keyProvider	= new ProvidesKey<ContextPx>() {

															@Override
															public Object getKey(ContextPx item) {
																return item == null ? null : item.getId();
															}
														};
}
