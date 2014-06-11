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
package com.agnie.useradmin.main.client.presenter.sahered.ui;

import com.agnie.useradmin.persistance.shared.proxy.PermissionPx;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;

public class PermissionsCell extends AbstractCell<PermissionPx>{
	interface MyUiRenderer extends UiRenderer {
		void render(SafeHtmlBuilder sb, PermissionPx userPermDet);
	}

	private static MyUiRenderer	renderer	= GWT.create(MyUiRenderer.class);
	
	
	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, PermissionPx value, SafeHtmlBuilder sb) {
		renderer.render(sb, value);
	}

}
