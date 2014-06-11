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
import com.agnie.useradmin.main.client.ui.ManageContextsView;
import com.agnie.useradmin.persistance.client.helper.Permissions;
import com.agnie.useradmin.persistance.shared.proxy.ContextPx;
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
public class ManageContextsPresenter extends BasePresenter {
	ManageContextDataFetcher	dataFetcher;

	public boolean go() {
		if (super.go() && appACLProvider.get() != null && appACLProvider.get().check(Permissions.MANAGE_CONTEXT)) {
			RootPanel contentPanel = clientFactory.getRootPanelFactory().getContentPanel();
			viewFactory.getListMenu().selectTab(ListMenu.Tab.CONTEXT.getIndex());
			HTMLPanel centerPanel = viewFactory.getCenterContentPanel();
			ManageContextsView view = viewFactory.getManageContextsView();
			if (dataFetcher == null) {
				dataFetcher = new ManageContextDataFetcher(view);
				AsyncDP<ContextPx> contextDP = new AsyncDP<ContextPx>(dataFetcher);
				view.setDataProvider(contextDP);
			}
			BreadCrumbPanel breadCrumbPanel = viewFactory.getBreadCrumbPanel();
			breadCrumbPanel.addBreadCrumb(I18.messages.manageContexts());

			viewFactory.getMenu().getSearchBox().clearSearchImgClkHandlers();
			viewFactory.getMenu().getSearchBox().addSearchImgClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					viewFactory.getManageContextsView().refreshPage();
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

	public class ManageContextDataFetcher extends AbstractDataFetcher<ContextPx> {
		ManageContextsView	view;

		public ManageContextDataFetcher(ManageContextsView view) {
			super();
			this.view = view;
		}

		@Override
		public void fire(Range range, Receiver<List<ContextPx>> reciever) {
			ContextManagerRequest cmr = clientFactory.getRequestFactory().contextManager();
			if ((viewFactory.getMenu().getSearchBox().getValue() != null) && (!(viewFactory.getMenu().getSearchBox().getValue().isEmpty()))) {
				cmr.list(getPaging(range, view, cmr), viewFactory.getMenu().getSearchBox().getValue()).fire(reciever);
			} else {
				cmr.list(getPaging(range, view, cmr), null).fire(reciever);
			}
		}
	}

	public void createContext(ContextManagerRequest cmr, ContextPx contextPx) {
		cmr.create(contextPx).fire(new Receiver<ContextPx>() {

			@Override
			public void onSuccess(ContextPx response) {
				messagePanel.show(true);
				messagePanel.setMessage(I18.messages.createContextSucced());
				messagePanel.setType(MessageType.INFORMATION);
				viewFactory.getManageContextsView().refreshPage();
				viewFactory.getManageContextsView().reset();
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
				messagePanel.setType(MessageType.ERROR);
			}
		});
	}

	public void deleteContext(ContextPx context) {
		ContextManagerRequest cmr = clientFactory.getRequestFactory().contextManager();
		cmr.removeContext(context).fire(new Receiver<Boolean>() {

			@Override
			public void onSuccess(Boolean arg0) {
				messagePanel.show(true);
				messagePanel.setMessage(I18.messages.deletContextSucced());
				messagePanel.setType(MessageType.INFORMATION);
				viewFactory.getManageContextsView().refreshPage();
				viewFactory.getManageContextsView().reset();
			}

			@Override
			public void onFailure(ServerFailure failure) {
				messagePanel.show(true);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
				messagePanel.setType(MessageType.ERROR);
			}
		});
	}

	@Override
	public void postRender() {
		viewFactory.getManageContextsView().setDefaultFocus();
	}

}
