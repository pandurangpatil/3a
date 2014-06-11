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

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;

public class Loader extends PopupPanel {
	static {
		LoaderResources.INSTANCE.css().ensureInjected();
	}

	public Loader() {
		this(LoaderResources.INSTANCE.defaultLoader());
	}

	public Loader(ImageResource resource) {
		Image img = new Image(resource);
		setAnimationEnabled(true);
		setWidget(img);
		setGlassStyleName(LoaderResources.INSTANCE.css().grayout());
		setGlassEnabled(true);
		setModal(true);
		center();
	}
}
