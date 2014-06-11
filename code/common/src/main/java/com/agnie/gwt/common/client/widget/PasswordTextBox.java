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

/**
 * A type of widget PasswordBox extends existing <br>
 * TextBox 'widget with error message feature'.
 * 
 * 
 */
public class PasswordTextBox extends TextBox {

	public PasswordTextBox() {
		super.textBox.getElement().setAttribute("type", "password");
	}

}
