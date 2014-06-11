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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface AccountResources extends ClientBundle {
	public static AccountResources	INSTANCE	= GWT.create(AccountResources.class);

	@Source("AccountCss.css")
	AccountCss css();

	@Source("arrow.png")
	ImageResource arrowImg();

	@Source("arrowDark.png")
	ImageResource arrowDarkImg();

	@Source("person.png")
	ImageResource person();

	@Source("acc-triangle-silver.png")
	ImageResource accTriangleSilver();
	
	@Source("acc-triangle.png")
	ImageResource accTriangle();
}
