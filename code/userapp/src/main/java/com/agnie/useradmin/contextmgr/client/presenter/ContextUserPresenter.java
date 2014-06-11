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
package com.agnie.useradmin.contextmgr.client.presenter;

import java.util.List;

import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.gwt.common.client.rpc.AsyncDP;
import com.agnie.gwt.common.client.widget.MessagePanel.MessageType;
import com.agnie.useradmin.common.client.helper.AbstractDataFetcher;
import com.agnie.useradmin.contextmgr.client.mvp.PlaceToken;
import com.agnie.useradmin.contextmgr.client.presenter.shared.ui.ListMenu;
import com.agnie.useradmin.contextmgr.client.ui.ContextUsersView;
import com.agnie.useradmin.persistance.client.enums.RequestStatus;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.client.service.I18;
import com.agnie.useradmin.persistance.shared.proxy.RolePx;
import com.agnie.useradmin.persistance.shared.proxy.UserApplicationCtxRegistrationPx;
import com.agnie.useradmin.persistance.shared.service.ContextManagerRequest;
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
public class ContextUserPresenter extends BasePresenter {

	ContextUsersView		view;
	UserContextDataFetcher	dataFetcher;

	@Override
	public boolean go() {
		if (checkPermission(Permissions.CONTEXT_USER_MANAGER)) {
			super.go();
			RootPanel contentPanel = clientFactory.getRootPanelFactory().getContentPanel();

			HTMLPanel centerPanel = viewFactory.getCenterContentPanel();

			view = viewFactory.getCTXUsersView();
			if (dataFetcher == null) {
				dataFetcher = new UserContextDataFetcher(view);
				AsyncDP<UserApplicationCtxRegistrationPx> userDp = new AsyncDP<UserApplicationCtxRegistrationPx>(dataFetcher);
				view.setDataProvider(userDp);
			}
			view.initialize();

			getTotalUserCTXRoles();

			getTotalUserCTXAdminRoles();

			viewFactory.getMenu().getSearchBox().clearSearchImgClkHandlers();
			viewFactory.getMenu().getSearchBox().addSearchImgClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					viewFactory.getCTXUsersView().refreshPage();
				}
			});

			centerPanel.add(view);
			contentPanel.add(centerPanel);
			viewFactory.getListMenu().selectTab(ListMenu.Tab.USERS.getIndex());
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

	public class UserContextDataFetcher extends AbstractDataFetcher<UserApplicationCtxRegistrationPx> {
		ContextUsersView	view;

		/**
		 * @param view
		 */
		public UserContextDataFetcher(ContextUsersView view) {
			super();
			this.view = view;
		}

		@Override
		public void fire(Range range, Receiver<List<UserApplicationCtxRegistrationPx>> reciever) {
			ContextManagerRequest cmr = clientFactory.getRequestFactory().contextManager();
			if ((viewFactory.getMenu().getSearchBox().getValue() != null) && (!(viewFactory.getMenu().getSearchBox().getValue().isEmpty()))) {
				if (I18.messages.all().equals(view.getStatusFilter().getTitle())) {
					cmr.getUsersByContext(getPaging(range, view, cmr), null, viewFactory.getMenu().getSearchBox().getValue()).with("user").fire(reciever);
				} else {
					cmr.getUsersByContext(getPaging(range, view, cmr), view.getStatusFilter().getReqStatus(), viewFactory.getMenu().getSearchBox().getValue()).with("user").fire(reciever);
				}
			} else {
				if (I18.messages.all().equals(view.getStatusFilter().getTitle())) {
					cmr.getUsersByContext(getPaging(range, view, cmr), null, null).with("user").fire(reciever);
				} else {
					cmr.getUsersByContext(getPaging(range, view, cmr), view.getStatusFilter().getReqStatus(), null).with("user").fire(reciever);
				}
			}
		}
	}

	public void approveUserCTXReg(List<String> ids) {
		ContextManagerRequest cmr = clientFactory.getRequestFactory().contextManager();
		cmr.updateStatusByUserAppCtxRegId(ids, RequestStatus.ACTIVE).fire(new Receiver<List<String>>() {

			@Override
			public void onSuccess(List<String> arg0) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.INFORMATION);
				messagePanel.setMessage(com.agnie.useradmin.contextmgr.client.I18.messages.approveActive());
				view.refreshPage();
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
			}
		});
	}

	public void approveProvUserCTXReg(List<String> ids) {
		ContextManagerRequest cmr = clientFactory.getRequestFactory().contextManager();
		cmr.updateStatusByUserAppCtxRegId(ids, RequestStatus.PROVISIONAL).fire(new Receiver<List<String>>() {

			@Override
			public void onSuccess(List<String> arg0) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.INFORMATION);
				messagePanel.setMessage(com.agnie.useradmin.contextmgr.client.I18.messages.approveProvReq());
				view.refreshPage();
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
			}
		});
	}

	public void markDisabledByUserCTXRegId(List<String> ids) {
		ContextManagerRequest cmr = clientFactory.getRequestFactory().contextManager();
		cmr.markDisabledByUserAppCtxRegId(ids).fire(new Receiver<List<String>>() {

			@Override
			public void onSuccess(List<String> response) {
				// TODO Auto-generated method stub
				messagePanel.show(true);
				messagePanel.setType(MessageType.INFORMATION);
				messagePanel.setMessage(com.agnie.useradmin.contextmgr.client.I18.messages.userDisabled());
				view.refreshPage();
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
			}
		});
	}

	public void getSelUserCTXRoles(final UserApplicationCtxRegistrationPx ucr) {
		ContextManagerRequest cmr = clientFactory.getRequestFactory().contextManager();
		cmr.getUserContextRoles(ucr).fire(new Receiver<List<RolePx>>() {

			@Override
			public void onSuccess(List<RolePx> response) {
				view.setSelRolesList(response);
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
			}
		});
	}

	public void getTotalUserCTXRoles() {
		ContextManagerRequest cmr = clientFactory.getRequestFactory().contextManager();
		cmr.getContextRoles().fire(new Receiver<List<RolePx>>() {

			@Override
			public void onSuccess(List<RolePx> response) {
				view.setRolePxList(response);
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
			}
		});
	}

	public void updateUserRoles(UserApplicationCtxRegistrationPx uar, List<RolePx> roles) {
		ContextManagerRequest cmr = clientFactory.getRequestFactory().contextManager();
		cmr.updateUserApplicationRoles(uar, roles).fire(new Receiver<UserApplicationCtxRegistrationPx>() {

			@Override
			public void onSuccess(UserApplicationCtxRegistrationPx response) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.INFORMATION);
				messagePanel.setMessage(com.agnie.useradmin.contextmgr.client.I18.messages.userRoleUpdated());
				view.resetCTXUserView();
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
				view.resetCTXUserView();
			}
		});
	}

	public void getTotalUserCTXAdminRoles() {
		ContextManagerRequest cmr = clientFactory.getRequestFactory().contextManager();
		cmr.getAdminRoles().fire(new Receiver<List<RolePx>>() {

			@Override
			public void onSuccess(List<RolePx> response) {
				view.setAdminRolePxList(response);
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
			}
		});
	}

	public void getSelUserCTXAdminRoles(final UserApplicationCtxRegistrationPx ucr) {
		ContextManagerRequest cmr = clientFactory.getRequestFactory().contextManager();
		cmr.getUserContextAdminRoles(ucr).fire(new Receiver<List<RolePx>>() {

			@Override
			public void onSuccess(List<RolePx> response) {
				view.setSelAdminRolesList(response);
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
			}
		});
	}

	public void updateUserAdminRoles(UserApplicationCtxRegistrationPx uar, List<RolePx> roles) {
		ContextManagerRequest cmr = clientFactory.getRequestFactory().contextManager();
		cmr.updateUserConextAdminRoles(uar, roles).fire(new Receiver<UserApplicationCtxRegistrationPx>() {

			@Override
			public void onSuccess(UserApplicationCtxRegistrationPx response) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.INFORMATION);
				messagePanel.setMessage(com.agnie.useradmin.contextmgr.client.I18.messages.userAdminRoleUpdated());
				view.resetCTXUserView();
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setType(MessageType.ERROR);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
				view.resetCTXUserView();
			}
		});
	}

	@Override
	public void postRender() {

	}

}
