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

import com.agnie.gwt.common.client.rpc.AsyncDP;
import com.agnie.gwt.common.client.rpc.MultiSelectEventTranslator;
import com.agnie.useradmin.common.client.ApplicationConfig;
import com.agnie.useradmin.common.client.base.Sort;
import com.agnie.useradmin.contextmgr.client.I18;
import com.agnie.useradmin.contextmgr.client.injector.MVPInjector;
import com.agnie.useradmin.contextmgr.client.presenter.ContextUserPresenter;
import com.agnie.useradmin.contextmgr.client.ui.ViewFactory;
import com.agnie.useradmin.persistance.client.enums.RequestStatus;
import com.agnie.useradmin.persistance.client.enums.SortOrder;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.shared.proxy.UserApplicationCtxRegistrationPx;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
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
public class CTXUsersCellTable extends Composite implements I18 {
	interface MyUiBinder extends UiBinder<Widget, CTXUsersCellTable> {
	}

	@Inject
	private MVPInjector												injector;
	@Inject
	private ContextUserPresenter									presenter;

	private static MyUiBinder										uiBinder	= GWT.create(MyUiBinder.class);
	@UiField
	CellTable<UserApplicationCtxRegistrationPx>						table;

	@UiField
	SimplePager														pager;

	AsyncDP<UserApplicationCtxRegistrationPx>						dataProvider;
	private MultiSelectionModel<UserApplicationCtxRegistrationPx>	multiSelectionModel;

	public CTXUsersCellTable() {
		initWidget(uiBinder.createAndBindUi(this));
		multiSelectionModel = new MultiSelectionModel<UserApplicationCtxRegistrationPx>(keyProvider);
		table.setPageSize(ApplicationConfig.getInstance().getPageSize());

		Column<UserApplicationCtxRegistrationPx, Boolean> select = new Column<UserApplicationCtxRegistrationPx, Boolean>(new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(UserApplicationCtxRegistrationPx object) {
				return multiSelectionModel.isSelected(object);
			}
		};
		table.addColumn(select, messages.select());

		Column<UserApplicationCtxRegistrationPx, String> fname = new TextColumn<UserApplicationCtxRegistrationPx>() {
			@Override
			public String getValue(UserApplicationCtxRegistrationPx object) {
				return object.getUser().getFirstName();
			}
		};
		fname.setSortable(true);
		fname.setDataStoreName("user.firstName");
		table.addColumn(fname, messages.fName());

		Column<UserApplicationCtxRegistrationPx, String> lName = new TextColumn<UserApplicationCtxRegistrationPx>() {
			@Override
			public String getValue(UserApplicationCtxRegistrationPx object) {
				return object.getUser().getLastName();
			}
		};
		lName.setSortable(true);
		lName.setDataStoreName("user.lastName");
		table.addColumn(lName, messages.lName());

		Column<UserApplicationCtxRegistrationPx, String> userName = new TextColumn<UserApplicationCtxRegistrationPx>() {
			@Override
			public String getValue(UserApplicationCtxRegistrationPx object) {
				return object.getUser().getUserName();
			}
		};
		userName.setSortable(true);
		userName.setDataStoreName("user.userName");
		table.addColumn(userName, messages.uName());

		Column<UserApplicationCtxRegistrationPx, String> status = new TextColumn<UserApplicationCtxRegistrationPx>() {
			@Override
			public String getValue(UserApplicationCtxRegistrationPx object) {
				return (object.getStatus() != null ? object.getStatus().getLocalized() : RequestStatus.DISABLED.getLocalized());
			}
		};
		status.setSortable(true);
		status.setDataStoreName("status");
		table.addColumn(status, messages.status());

		Column<UserApplicationCtxRegistrationPx, String> roles = new Column<UserApplicationCtxRegistrationPx, String>(new ButtonCell()) {
			@Override
			public String getValue(UserApplicationCtxRegistrationPx object) {
				return messages.view_edit();
			}
		};
		roles.setFieldUpdater(new FieldUpdater<UserApplicationCtxRegistrationPx, String>() {

			@Override
			public void update(int index, UserApplicationCtxRegistrationPx object, String value) {
				if (presenter.checkPermission(Permissions.CONTEXT_USER_ROLE_MANAGER)) {
					getViewFactory().getCTXUsersView().getUserCTXRoles(object);
				}
			}
		});
		table.addColumn(roles, messages.roles());

		Column<UserApplicationCtxRegistrationPx, String> adminRoles = new Column<UserApplicationCtxRegistrationPx, String>(new ButtonCell()) {
			@Override
			public String getValue(UserApplicationCtxRegistrationPx object) {
				return messages.view_edit();
			}
		};
		adminRoles.setFieldUpdater(new FieldUpdater<UserApplicationCtxRegistrationPx, String>() {

			@Override
			public void update(int index, UserApplicationCtxRegistrationPx object, String value) {
				if (presenter.checkPermission(Permissions.CONTEXT_USER_ADMIN_ROLE_MANAGER)) {
					getViewFactory().getCTXUsersView().getUserCTXAdminRoles(object);
				}
			}
		});
		table.addColumn(adminRoles, messages.adminRoles());

		VerticalPanel panel = new VerticalPanel();
		Label label = new Label(messages.no_data());
		panel.add(label);
		panel.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_CENTER);
		table.setEmptyTableWidget(label);
		table.setSelectionModel(multiSelectionModel,
				DefaultSelectionEventManager.<UserApplicationCtxRegistrationPx> createCustomManager(new MultiSelectEventTranslator<UserApplicationCtxRegistrationPx>()));
		pager.setRangeLimited(true);
		pager.setDisplay(table);
		AsyncHandler columnSortHandler = new AsyncHandler(table);
		table.addColumnSortHandler(columnSortHandler);
	}

	public MultiSelectionModel<UserApplicationCtxRegistrationPx> getSelectionModel() {
		return this.multiSelectionModel;
	}

	public void setDataProvider(AsyncDP<UserApplicationCtxRegistrationPx> dataProvider) {
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

	public static ProvidesKey<UserApplicationCtxRegistrationPx>	keyProvider	= new ProvidesKey<UserApplicationCtxRegistrationPx>() {

																				@Override
																				public Object getKey(UserApplicationCtxRegistrationPx item) {
																					return item == null ? null : item.getId();
																				}
																			};

	ViewFactory getViewFactory() {
		return injector.getDesktopViewFactory();
	}

}
