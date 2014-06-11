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

public interface DecoratedPanelResources extends ClientBundle {
	public static DecoratedPanelResources	INSTANCE	= GWT.create(DecoratedPanelResources.class);

	@Source("DecoratedPanelCss.css")
	DecoratedPanelCss css();

	@Source("cross2.png")
	ImageResource closeBtn();

	@Source("min.png")
	ImageResource minBtn();

	@Source("max.png")
	ImageResource maxBtn();

}
