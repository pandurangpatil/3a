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
package com.agnie.useradmin.main.client.presenter;

import java.util.List;

import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.gwt.common.client.rpc.AsyncDP;
import com.agnie.gwt.common.client.widget.BreadCrumbPanel;
import com.agnie.gwt.common.client.widget.MessagePanel.MessageType;
import com.agnie.useradmin.common.client.helper.AbstractDataFetcher;
import com.agnie.useradmin.main.client.I18;
import com.agnie.useradmin.main.client.mvp.PlaceToken;
import com.agnie.useradmin.main.client.presenter.sahered.ui.ListMenu;
import com.agnie.useradmin.main.client.ui.DesktopViewFactory.AuthLevelWrapper;
import com.agnie.useradmin.main.client.ui.RolesView;
import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.shared.proxy.PermissionPx;
import com.agnie.useradmin.persistance.shared.proxy.RolePx;
import com.agnie.useradmin.persistance.shared.service.PermissionManagerRequest;
import com.agnie.useradmin.persistance.shared.service.RoleManagerRequest;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.Range;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

@Singleton
public class RolesPresenter extends BasePresenter {
	RolesView				view;
	RoleDataFetcher			dataFetcher;
	private ClickHandler	roleBreadCrumbClikhandler	= new ClickHandler() {

															@Override
															public void onClick(ClickEvent event) {
																appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.ROLES));
															}
														};

	public boolean go() {
		if (super.go() && appACLProvider.get() != null && appACLProvider.get().check(Permissions.ROLE)) {
			BreadCrumbPanel breadCrumbPanel = viewFactory.getBreadCrumbPanel();
			breadCrumbPanel.addBreadCrumb(I18.messages.roles());
			breadCrumbPanel.getBreadCrumb(1).addClickHandler(roleBreadCrumbClikhandler);

			viewFactory.getListMenu().selectTab(ListMenu.Tab.ROLE.getIndex());
			RootPanel contentPanel = clientFactory.getRootPanelFactory().getContentPanel();
			HTMLPanel centerPanel = viewFactory.getCenterContentPanel();
			view = viewFactory.getListRolesView();
			if (dataFetcher == null) {
				dataFetcher = new RoleDataFetcher(view);
				AsyncDP<RolePx> roleDp = new AsyncDP<RolePx>(dataFetcher);
				view.setDataProvider(roleDp);
			}
			messagePanel.hide();

			viewFactory.getMenu().getSearchBox().clearSearchImgClkHandlers();
			viewFactory.getMenu().getSearchBox().addSearchImgClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					viewFactory.getListRolesView().refreshPage();
				}
			});
			getPermissions();
			view.reset();
			view.initialize();
			centerPanel.add(view);

			contentPanel.add(centerPanel);
			return true;
		} else {
			Scheduler.get().scheduleDeferred(new Command() {
				public void execute() {
					appController.getPlaceManager().changePlace(new Place<PlaceToken>(appController.getDefaultPlace()));
				}
			});
			return false;
		}
	}

	public class RoleDataFetcher extends AbstractDataFetcher<RolePx> {
		RolesView	view;

		/**
		 * @param view
		 */
		public RoleDataFetcher(RolesView view) {
			super();
			this.view = view;
		}

		@Override
		public void fire(Range range, Receiver<List<RolePx>> reciever) {
			RoleManagerRequest rmr = clientFactory.getRequestFactory().roleManager();
			AuthLevelWrapper level = (AuthLevelWrapper) viewFactory.getLevelFilter().getSelectedItem();
			if ((viewFactory.getMenu().getSearchBox().getValue() != null) && (!(viewFactory.getMenu().getSearchBox().getValue().isEmpty()))) {
				if (AuthLevel.CONTEXT.equals(level.getLevel())) {
					rmr.getRolesByApplication(getPaging(range, view, rmr), AuthLevel.CONTEXT, viewFactory.getMenu().getSearchBox().getValue()).fire(reciever);
				} else {
					rmr.getRolesByApplication(getPaging(range, view, rmr), AuthLevel.APPLICATION, viewFactory.getMenu().getSearchBox().getValue()).fire(reciever);
				}
			} else {
				if (AuthLevel.CONTEXT.equals(level.getLevel())) {
					rmr.getRolesByApplication(getPaging(range, view, rmr), AuthLevel.CONTEXT, null).fire(reciever);
				} else {
					rmr.getRolesByApplication(getPaging(range, view, rmr), AuthLevel.APPLICATION, null).fire(reciever);
				}
			}
		}
	}

	public void saveRole(RoleManagerRequest rmr, RolePx rolePx) {
		rmr.save(rolePx).fire(new Receiver<RolePx>() {

			@Override
			public void onSuccess(RolePx response) {
				viewFactory.getListRolesView().refreshPage();
				viewFactory.getListRolesView().reset();
				messagePanel.show(true);
				messagePanel.setType(MessageType.INFORMATION);
				messagePanel.setMessage(I18.messages.roleSaved());
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
			}
		});
	}

	public void deletRole(List<String> listToDel) {
		RoleManagerRequest rmr = clientFactory.getRequestFactory().roleManager();
		rmr.removeRolesByIds(listToDel).fire(new Receiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				viewFactory.getListRolesView().refreshPage();
				viewFactory.getListRolesView().reset();
				messagePanel.show(true);
				messagePanel.setType(MessageType.INFORMATION);
				messagePanel.setMessage(I18.messages.roleDeleted());
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
			}
		});
	}

	public void getPermissions() {
		PermissionManagerRequest pmr = clientFactory.getRequestFactory().permissionManager();
		AuthLevelWrapper level = (AuthLevelWrapper) viewFactory.getLevelFilter().getSelectedItem();
		if (AuthLevel.CONTEXT.equals(level.getLevel())) {
			pmr.getPermissionsByApplication(null, AuthLevel.CONTEXT, null).fire(new Receiver<List<PermissionPx>>() {

				@Override
				public void onSuccess(List<PermissionPx> response) {
					viewFactory.getListRolesView().setListPermissionPx(response);
				}
			});
		} else {
			pmr.getPermissionsByApplication(null, AuthLevel.APPLICATION, null).fire(new Receiver<List<PermissionPx>>() {

				@Override
				public void onSuccess(List<PermissionPx> response) {
					viewFactory.getListRolesView().setListPermissionPx(response);
				}
			});
		}
	}

	public void getPermById(RolePx rolePx) {
		RoleManagerRequest rmr = clientFactory.getRequestFactory().roleManager();
		rmr.getRoleById(rolePx.getId()).with("permissions").fire(new Receiver<RolePx>() {

			@Override
			public void onSuccess(RolePx response) {
				viewFactory.getListRolesView().setRolesViewData(response);
			}
		});
	}

	@Override
	public void postRender() {
		view.setDefaultFocus();
	}

}
