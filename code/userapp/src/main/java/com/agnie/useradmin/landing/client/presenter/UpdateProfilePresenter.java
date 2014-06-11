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
package com.agnie.useradmin.landing.client.presenter;

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.gwt.common.client.widget.MessagePanel.MessageType;
import com.agnie.useradmin.landing.client.I18;
import com.agnie.useradmin.landing.client.ui.UpdateProfileView;
import com.agnie.useradmin.persistance.shared.proxy.UserPx;
import com.agnie.useradmin.persistance.shared.service.UserManagerRequest;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

@Singleton
public class UpdateProfilePresenter extends LandingBasePresenter {

	UpdateProfileView	view;

	@Override
	public boolean go() {
		super.go();
		RootPanel contentPanel = clientFactory.getRootPanelFactory().getContentPanel();
		contentPanel.clear();

		HTMLPanel centerPanel = viewFactory.getCenterContentPanel();
		centerPanel.clear();

		view = viewFactory.getUpdateProfileView();
		messagePanel.hide();
		centerPanel.add(messagePanel);
		view.reset();
		centerPanel.add(view);
		contentPanel.add(centerPanel);
		UserManagerRequest umr = clientFactory.getRequestFactory().userManager();
		umr.getUserByUserName(userContext.getCurrentUser().getUserName()).fire(new Receiver<UserPx>() {

			@Override
			public void onSuccess(UserPx response) {

				if (response != null) {
					viewFactory.getUpdateProfileView().setUserDetails(response);
				}
			}

			@Override
			public void onFailure(ServerFailure error) {
				GWT.log("Error:In UpdateProfileViewImpl> fetchPopulateUserDet()");
			}
		});
		return true;
	}

	public void updateUserProfile(UserManagerRequest umr, final UserPx uPxToModify) {
		umr.saveUser(uPxToModify).fire(new Receiver<String>() {

			@Override
			public void onSuccess(String response) {
				setUpdateUserAcc(uPxToModify);
				messagePanel.show(true);
				messagePanel.setMessage(I18.messages.profUpdated());
				messagePanel.setType(MessageType.INFORMATION);
				Window.Location.assign(uaUrlGenerator.getSourceUrl(urlInfo));
			}

			@Override
			public void onFailure(ServerFailure error) {
				messagePanel.show(true);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
				messagePanel.setType(MessageType.ERROR);
			}
		});
	}

	private void setUpdateUserAcc(UserPx uPxToModify) {
		UserAccount ua = new UserAccount();
		if (uPxToModify.getFirstName() != null) {
			ua.setFirstName(uPxToModify.getFirstName());
		}
		if (uPxToModify.getFirstName() != null) {
			ua.setId(uPxToModify.getId());
		}
		if (uPxToModify.getFirstName() != null) {
			ua.setLastName(uPxToModify.getLastName());
		}
		if (uPxToModify.getFirstName() != null) {
			ua.setTitle(uPxToModify.getTitle());
		}
		if (uPxToModify.getFirstName() != null) {
			ua.setUserImgUrl(uPxToModify.getProfileImage());
		}
		if (uPxToModify.getFirstName() != null) {
			ua.setUserName(uPxToModify.getUserName());
		}
		commonViewFactory.getHeaderView().setUserAcc(ua);
	}

	@Override
	public void postRender() {
		view.setDefaultFocus();
	}

}
