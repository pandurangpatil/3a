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

public interface WizardBarResources extends ClientBundle {

	public static WizardBarResources	INSTANCE	= GWT.create(WizardBarResources.class);

	@Source("WizardBarCss.css")
	WizardBarCss css();

	@Source("inactive.png")
	ImageResource navButton();

	@Source("current_btn.png")
	ImageResource navCurrentButton();

	@Source("done.png")
	ImageResource navDoneButton();

	@Source("last_done.png")
	ImageResource navLastDoneButton();

	@Source("last_transparent.png")
	ImageResource lastButton();

}
