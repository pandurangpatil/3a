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
import com.agnie.useradmin.main.client.ui.PermissionsView;
import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.shared.proxy.PermissionPx;
import com.agnie.useradmin.persistance.shared.service.PermissionManagerRequest;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.Range;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.Receiver;

@Singleton
public class PermissionsPresenter extends BasePresenter {
	PermissionsView			view;
	PermissionDataFetcher	dataFetcher;
	ClickHandler			permBreadCrumbClickhandler	= new ClickHandler() {

															@Override
															public void onClick(ClickEvent event) {
																appController.getPlaceManager().changePlace(new Place<PlaceToken>(PlaceToken.PERMISSION));
															}
														};

	public boolean go() {
		if (super.go() && appACLProvider.get() != null && appACLProvider.get().check(Permissions.PERMISSION)) {

			BreadCrumbPanel breadCrumbPanel = viewFactory.getBreadCrumbPanel();
			breadCrumbPanel.addBreadCrumb(I18.messages.permissions());
			breadCrumbPanel.getBreadCrumb(1).addClickHandler(permBreadCrumbClickhandler);

			viewFactory.getListMenu().selectTab(ListMenu.Tab.PERMISSION.getIndex());

			RootPanel contentPanel = clientFactory.getRootPanelFactory().getContentPanel();
			HTMLPanel centerPanel = viewFactory.getCenterContentPanel();
			view = viewFactory.getListPermissionsView();
			if (dataFetcher == null) {
				dataFetcher = new PermissionDataFetcher(view);
				AsyncDP<PermissionPx> permissionDP = new AsyncDP<PermissionPx>(dataFetcher);
				view.setDataProvider(permissionDP);
			}
			messagePanel.hide();

			viewFactory.getMenu().getSearchBox().clearSearchImgClkHandlers();
			viewFactory.getMenu().getSearchBox().addSearchImgClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					viewFactory.getListPermissionsView().refreshPage();
				}
			});
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

	public class PermissionDataFetcher extends AbstractDataFetcher<PermissionPx> {
		PermissionsView	view;

		/**
		 * @param view
		 */
		public PermissionDataFetcher(PermissionsView view) {
			super();
			this.view = view;
		}

		@Override
		public void fire(Range range, Receiver<List<PermissionPx>> reciever) {
			PermissionManagerRequest pmr = clientFactory.getRequestFactory().permissionManager();
			AuthLevelWrapper level = (AuthLevelWrapper) viewFactory.getLevelFilter().getSelectedItem();
			if ((viewFactory.getMenu().getSearchBox().getValue() != null) && (!(viewFactory.getMenu().getSearchBox().getValue().isEmpty()))) {
				if (AuthLevel.CONTEXT.equals(level.getLevel())) {
					pmr.getPermissionsByApplication(getPaging(range, view, pmr), AuthLevel.CONTEXT, viewFactory.getMenu().getSearchBox().getValue()).fire(reciever);
				} else {
					pmr.getPermissionsByApplication(getPaging(range, view, pmr), AuthLevel.APPLICATION, viewFactory.getMenu().getSearchBox().getValue()).fire(reciever);
				}
			} else {
				if (AuthLevel.CONTEXT.equals(level.getLevel())) {
					pmr.getPermissionsByApplication(getPaging(range, view, pmr), AuthLevel.CONTEXT, null).fire(reciever);
				} else {
					pmr.getPermissionsByApplication(getPaging(range, view, pmr), AuthLevel.APPLICATION, null).fire(reciever);
				}
			}
		}
	}

	public void savePermission(PermissionPx permPx, PermissionManagerRequest pmr) {
		pmr.save(permPx).fire(new Receiver<PermissionPx>() {

			@Override
			public void onSuccess(PermissionPx response) {
				viewFactory.getListPermissionsView().refreshPage();
				viewFactory.getListPermissionsView().reset();
				messagePanel.setVisible(true);
				messagePanel.setType(MessageType.INFORMATION);
				messagePanel.setMessage(I18.messages.permissionSaved());
			}
		});
	}

	public void deletePermissions(List<String> listToDel) {
		PermissionManagerRequest pmr = clientFactory.getRequestFactory().permissionManager();
		pmr.removePermissionsByIds(listToDel).fire(new Receiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				if (response) {
					viewFactory.getListPermissionsView().refreshPage();
					viewFactory.getListPermissionsView().reset();
					messagePanel.setVisible(true);
					messagePanel.setType(MessageType.INFORMATION);
					messagePanel.setMessage(I18.messages.permissionsDeleted());
				}
			}
		});
	}

	@Override
	public void postRender() {
		view.setDefaultFocus();
	}

}
