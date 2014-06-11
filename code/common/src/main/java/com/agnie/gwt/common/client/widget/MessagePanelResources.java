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

public interface MessagePanelResources extends ClientBundle {
	public static MessagePanelResources	INSTANCE	= GWT.create(MessagePanelResources.class);

	@Source("MessagePanelCss.css")
	MessagePanelCss css();

	@Source("error.png")
	ImageResource error();

	@Source("info.png")
	ImageResource info();

	@Source("warning.png")
	ImageResource warning();

	@Source("cross2.png")
	ImageResource closeBtn();

}
