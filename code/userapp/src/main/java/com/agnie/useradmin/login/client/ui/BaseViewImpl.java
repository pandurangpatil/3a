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
/**
 * 
 */
package com.agnie.useradmin.login.client.ui;

import com.agnie.common.gwt.serverclient.client.enums.Language;
import com.agnie.common.gwt.serverclient.client.helper.QueryStringProcessor;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 */
public abstract class BaseViewImpl extends Composite {

	@UiFactory
	TextBox getLocalizedTextBox() {
		TextBox tb = new TextBox();
		if (Language.HINDI.getCode().equals(QueryStringProcessor.getCurrentLocale()) || Language.MARATHI.getCode().equals(QueryStringProcessor.getCurrentLocale())) {
			// tb.getElement().setAttribute("quillpad", "true");
		}
		return tb;
	}

	@UiFactory
	TextArea getLocalizedTextArea() {
		TextArea ta = new TextArea();
		if (Language.HINDI.getCode().equals(QueryStringProcessor.getCurrentLocale()) || Language.HINDI.getCode().equals(QueryStringProcessor.getCurrentLocale())) {
			// ta.getElement().setAttribute("quillpad", "true");
		}
		return ta;
	}
}
