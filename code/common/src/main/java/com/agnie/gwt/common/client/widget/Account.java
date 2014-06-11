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
package com.agnie.gwt.common.client.widget;

import javax.inject.Named;

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.common.gwt.serverclient.client.helper.URLGenerator;
import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.common.gwt.serverclient.client.injector.CommonServerClientModule;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Account widget.To show users account detail.
 * 
 */
@Singleton
public class Account extends Composite {
	private static AccountResources	resource	= AccountResources.INSTANCE;

	static {
		resource.css().ensureInjected();
	}

	interface MyUiBinder extends UiBinder<Widget, Account> {
	}

	private static MyUiBinder	uiBinder		= GWT.create(MyUiBinder.class);

	@UiField
	HTMLPanel					accTitlePan;
	@UiField
	SpanElement					accTitle;
	@UiField
	FocusPanel					accDropPan;
	@UiField
	DivElement					accDropPanBody;
	@UiField
	Image						accImg;
	@UiField
	Image						accUserImg;
	@UiField
	HTMLPanel					changePass;
	@UiField
	HTMLPanel					modify;
	@UiField
	HTMLPanel					logout;

	@Inject
	URLGenerator				urlGenerator;
	@Inject
	@Named(CommonServerClientModule.CURRENT_APP_DOMAIN)
	String						appDomain;
	@Inject
	private URLInfo				urlinfo;

	protected HTMLPanel			container;
	protected boolean			visibleDropPan	= false;
	private boolean				init			= false;

	@Inject
	public Account() {
		this(resource.css().accPan());
	}

	HideClickHandler	hidePanelHandler	= new HideClickHandler();
	UserAccount			userAcc;

	private Account(String styleClassName) {
		container = (HTMLPanel) uiBinder.createAndBindUi(this);
		container.addStyleName(styleClassName);
		initWidget(container);

		accImg.setUrl(GWT.getModuleBaseURL() + "images/transparent.png");
		setUserImageResource(resource.person());
		accTitlePan.sinkEvents(Event.ONCLICK);// 'enables' click events for the
												// HtmlPanel
		accTitlePan.addHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (visibleDropPan) {
					hide();
				} else {
					show();
				}
			}
		}, ClickEvent.getType());

	}

	@UiHandler("accDropPan")
	void handleBlurDiv(BlurEvent be) {
		this.hide();
	}

	private void setAccName(String title) {
		accTitle.setInnerText(title);
	}

	private void setUserImageResource(ImageResource resource) {
		accUserImg.setResource(resource);
	}

	private void setUserImageUrl(String imageUrl) {
		accUserImg.setUrl(imageUrl);
	}

	private void init() {
		changePass.sinkEvents(Event.ONCLICK);
		changePass.addHandler(hidePanelHandler, ClickEvent.getType());
		changePass.addHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String changePassUrl = urlGenerator.getChangePassUrl(urlinfo, appDomain, urlinfo.getParameter(QueryString.GWT_DEV_MODE.getKey()));
				Window.Location.assign(changePassUrl);
			}
		}, ClickEvent.getType());

		modify.sinkEvents(Event.ONCLICK);
		modify.addHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String updateProfileUrl = urlGenerator.getUpdateProfUrl(urlinfo, appDomain, urlinfo.getParameter(QueryString.GWT_DEV_MODE.getKey()));
				Window.Location.assign(updateProfileUrl);
			}
		}, ClickEvent.getType());
		modify.addHandler(hidePanelHandler, ClickEvent.getType());
		init = true;
	}

	public void setUserAcc(UserAccount userAcc) {
		if (userAcc != null) {
			this.userAcc = userAcc;
			String title = userAcc.getFirstName() + " " + userAcc.getLastName();
			if (title.trim().isEmpty()) {
				title = userAcc.getUserName();
			}
			setAccName(title);
			if (userAcc.getUserImgUrl() != null && !(userAcc.getUserImgUrl().isEmpty())) {
				setUserImageUrl(userAcc.getUserImgUrl());
				accUserImg.addStyleName(resource.css().accUserImg());
			} else {
				setUserImageResource(resource.person());
			}
			if (!init) {
				init();
			}
		}
	}

	private void hide() {
		accDropPan.removeStyleName(resource.css().accDropPanVisible());
		visibleDropPan = false;
	}

	private void show() {
		accDropPan.addStyleName(resource.css().accDropPanVisible());
		accDropPan.setFocus(true);
		visibleDropPan = true;
	}

	public void addChangePassClickHandler(ClickHandler handler) {
		changePass.addHandler(handler, ClickEvent.getType());
	}

	public void addModifyClickHandler(ClickHandler handler) {
		modify.addHandler(handler, ClickEvent.getType());
	}

	public void addLogoutClickHandler(ClickHandler handler) {
		logout.sinkEvents(Event.ONCLICK);
		logout.addHandler(handler, ClickEvent.getType());
		logout.addHandler(hidePanelHandler, ClickEvent.getType());
	}

	public void setHeight(String height) {
		container.setHeight(height);
	}

	public void setSize(String width, String height) {
		container.setSize(width, height);
	}

	public static AccountResources getResources() {
		return resource;
	}

	private class HideClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			hide();
		}
	}

}
