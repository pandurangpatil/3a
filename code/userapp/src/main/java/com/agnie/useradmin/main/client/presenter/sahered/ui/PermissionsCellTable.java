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
import com.agnie.useradmin.persistance.shared.proxy.PermissionPx;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
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
public class PermissionsCellTable extends Composite {
	interface MyUiBinder extends UiBinder<Widget, PermissionsCellTable> {
	}

	private static MyUiBinder					uiBinder	= GWT.create(MyUiBinder.class);
	@UiField
	CellTable<PermissionPx>						table;

	@UiField
	SimplePager									pager;

	AsyncDP<PermissionPx>						dataProvider;
	private MultiSelectionModel<PermissionPx>	multiSelectionModel;
	@Inject
	MVPInjector									injector;

	public PermissionsCellTable() {
		initWidget(uiBinder.createAndBindUi(this));
		multiSelectionModel = new MultiSelectionModel<PermissionPx>(keyProvider);
		table.setPageSize(ApplicationConfig.getInstance().getPageSize());

		Column<PermissionPx, Boolean> select = new Column<PermissionPx, Boolean>(new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(PermissionPx object) {
				return multiSelectionModel.isSelected(object);
			}
		};
		table.addColumn(select, "");
		table.setColumnWidth(select, 10, Unit.PX);

		Column<PermissionPx, PermissionPx> permissions = new Column<PermissionPx, PermissionPx>(new PermissionsCell()) {
			@Override
			public PermissionPx getValue(PermissionPx object) {
				return object;
			}
		};
		table.addColumn(permissions, I18.messages.permissions());

		VerticalPanel panel = new VerticalPanel();
		Label label = new Label(I18.messages.no_data());
		panel.add(label);
		panel.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_CENTER);
		table.setEmptyTableWidget(label);
		table.setSelectionModel(multiSelectionModel,
				DefaultSelectionEventManager.<PermissionPx> createCustomManager(new MultiSelectEventTranslator<PermissionPx>(new MultiSelectEventTranslator.RowSelectCommand<PermissionPx>() {
					PermissionPx	selected;

					@Override
					public void execute() {
						getViewFactory().getListPermissionsView().setPermissionsViewData(selected);
						getViewFactory().getListPermissionsView().setPermSelToEdit(true);
						getViewFactory().getListPermissionsView().setPerPxToEdit(selected);
					}

					@Override
					public void setSelected(PermissionPx selected) {
						this.selected = selected;
					}
				})));
		pager.setRangeLimited(true);
		pager.setDisplay(table);
		AsyncHandler columnSortHandler = new AsyncHandler(table);
		table.addColumnSortHandler(columnSortHandler);
	}

	public MultiSelectionModel<PermissionPx> getSelectionModel() {
		return this.multiSelectionModel;
	}

	public void setDataProvider(AsyncDP<PermissionPx> dataProvider) {
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

	public CellTable<PermissionPx> getTable() {
		return this.table;
	}

	ViewFactory getViewFactory() {
		return injector.getDesktopViewFactory();
	}

	public static ProvidesKey<PermissionPx>	keyProvider	= new ProvidesKey<PermissionPx>() {

															@Override
															public Object getKey(PermissionPx item) {
																return item == null ? null : item.getId();
															}
														};

}
