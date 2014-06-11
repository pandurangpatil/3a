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

import java.util.ArrayList;
import java.util.List;

import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.gwt.common.client.widget.MessagePanel.MessageType;
import com.agnie.useradmin.main.client.I18;
import com.agnie.useradmin.main.client.helper.MainQSProcessor;
import com.agnie.useradmin.main.client.mvp.PlaceToken;
import com.agnie.useradmin.main.client.presenter.sahered.ui.Menu;
import com.agnie.useradmin.main.client.ui.DomainParentView;
import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.persistance.shared.proxy.ApplicationPx;
import com.agnie.useradmin.persistance.shared.proxy.RolePx;
import com.agnie.useradmin.persistance.shared.service.ApplicationManagerRequest;
import com.agnie.useradmin.persistance.shared.service.RoleManagerRequest;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

@Singleton
public class DomainPresenter extends BaseDomainSetPresenter {

	DomainParentView		view;
	@Inject
	URLInfo					urlInfo;
	@Inject
	UserAdminURLGenerator	uaug;

	@Override
	public boolean go() {
		if (super.go()) {
			view = viewFactory.getDomainParentView();
			RootPanel contentPanel = clientFactory.getRootPanelFactory().getContentPanel();
			HTMLPanel centerPanel = viewFactory.getCenterContentPanel();
			messagePanel.hide();
			final Menu menu = viewFactory.getMenu();
			menu.getSearchBox().clearSearchImgClkHandlers();
			setPrevData(centerPanel);
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

	public void setPrevData(final HTMLPanel centerPanel) {
		String domain = MainQSProcessor.getSelDomain();
		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		amr.getApplicationByDomainName(domain).fire(new Receiver<ApplicationPx>() {

			@Override
			public void onSuccess(ApplicationPx response) {
				if (response != null) {
					view.init(response);
					centerPanel.add(view);

					getSelDefAppRoles();
					getSelDefContextRoles();
				}
			}
		});
	}

	public void generateNewApiAccessKey(ApplicationPx appPx) {
		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		amr.generateNewApiAccessKey(appPx).fire(new Receiver<ApplicationPx>() {

			@Override
			public void onSuccess(ApplicationPx response) {
				view.setData(response);
			}

			@Override
			public void onFailure(ServerFailure error) {
				messagePanel.show(true);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
				messagePanel.setType(MessageType.ERROR);
			}
		});
	}

	public void editApp(ApplicationManagerRequest amr, ApplicationPx appPx, final boolean domainChange) {
		amr.updateApplication(appPx).fire(new Receiver<ApplicationPx>() {

			@Override
			public void onSuccess(ApplicationPx response) {
				messagePanel.show(true);
				messagePanel.setMessage(I18.messages.editAppSucced());
				messagePanel.setType(MessageType.INFORMATION);
				view.init(response);
				/*
				 * if (domainChange) { String newUrl = UserAdminURLGenerator.editSelDomain(response.getDomain(),
				 * params); Window.Location.assign(newUrl); }
				 */
			}

			@Override
			public void onFailure(ServerFailure error) {
				messagePanel.show(true);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
				messagePanel.setType(MessageType.ERROR);
			}
		});
	}

	public void getAllDefAppRoles(final List<RolePx> selected) {
		RoleManagerRequest rmr = clientFactory.getRequestFactory().roleManager();
		rmr.getRolesByApplication(null, AuthLevel.APPLICATION, null).fire(new Receiver<List<RolePx>>() {

			@Override
			public void onSuccess(List<RolePx> response) {
				if (response != null) {
					view.setTotalAppRoles(response);
				} else {
					response = new ArrayList<RolePx>();
					view.setTotalAppRoles(response);
				}
				view.setSelAppRolesList(selected);
			}
		});
	}

	public void getAllDefContextRoles(final List<RolePx> selected) {
		RoleManagerRequest rmr = clientFactory.getRequestFactory().roleManager();
		rmr.getRolesByApplication(null, AuthLevel.CONTEXT, null).fire(new Receiver<List<RolePx>>() {

			@Override
			public void onSuccess(List<RolePx> response) {
				if (response != null) {
					view.setTotalContextRoles(response);
				} else {
					response = new ArrayList<RolePx>();
					view.setTotalContextRoles(response);
				}
				view.setSelCTXRolesList(selected);
			}
		});
	}

	public void getSelDefAppRoles() {
		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		amr.getDefaultAppRoles().fire(new Receiver<List<RolePx>>() {

			@Override
			public void onSuccess(List<RolePx> response) {
				if (response != null) {
					view.setdefAppRolesList(response);
				}
				getAllDefAppRoles(response);
			}

			@Override
			public void onFailure(ServerFailure error) {
				messagePanel.show(true);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
				messagePanel.setType(MessageType.ERROR);
			}
		});
	}

	public void getSelDefContextRoles() {
		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		amr.getDefaultAppCtxRoles().fire(new Receiver<List<RolePx>>() {

			@Override
			public void onSuccess(List<RolePx> response) {
				if (response != null) {
					view.setdefContextRolesList(response);
				}
				getAllDefContextRoles(response);
			}

			@Override
			public void onFailure(ServerFailure error) {
				messagePanel.show(true);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
				messagePanel.setType(MessageType.ERROR);
			}
		});
	}

	public void updateDefAppRoles(final List<RolePx> roles) {
		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		amr.updateDefaultAppRoles(roles).fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				messagePanel.show(true);
				messagePanel.setMessage(I18.messages.defAppRolesUpdated());
				messagePanel.setType(MessageType.INFORMATION);
				view.setdefAppRolesList(roles);
				view.hideReadView();
			}

			public void onFailure(ServerFailure error) {
				messagePanel.show(true);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
				messagePanel.setType(MessageType.ERROR);
			}
		});
	}

	public void updateDefCTXRoles(final List<RolePx> roles) {
		ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
		amr.updateDefaultAppCtxRoles(roles).fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				messagePanel.show(true);
				messagePanel.setMessage(I18.messages.defContextRolesupdated());
				messagePanel.setType(MessageType.INFORMATION);
				view.setdefContextRolesList(roles);
				view.hideReadView();
			}

			public void onFailure(ServerFailure error) {
				messagePanel.show(true);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
				messagePanel.setType(MessageType.ERROR);
			}
		});
	}

	// public void transferOwnerShip(String username) {
	// ApplicationManagerRequest amr = clientFactory.getRequestFactory().applicationManager();
	// amr.transferOwnerShip(username).fire(new Receiver<Void>() {
	//
	// @Override
	// public void onSuccess(Void response) {
	// Window.alert(I18.messages.owner_transferred());
	// Window.Location.assign(uaug.getLandingPageUrl(urlInfo, UserAdminURLGenerator.USERADMIN,
	// urlInfo.getParameter(QueryString.GWT_DEV_MODE.getKey())));
	// }
	//
	// public void onFailure(ServerFailure error) {
	// messagePanel.show(true);
	// messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
	// messagePanel.setType(MessageType.ERROR);
	// }
	// });
	// }

	@Override
	public void postRender() {

	}
}
