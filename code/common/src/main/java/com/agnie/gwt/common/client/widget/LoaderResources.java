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

public interface LoaderResources extends ClientBundle {
	public static LoaderResources	INSTANCE	= GWT.create(LoaderResources.class);

	@Source("getting-synced.gif")
	ImageResource gettingSynced();

	@Source("loader.gif")
	ImageResource defaultLoader();

	@Source("communicating.gif")
	ImageResource communicating();

	@Source("LoaderCss.css")
	LoaderCss css();
}
