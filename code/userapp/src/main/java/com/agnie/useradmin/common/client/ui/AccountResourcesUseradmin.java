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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface AccountResourcesUseradmin extends ClientBundle {
	public static AccountResourcesUseradmin	INSTANCE	= GWT.create(AccountResourcesUseradmin.class);

	@Source("AccountCss.css")
	CssResource css();

	@Source("AccountCss.css")
	AccountCss cssAcc();

	@Source("arrowWhite.png")
	ImageResource arrowImg();

	@Source("arrowDark.png")
	ImageResource arrowDarkImg();

	@Source("acc-triangle.png")
	ImageResource accTriangle();
}
