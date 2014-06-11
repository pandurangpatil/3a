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
import com.agnie.useradmin.main.client.mvp.PlaceToken;
import com.agnie.useradmin.main.client.presenter.sahered.ui.ListMenu;
import com.agnie.useradmin.main.client.ui.ApplicationUsersView;
import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.agnie.useradmin.persistance.client.enums.RequestStatus;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.client.service.I18;
import com.agnie.useradmin.persistance.shared.proxy.RolePx;
import com.agnie.useradmin.persistance.shared.proxy.UserApplicationRegistrationPx;
import com.agnie.useradmin.persistance.shared.service.ApplicationManagerRequest;
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
public class ApplicationUserPresenter extends BasePresenter {

	ApplicationUsersView		view;
	UserApplicationDataFetcher	dataFetcher;

	@Override
	public boolean go() {
		if (super.go() && appACLProvider.get() != null && appACLProvider.get().check(Permissions.APPLICATION_USER_MANAGER)) {
			BreadCrumbPanel breadCrumbPanel = viewFactory.getBreadCrumbPanel();
			breadCrumbPanel.addBreadCrumb(I18.messages.users());
			viewFactory.getListMenu().selectTab(ListMenu.Tab.USERS.getIndex());
			RootPanel contentPanel = clientFactory.getRootPanelFactory().getContentPanel();
			HTMLPanel centerPanel = viewFactory.getCenterContentPanel();
			view = viewFactory.getAppUsersView();
			view.initialize();
			if (dataFetcher == null) {
				dataFetcher = new UserApplicationDataFetcher(view);
				AsyncDP<UserApplicationRegistrationPx> userDp = new AsyncDP<UserApplicationRegistrationPx>(dataFetcher);
				view.setDataProvider(userDp);
			}
			getTotalUserAppRoles();
			getTotalUserAppAdminRoles();
			viewFactory.getMenu().getSearchBox().clearSearchImgClkHandlers();
			viewFactory.getMenu().getSearchBox().addSearchImgClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					viewFactory.getAppUsersView().refreshPage();
				}
			});

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

	public class UserApplicationDataFetcher extends AbstractDataFetcher<UserApplicationRegistrationPx> {
		ApplicationUsersView	view;

		/**
		 * @param view
		 */
		public UserApplicationDataFetcher(ApplicationUsersView view) {
			super();
			this.view = view;
		}

		@Override
		public void fire(Range range, Receiver<List<UserApplicationRegistrationPx>> reciever) {
			ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
			if ((viewFactory.getMenu().getSearchBox().getValue() != null) && (!(viewFactory.getMenu().getSearchBox().getValue().isEmpty()))) {
				if (I18.messages.all().equals(view.getStatusFilter().getTitle())) {
					amr.getUsersByApplication(getPaging(range, view, amr), null, viewFactory.getMenu().getSearchBox().getValue()).with("user").fire(reciever);
				} else {
					amr.getUsersByApplication(getPaging(range, view, amr), view.getStatusFilter().getReqStatus(), viewFactory.getMenu().getSearchBox().getValue()).with("user").fire(reciever);
				}
			} else {
				if (I18.messages.all().equals(view.getStatusFilter().getTitle())) {
					amr.getUsersByApplication(getPaging(range, view, amr), null, null).with("user").fire(reciever);
				} else {
					amr.getUsersByApplication(getPaging(range, view, amr), view.getStatusFilter().getReqStatus(), null).with("user").fire(reciever);
				}
			}
		}
	}

	public void approveUserAppReg(List<String> ids) {
		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		amr.updateStatusByUserAppRegId(ids, RequestStatus.ACTIVE).fire(new Receiver<List<String>>() {

			@Override
			public void onSuccess(List<String> arg0) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.INFORMATION);
				messagePanel.setMessage(com.agnie.useradmin.main.client.I18.messages.approveActive());
				viewFactory.getAppUsersView().refreshPage();
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
			}
		});
	}

	public void approveProvUserAppReg(List<String> ids) {
		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		amr.updateStatusByUserAppRegId(ids, RequestStatus.PROVISIONAL).fire(new Receiver<List<String>>() {

			@Override
			public void onSuccess(List<String> arg0) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.INFORMATION);
				messagePanel.setMessage(com.agnie.useradmin.main.client.I18.messages.approveProvReq());
				viewFactory.getAppUsersView().refreshPage();
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
			}
		});
	}

	public void markDisabledByUserAppRegId(List<String> ids) {
		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		amr.markDisabledByUserAppRegId(ids).fire(new Receiver<List<String>>() {

			@Override
			public void onSuccess(List<String> response) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.INFORMATION);
				messagePanel.setMessage(com.agnie.useradmin.main.client.I18.messages.userDisabled());
				viewFactory.getAppUsersView().refreshPage();
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
			}
		});
	}

	public void getSelUserApplicationRoles(UserApplicationRegistrationPx uar) {
		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		amr.getUserApplicationRoles(uar).fire(new Receiver<List<RolePx>>() {

			@Override
			public void onSuccess(List<RolePx> response) {
				viewFactory.getAppUsersView().setSelRolesList(response);
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
			}
		});
	}

	public void getTotalUserAppRoles() {
		if (appACLProvider.get().check(Permissions.APPLICATION_USER_ROLE_MANAGER)) {
			RoleManagerRequest rmr = clientFactory.getRequestFactory().roleManager();
			rmr.getRolesByApplication(null, AuthLevel.APPLICATION, null).fire(new Receiver<List<RolePx>>() {

				@Override
				public void onSuccess(List<RolePx> response) {
					viewFactory.getAppUsersView().setRolePxList(response);
				}
			});
		}
	}

	public void updateUserRoles(UserApplicationRegistrationPx uar, List<RolePx> roles) {
		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		amr.updateUserApplicationRoles(uar, roles).fire(new Receiver<UserApplicationRegistrationPx>() {

			@Override
			public void onSuccess(UserApplicationRegistrationPx response) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.INFORMATION);
				messagePanel.setMessage(com.agnie.useradmin.main.client.I18.messages.userRoleUpdated());
				viewFactory.getAppUsersView().resetAppUserView();
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
			}
		});
	}

	public void getTotalUserAppAdminRoles() {
		if (appACLProvider.get().check(Permissions.APPLICATION_USER_ADMIN_ROLE_MANAGER)) {
			ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
			amr.getAdminRoles().fire(new Receiver<List<RolePx>>() {

				@Override
				public void onSuccess(List<RolePx> response) {
					viewFactory.getAppUsersView().setAdminRolePxList(response);
				}
			});
		}
	}

	public void updateUserAdminRoles(UserApplicationRegistrationPx uar, List<RolePx> roles) {
		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		amr.updateUserApplicationAdminRoles(uar, roles).fire(new Receiver<UserApplicationRegistrationPx>() {

			@Override
			public void onSuccess(UserApplicationRegistrationPx response) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.INFORMATION);
				messagePanel.setMessage(com.agnie.useradmin.main.client.I18.messages.userAdminRoleUpdated());
				viewFactory.getAppUsersView().resetAppUserView();
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
			}
		});
	}

	public void getSelUserApplicationAdminRoles(UserApplicationRegistrationPx uar) {
		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		amr.getUserApplicationAdminRoles(uar).fire(new Receiver<List<RolePx>>() {

			@Override
			public void onSuccess(List<RolePx> response) {
				viewFactory.getAppUsersView().setSelAdminRolesList(response);
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
			}
		});
	}

	@Override
	public void postRender() {

	}
}
