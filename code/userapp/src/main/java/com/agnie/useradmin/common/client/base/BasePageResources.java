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
package com.agnie.useradmin.common.client.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface BasePageResources extends ClientBundle {

	public static BasePageResources	INSTANCE	= GWT.create(BasePageResources.class);

	@Source("BasePageCss.css")
	BasePageCss css();

	@Source("logo.png")
	ImageResource logo();

	@Source("help_icon.png")
	ImageResource helpImage();

	@Source("powerd-by.png")
	ImageResource poweredBy();

	@Source("comodo.png")
	ImageResource comodo();
}
