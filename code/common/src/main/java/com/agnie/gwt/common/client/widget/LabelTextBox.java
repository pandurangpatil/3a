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

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * LabelTextBox is an extension of GWT TextBox to show label as text field default value(in little bit light color).
 * 
 * @param String
 *            Label
 * 
 */
public class LabelTextBox extends TextBox {
	private static LabelTextBoxResources	resource	= LabelTextBoxResources.INSTANCE;

	static {
		resource.css().ensureInjected();
	}

	private String							label		= new String();

	public LabelTextBox() {
		this(null);
	}

	public LabelTextBox(String label) {

		setLabel(label);
		this.label = label;
		/* TODO:It is not working on some Chromium version so need to check. */
		// super.addFocusHandler(new FocusHandler() {
		//
		// @Override
		// public void onFocus(FocusEvent event) {
		// if (!dirtyFlag) {
		// setText("");
		// }
		// removeStyle();
		// }
		// });

		super.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				String text = getValue();
				if (text.isEmpty()) {
					reset();
				}
			}
		});

		super.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				removeStyle();
			}
		});

	}

	/**
	 * To hide errorPan when this widget getsRemoved from Parent.
	 */
	@Override
	public void onDetach() {
		super.onDetach();// To avoid Uncaught exception<Should only call onAttach when the widget is detached from the
							// browser's document>
		this.errorPan.hide();
		this.reset();
	}

	public void onFirstKeyPress(char character) {
		super.onFirstKeyPress(character);
		super.setText("");
	}

	@Override
	public void setText(String text) {
		if (text != null && !text.isEmpty()) {
			this.dirtyFlag = true;
			super.setText(text);
		}

	}

	/**
	 * To reset textbox with default label.
	 */
	public void reset() {
		super.reset();
		super.setText(getLabel());
		addStyle();
	}

	/**
	 * to get textbox
	 * 
	 * @return
	 */

	public TextBox getTextBox() {
		return this;
	}

	/**
	 * to get ErrorMessage Panel
	 * 
	 * @return
	 */
	public PopupPanel getErrorPan() {
		return this.errorPan;
	}

	protected void removeStyle() {
		super.textBox.removeStyleName("text-field-label");
	}

	protected void addStyle() {
		super.textBox.addStyleName("text-field-label");
	}

	/**
	 * Sets default Label to text field,if end-user focus on text box this label disappears.
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		if (label != null && !("".equals(label))) {
			this.label = label;
			addStyle();
			super.setText(this.label);
		}
	}

	/**
	 * returns default Label set by widget-user
	 * 
	 * @return String label value
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * returns textBox value entered by end-user. if end user don't enter any new value it returns empty.
	 */

	@Override
	public String getValue() {
		String text = super.getValue();
		if (dirtyFlag)
			return text;
		else
			return "";
	}

}
