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
package com.agnie.useradmin.contextmgr.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface CtxMgrPageResources extends ClientBundle {

	public static CtxMgrPageResources	INSTANCE	= GWT.create(CtxMgrPageResources.class);

	@Source("CtxMgrPageCss.css")
	CtxMgrPageCss css();
	
}
