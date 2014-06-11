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

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * LabelPasswordBox is an 'extended GWT LabelTextBox' to show label(e.g.'password')<br>
 * as text field default value(in little bit light color).<br>
 * <br>
 * on focus it switch to password field until end user enter valid password <br>
 * other than if end user enter invalid or null it switches to text field
 */
public class LabelPasswordBox extends LabelTextBox {
	private static LabelTextBoxResources	resource		= LabelTextBoxResources.INSTANCE;

	boolean									passwordmode	= false;
	static {
		resource.css().ensureInjected();
	}

	public LabelPasswordBox() {
		this(null);
	}

	public LabelPasswordBox(String label) {

		super.setLabel(label);

		/*
		 * NOTE: Below code to detect firefox browser and make use of focus event to change type of input field to
		 * password. Is a temporary fix, ideal way to handle the browser specific handling is to make use of deferred
		 * binding. The issue is when we make use of key press handler to change the type first key is not getting
		 * recognised on firefox because of some reason. And if we think of using focus handler for all browsers. Focus
		 * handler fails to convert it to password box on chrome.
		 */
		String userAgent = Navigator.getUserAgent();
		if (userAgent.contains("Firefox")) {
			super.addFocusHandler(new FocusHandler() {
				@Override
				public void onFocus(FocusEvent arg0) {
					changeTypeToPass();
					removeStyle();
				}
			});
		} else {
			super.addKeyPressHandler(new KeyPressHandler() {

				@Override
				public void onKeyPress(KeyPressEvent event) {
					if (!passwordmode) {
						Scheduler.get().scheduleDeferred(new Command() {
							public void execute() {
								changeTypeToPass();
								removeStyle();
							}
						});
						passwordmode = true;
					}
				}
			});
		}
		super.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent arg0) {
				String value = getValue();
				if (value.isEmpty()) {
					changeTypeToText();
					addStyle();
					passwordmode = false;
				} else {
					changeTypeToPass();
					removeStyle();
				}
			}
		});

	}

	/**
	 * To reset field with default label.
	 */
	public void reset() {
		super.reset();
		changeTypeToText();
	}

	/**
	 * to get ErrorMessage Panel
	 * 
	 * @return
	 */
	public PopupPanel getErrorPan() {
		return this.errorPan;
	}

	/**
	 * changes type of text field to password field
	 */
	private void changeTypeToPass() {
		super.textBox.getElement().setAttribute("type", "password");
	}

	/**
	 * To resume to text field / changes type of password field to text field
	 */
	private void changeTypeToText() {
		super.textBox.getElement().setAttribute("type", "text");
	}
}
