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
import com.agnie.useradmin.main.client.presenter.ApplicationUserPresenter;
import com.agnie.useradmin.main.client.ui.ViewFactory;
import com.agnie.useradmin.persistance.client.enums.RequestStatus;
import com.agnie.useradmin.persistance.client.enums.SortOrder;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.shared.proxy.UserApplicationRegistrationPx;
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
public class AppUsersCellTable extends Composite implements I18 {
	interface MyUiBinder extends UiBinder<Widget, AppUsersCellTable> {
	}

	private static MyUiBinder									uiBinder	= GWT.create(MyUiBinder.class);
	@UiField
	CellTable<UserApplicationRegistrationPx>					table;

	@UiField
	SimplePager													pager;

	AsyncDP<UserApplicationRegistrationPx>						dataProvider;
	private MultiSelectionModel<UserApplicationRegistrationPx>	multiSelectionModel;

	@Inject
	ApplicationUserPresenter									presenter;

	@Inject
	MVPInjector													injector;

	public AppUsersCellTable() {
		initWidget(uiBinder.createAndBindUi(this));
		multiSelectionModel = new MultiSelectionModel<UserApplicationRegistrationPx>(keyProvider);
		table.setPageSize(ApplicationConfig.getInstance().getPageSize());

		Column<UserApplicationRegistrationPx, Boolean> select = new Column<UserApplicationRegistrationPx, Boolean>(new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(UserApplicationRegistrationPx object) {
				return multiSelectionModel.isSelected(object);
			}
		};
		table.addColumn(select, messages.select());

		Column<UserApplicationRegistrationPx, String> fName = new TextColumn<UserApplicationRegistrationPx>() {
			@Override
			public String getValue(UserApplicationRegistrationPx object) {
				return object.getUser().getFirstName();
			}
		};
		fName.setSortable(true);
		fName.setDataStoreName("user.firstName");
		table.addColumn(fName, messages.fName());

		Column<UserApplicationRegistrationPx, String> lName = new TextColumn<UserApplicationRegistrationPx>() {
			@Override
			public String getValue(UserApplicationRegistrationPx object) {
				return object.getUser().getLastName();
			}
		};
		lName.setSortable(true);
		lName.setDataStoreName("user.lastName");
		table.addColumn(lName, messages.lName());

		Column<UserApplicationRegistrationPx, String> userName = new TextColumn<UserApplicationRegistrationPx>() {
			@Override
			public String getValue(UserApplicationRegistrationPx object) {
				return object.getUser().getUserName();
			}
		};
		userName.setSortable(true);
		userName.setDataStoreName("user.userName");
		table.addColumn(userName, messages.uName());

		Column<UserApplicationRegistrationPx, String> status = new TextColumn<UserApplicationRegistrationPx>() {
			@Override
			public String getValue(UserApplicationRegistrationPx object) {
				return (object.getStatus() != null ? object.getStatus().getLocalized() : RequestStatus.DISABLED.getLocalized());
			}
		};
		status.setSortable(true);
		status.setDataStoreName("status");
		table.addColumn(status, messages.status());

		Column<UserApplicationRegistrationPx, String> roles = new Column<UserApplicationRegistrationPx, String>(new ButtonCell()) {
			@Override
			public String getValue(UserApplicationRegistrationPx object) {
				return messages.view_edit();
			}
		};
		roles.setFieldUpdater(new FieldUpdater<UserApplicationRegistrationPx, String>() {

			@Override
			public void update(int index, UserApplicationRegistrationPx object, String value) {
				if (presenter.checkPermission(Permissions.APPLICATION_USER_ROLE_MANAGER)) {
					getViewFactory().getAppUsersView().getUserAppRoles(object);
				}
			}
		});
		table.addColumn(roles, messages.roles());

		Column<UserApplicationRegistrationPx, String> adminRoles = new Column<UserApplicationRegistrationPx, String>(new ButtonCell()) {
			@Override
			public String getValue(UserApplicationRegistrationPx object) {
				return messages.view_edit();
			}
		};
		adminRoles.setFieldUpdater(new FieldUpdater<UserApplicationRegistrationPx, String>() {

			@Override
			public void update(int index, UserApplicationRegistrationPx object, String value) {
				if (presenter.checkPermission(Permissions.APPLICATION_USER_ADMIN_ROLE_MANAGER)) {
					getViewFactory().getAppUsersView().getUserAppAdminRoles(object);
				}
			}
		});
		table.addColumn(adminRoles, messages.adminRoles());

		VerticalPanel panel = new VerticalPanel();
		Label label = new Label(messages.no_data());
		panel.add(label);
		panel.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_CENTER);
		table.setEmptyTableWidget(label);
		table.setSelectionModel(multiSelectionModel, DefaultSelectionEventManager.<UserApplicationRegistrationPx> createCustomManager(new MultiSelectEventTranslator<UserApplicationRegistrationPx>()));
		pager.setRangeLimited(true);
		pager.setDisplay(table);
		AsyncHandler columnSortHandler = new AsyncHandler(table);
		table.addColumnSortHandler(columnSortHandler);
	}

	public MultiSelectionModel<UserApplicationRegistrationPx> getSelectionModel() {
		return this.multiSelectionModel;
	}

	public void setDataProvider(AsyncDP<UserApplicationRegistrationPx> dataProvider) {
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

	ViewFactory getViewFactory() {
		return injector.getDesktopViewFactory();
	}

	public static ProvidesKey<UserApplicationRegistrationPx>	keyProvider	= new ProvidesKey<UserApplicationRegistrationPx>() {

																				@Override
																				public Object getKey(UserApplicationRegistrationPx item) {
																					return item == null ? null : item.getId();
																				}
																			};

}
