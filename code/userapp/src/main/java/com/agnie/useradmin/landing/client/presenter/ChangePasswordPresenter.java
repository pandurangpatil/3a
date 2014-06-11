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

import com.agnie.gwt.common.client.widget.MessagePanel.MessageType;
import com.agnie.useradmin.landing.client.I18;
import com.agnie.useradmin.landing.client.ui.ChangePasswordView;
import com.agnie.useradmin.persistance.shared.service.UserManagerRequest;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

@Singleton
public class ChangePasswordPresenter extends LandingBasePresenter {

	ChangePasswordView	view;

	@Override
	public boolean go() {
		super.go();
		RootPanel contentPanel = clientFactory.getRootPanelFactory().getContentPanel();
		contentPanel.clear();

		HTMLPanel centerPanel = viewFactory.getCenterContentPanel();
		centerPanel.clear();

		view = viewFactory.getChangePasswordView();
		messagePanel.hide();
		centerPanel.add(messagePanel);
		view.reset();
		centerPanel.add(view);
		contentPanel.add(centerPanel);
		return true;
	}

	public void resetPassword(String oldPass, String newPass) {
		final UserManagerRequest umr = clientFactory.getRequestFactory().userManager();
		umr.resetPassword(userContext.getCurrentUser().getId(), oldPass, newPass).fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				messagePanel.show(true);
				messagePanel.setMessage(I18.messages.passwordResetSucced());
				messagePanel.setType(MessageType.INFORMATION);
				Window.Location.assign(uaUrlGenerator.getSourceUrl(urlInfo));
			}

			@Override
			public void onFailure(ServerFailure error) {
				if (error.getExceptionType().equals("com.agnie.useradmin.persistance.client.exception.OldPasswordDontMatch")) {
					messagePanel.show(true);
					messagePanel.setMessage(I18.messages.existPassDontMatch());
					messagePanel.setType(MessageType.ERROR);
				} else {
					messagePanel.show(true);
					messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
					messagePanel.setType(MessageType.ERROR);
				}
			}
		});
	}

	@Override
	public void postRender() {
		view.setDefaultFocus();
	}

}
