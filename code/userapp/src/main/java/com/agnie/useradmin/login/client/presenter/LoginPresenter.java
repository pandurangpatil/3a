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
package com.agnie.useradmin.login.client.presenter;

import com.agnie.common.gwt.serverclient.client.enums.Cokie;
import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.gwt.common.client.widget.MessagePanel.MessageType;
import com.agnie.useradmin.login.client.I18;
import com.agnie.useradmin.login.client.ui.LoginView;
import com.agnie.useradmin.persistance.client.enums.RequestStatus;
import com.agnie.useradmin.persistance.client.exception.DomainAuthException;
import com.agnie.useradmin.persistance.client.exception.InvalidDomain;
import com.agnie.useradmin.persistance.client.exception.RegistrationDisabledException;
import com.agnie.useradmin.persistance.client.exception.RequestedException;
import com.agnie.useradmin.persistance.client.exception.UserAlreadyRegistered;
import com.agnie.useradmin.persistance.client.service.AuthenticateServiceAsync;
import com.agnie.useradmin.persistance.client.service.UserProfileServiceAsync;
import com.agnie.useradmin.persistance.client.service.dto.Credential;
import com.agnie.useradmin.session.client.helper.Helper;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LoginPresenter extends BasePresenter implements I18 {
	@Inject
	Helper		helper;
	LoginView	view;

	@Override
	public boolean go() {
		super.go();
		RootPanel contentPanel = clientFactory.getRootPanelFactory().getContentPanel();
		contentPanel.clear();

		HTMLPanel centerPanel = viewFactory.getCenterContentPanel();
		centerPanel.clear();

		view = viewFactory.getLoginView();

		String status = Cookies.getCookie(Cokie.STATUS.getKey());
		Cookies.setCookie(Cokie.STATUS.getKey(), null);
		if (status != null && status.equals(DomainAuthException.class.getName())) {
			String username = Cookies.getCookie(Cokie.USER.getKey());
			Cookies.setCookie(Cokie.USER.getKey(), null);
			if (username == null) {
				username = "";
			}
			view.registerView(username);
		} else if (status != null && status.equals(RequestedException.class.getName())) {
			messagePanel.show(false);
			messagePanel.setType(MessageType.WARNING);
			messagePanel.setMessage(messages.regrequested());
		} else if (status != null && status.equals(RegistrationDisabledException.class.getName())) {
			messagePanel.show(false);
			messagePanel.setType(MessageType.WARNING);
			messagePanel.setMessage(messages.reg_disabled());
		}
		view.initLabels();
		centerPanel.add(messagePanel);
		centerPanel.add(view);
		contentPanel.add(centerPanel);
		return true;
	}

	public void authenticate(final Credential cred, final String salt, final boolean remember) {
		AuthenticateServiceAsync authService = clientFactory.getAuthenticateService();
		authService.authenticate(cred.getUsername(), cred.getPassword(), salt, remember, new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				if (result != null) {

					String domain = Window.Location.getParameter(QueryString.DOMAIN.getKey());
					String source = Window.Location.getParameter(QueryString.SOURCE.getKey());
					if ((domain == null || domain.isEmpty()) && (source == null || source.isEmpty())) {
						// if 'domain' and 'source' query parameters are absent then we can safely redirect the user to
						// usreadmin application.
						Window.Location.assign(uug.getAfterLoginRedirectUrl(params));
					} else {
						validateApplicationAccess(domain, source, cred.getUsername());
					}
				} else {
					messagePanel.show(true);
					messagePanel.setMessage(messages.invalidUserCreds());
					messagePanel.setType(MessageType.ERROR);
					viewFactory.getLoginView().reset();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				LoginView loginView = viewFactory.getLoginView();
				loginView.reset();
				messagePanel.show(false);
				messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
				messagePanel.setType(MessageType.ERROR);
			}
		});
	}

	private void validateApplicationAccess(final String domain, final String source, final String username) {
		UserProfileServiceAsync ups = clientFactory.getUserProfileService();
		ups.validateApplicationAccess(domain, source, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				LoginView loginView = viewFactory.getLoginView();
				boolean reset = false;
				if (caught instanceof DomainAuthException) {
					loginView.clearPassword();
					loginView.registerView(username);
				} else if (caught instanceof InvalidDomain) {
					messagePanel.show(false);
					messagePanel.setMessage(messages.invalid_domain());
					messagePanel.setType(MessageType.ERROR);
					reset = true;
				} else if (caught instanceof RequestedException) {
					messagePanel.show(false);
					messagePanel.setMessage(messages.regrequested());
					messagePanel.setType(MessageType.WARNING);
					reset = true;
				} else if (caught instanceof RegistrationDisabledException) {
					messagePanel.show(false);
					messagePanel.setMessage(messages.reg_disabled());
					messagePanel.setType(MessageType.WARNING);
					reset = true;
				} else {
					messagePanel.show(false);
					messagePanel.setMessage(com.agnie.useradmin.common.client.I18.messages.internal_server_error());
					messagePanel.setType(MessageType.ERROR);
					reset = true;
				}
				if (reset) {
					helper.logout();
				}
				loginView.reset();
			}

			@Override
			public void onSuccess(String result) {
				if (result != null) {
					Window.Location.assign(uug.getAfterLoginRedirectUrl(params, domain, result));
				} else {
					LoginView loginView = viewFactory.getLoginView();
					loginView.clearPassword();
					loginView.registerView(username);
					viewFactory.getLoginView().reset();
				}

			}
		});
	}

	public void register(final Credential cred, final String salt) {
		UserProfileServiceAsync ups = clientFactory.getUserProfileService();
		ups.registerWithNewDomain(cred, salt, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				messagePanel.show(false);
				String message = com.agnie.useradmin.common.client.I18.messages.internal_server_error();
				if (caught instanceof UserAlreadyRegistered) {
					UserAlreadyRegistered uar = (UserAlreadyRegistered) caught;

					if (RequestStatus.REQUESTED.name().equals(uar.getStatus())) {
						messagePanel.setType(MessageType.WARNING);
						messagePanel.setMessage(messages.regrequested());
					} else if (RequestStatus.DISABLED.name().equals(uar.getStatus())) {
						messagePanel.setType(MessageType.WARNING);
						messagePanel.setMessage(messages.reg_disabled());
					} else {
						messagePanel.setType(MessageType.ERROR);
					}
				}
				messagePanel.setMessage(message);
				helper.logout();
			}

			@Override
			public void onSuccess(String result) {
				if (result != null && !result.isEmpty()) {
					Window.Location.assign(uug.getAfterLoginRedirectUrl(params, Window.Location.getParameter(QueryString.DOMAIN.getKey()), result));
				} else {
					messagePanel.show(false);
					messagePanel.setMessage(messages.request_submitted());
					messagePanel.setType(MessageType.INFORMATION);
					// TODO : Need to decide what should be the next step.
				}
			}
		});
	}

	@Override
	public void postRender() {
		view.setDefaultFocus();
	}
}
