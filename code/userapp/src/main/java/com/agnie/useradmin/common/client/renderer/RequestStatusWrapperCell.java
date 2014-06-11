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
package com.agnie.useradmin.common.client.renderer;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;

public class RequestStatusWrapperCell extends AbstractCell<com.agnie.useradmin.common.client.helper.RequestStatusWrapper> {

	interface MyUiRenderer extends UiRenderer {
		void render(SafeHtmlBuilder sb, com.agnie.useradmin.common.client.helper.RequestStatusWrapper string);
	}

	private static MyUiRenderer	renderer	= GWT.create(MyUiRenderer.class);

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, com.agnie.useradmin.common.client.helper.RequestStatusWrapper value, SafeHtmlBuilder sb) {
		renderer.render(sb, value);
	}
}
