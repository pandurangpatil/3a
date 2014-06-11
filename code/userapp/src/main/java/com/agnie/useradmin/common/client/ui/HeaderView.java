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
package com.agnie.useradmin.common.client.ui;

import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.common.gwt.serverclient.client.helper.URLInfo;
import com.agnie.gwt.common.client.widget.Account;
import com.agnie.useradmin.persistance.client.helper.UserAdminURLGenerator;
import com.agnie.useradmin.session.client.helper.UserContext;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class HeaderView extends Composite {
	@Inject
	private URLInfo								urlinfo;
	@Inject
	private UserContext							userContext;

	private static AccountResourcesUseradmin	resource	= AccountResourcesUseradmin.INSTANCE;

	static {
		resource.css().ensureInjected();
	}

	public static interface HeaderStyle extends CssResource {
		String headerimage();
	}

	interface MyUiBinder extends UiBinder<Widget, HeaderView> {
	}

	private static MyUiBinder	uiBinder	= GWT.create(MyUiBinder.class);

	@UiField
	HTMLPanel					accPan;
	@UiField
	HTMLPanel					headerImage;
	@UiField
	HeaderStyle					style;
	@Inject
	Account						acc;

	boolean						init		= false;

	// Account acc = new Account(resource.cssAcc().accPanUseradmin());

	public HeaderView() {
		initWidget(uiBinder.createAndBindUi(this));
		headerImage.addStyleName("header-img");
	}

	public void init() {
		accPan.add(acc);
		acc.addLogoutClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				userContext.logoutUser(urlinfo, UserAdminURLGenerator.USERADMIN, urlinfo.getParameter(QueryString.GWT_DEV_MODE.getKey()));
			}
		});
		setAccPanVisible(false);
		init = true;
	}

	public void setUserAcc(UserAccount userAcc) {
		if (!init) {
			init();
		}
		this.acc.setUserAcc(userAcc);
	}

	public void setAccPanVisible(boolean visible) {
		this.accPan.setVisible(visible);
	}

	public void setHeaderImg(String url) {
		if (url != null && !url.isEmpty()) {
			Image img = new Image(url);
			img.setStyleName(style.headerimage());
			headerImage.removeStyleName("header-img");
			headerImage.add(img);
		}
	}
}
