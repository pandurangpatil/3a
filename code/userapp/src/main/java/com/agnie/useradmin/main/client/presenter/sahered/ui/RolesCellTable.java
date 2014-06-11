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
import com.agnie.useradmin.main.client.presenter.RolesPresenter;
import com.agnie.useradmin.main.client.ui.ViewFactory;
import com.agnie.useradmin.persistance.client.enums.SortOrder;
import com.agnie.useradmin.persistance.shared.proxy.RolePx;
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
public class RolesCellTable extends Composite {
	interface MyUiBinder extends UiBinder<Widget, RolesCellTable> {
	}

	private static MyUiBinder			uiBinder	= GWT.create(MyUiBinder.class);
	@UiField
	CellTable<RolePx>					table;

	@UiField
	SimplePager							pager;

	AsyncDP<RolePx>						dataProvider;
	private MultiSelectionModel<RolePx>	multiSelectionModel;
	@Inject
	MVPInjector							injector;
	@Inject
	RolesPresenter						presenter;

	public RolesCellTable() {
		initWidget(uiBinder.createAndBindUi(this));

		multiSelectionModel = new MultiSelectionModel<RolePx>(keyProvider);
		table.setPageSize(ApplicationConfig.getInstance().getPageSize());

		Column<RolePx, Boolean> select = new Column<RolePx, Boolean>(new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(RolePx object) {
				return multiSelectionModel.isSelected(object);
			}
		};
		table.addColumn(select, "");
		table.setColumnWidth(select, 10, Unit.PX);

		Column<RolePx, RolePx> roles = new Column<RolePx, RolePx>(new RolesCell()) {
			@Override
			public RolePx getValue(RolePx object) {
				return object;
			}
		};
		table.addColumn(roles, I18.messages.role());

		VerticalPanel panel = new VerticalPanel();
		Label label = new Label(I18.messages.no_data());
		panel.add(label);
		panel.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_CENTER);
		table.setEmptyTableWidget(label);
		table.setSelectionModel(multiSelectionModel,
				DefaultSelectionEventManager.<RolePx> createCustomManager(new MultiSelectEventTranslator<RolePx>(new MultiSelectEventTranslator.RowSelectCommand<RolePx>() {
					RolePx	selected;

					@Override
					public void execute() {
						getViewFactory().getListRolesView().setRoleSelToEdit(true);
						getViewFactory().getListRolesView().setRolePxToEdit(selected);
						presenter.getPermById(selected);
					}

					@Override
					public void setSelected(RolePx selected) {
						this.selected = selected;
					}
				})));
		pager.setRangeLimited(true);
		pager.setDisplay(table);
		AsyncHandler columnSortHandler = new AsyncHandler(table);
		table.addColumnSortHandler(columnSortHandler);
	}

	public MultiSelectionModel<RolePx> getSelectionModel() {
		return this.multiSelectionModel;
	}

	public void setDataProvider(AsyncDP<RolePx> dataProvider) {
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

	public CellTable<RolePx> getTable() {
		return this.table;
	}

	ViewFactory getViewFactory() {
		return injector.getDesktopViewFactory();
	}

	public static ProvidesKey<RolePx>	keyProvider	= new ProvidesKey<RolePx>() {

														@Override
														public Object getKey(RolePx item) {
															return item == null ? null : item.getId();
														}
													};
}
