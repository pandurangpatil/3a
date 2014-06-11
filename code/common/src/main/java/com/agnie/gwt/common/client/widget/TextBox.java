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
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * A type of widget TextBox extends existing TextBox with error message feature.
 * 
 * 
 */
public class TextBox extends Composite {
	private static TextBoxResources	resource	= TextBoxResources.INSTANCE;

	static {
		resource.css().ensureInjected();
	}

	interface MyUiBinder extends UiBinder<Widget, TextBox> {
	}

	private static MyUiBinder				uiBinder		= GWT.create(MyUiBinder.class);

	@UiField
	com.google.gwt.user.client.ui.TextBox	textBox;

	protected HTMLPanel						container;
	protected boolean						dirtyFlag		= false;

	private Timer							timer			= null;
	private static final int				TIMEOUT			= 20000;

	final PopupPanel						errorPan		= new PopupPanel();
	private LeftErrorPan					leftErrorPan	= new LeftErrorPan();

	public TextBox() {

		container = (HTMLPanel) uiBinder.createAndBindUi(this);
		initWidget(container);

		errorPan.add(leftErrorPan);

		textBox.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (!dirtyFlag) {
					onFirstKeyPress(event.getCharCode());
				}
				dirtyFlag = true;
			}
		});
		timer = new Timer() {
			public void run() {
				TextBox.this.setErrorMessVisible(false);
			}
		};
	}

	public void onFirstKeyPress(char character) {
		setErrorMessVisible(false);
	}

	/**
	 * To reset textBox to default values.
	 */
	public void reset() {
		dirtyFlag = false;
		setErrorMessVisible(false);
	}

	/**
	 * sets whether this widget is enabled or not
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		textBox.setEnabled(enabled);
	}

	/**
	 * sets error-message to visible or not
	 * 
	 * @param visible
	 */
	public void setErrorMessVisible(boolean visible) {
		if (visible) {
			errorPan.setVisible(visible);
			textBox.addStyleName("text-box-error");
		} else {
			errorPan.setVisible(false);
			textBox.removeStyleName("text-box-error");
		}
	}

	/**
	 * To set error message.
	 * 
	 * @param message
	 * @param autoHide
	 *            To autohide the error message.
	 */
	public void setErrorMessage(String message, boolean autoHide) {
		setErrorMessage(message, autoHide, TIMEOUT);
	}

	/**
	 * To set error message.
	 * 
	 * @param message
	 * @param autoHide
	 *            To autohide the error message in given timeout.
	 */
	public void setErrorMessage(String message, boolean autoHide, int timeout) {
		this.leftErrorPan.message.setText(message);
		setErrorMessVisible(true);
		if (autoHide) {
			timer.schedule(timeout);
		}
		this.leftErrorPan.close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setErrorMessVisible(false);
			}
		});
		setErrorPanPositionLeft();
	}

	private void setErrorPanPositionLeft() {
		errorPan.setPopupPositionAndShow(new PositionCallback() {

			public void setPosition(int offsetWidth, int offsetHeight) {
				int x = 0, y = 0;

				final int widgetX = TextBox.this.textBox.getAbsoluteLeft();
				final int widgetY = TextBox.this.textBox.getAbsoluteTop();

				x = widgetX - offsetWidth - 5;// -5 is for errorPan triangle
				y = widgetY - 7; // -7 is for errorPan triangle.

				errorPan.setPopupPosition(x, y);
			}
		});
	}

	/**
	 * sets error panel width
	 * 
	 * @param width
	 */
	public void setErrorPanWidth(int width) {
		int messSpanwidth = 0;
		errorPan.setWidth(width + "px");
		messSpanwidth = width - 60;
		String messSpanWidthStr = messSpanwidth + "px";
		this.leftErrorPan.message.setWidth(messSpanWidthStr);
	}

	/**
	 * sets errorPan height
	 * 
	 * @param height
	 */
	public void setErrorPanHeight(int height) {
		errorPan.setHeight(height + "px");
	}

	/**
	 * sets text in textbox
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.textBox.setText(text);
	}

	public void addBlurHandler(BlurHandler handler) {
		this.textBox.addBlurHandler(handler);
	}

	public void addClickHandler(ClickHandler handler) {
		this.textBox.addClickHandler(handler);
	}

	public void addFocusHandler(FocusHandler handler) {
		this.textBox.addFocusHandler(handler);
	}

	public void addKeyPressHandler(KeyPressHandler handler) {
		this.textBox.addKeyPressHandler(handler);
	}

	public void addKeyUpHandler(KeyUpHandler handler) {
		this.textBox.addKeyUpHandler(handler);
	}

	public void addKeyDownHandler(KeyDownHandler handler) {
		this.textBox.addKeyDownHandler(handler);
	}

	public void addStyleName(String style) {
		this.container.addStyleName(style);
	}

	public int getMaxLength() {
		return this.textBox.getMaxLength();
	}

	public String getText() {
		return this.textBox.getText();
	}

	public String getValue() {
		return this.textBox.getValue();
	}

	public void removeStyleName(String style) {
		this.container.removeStyleName(style);
	}

	public void setFocus(boolean focused) {
		this.textBox.setFocus(focused);
	}

	public void setHeight(String height) {
		this.textBox.setHeight(height);
	}

	public void setPixelSize(int width, int height) {
		this.textBox.setPixelSize(width, height);
	}

	public void setSize(String width, String height) {
		this.textBox.setSize(width, height);
	}

	public void setMaxLength(int length) {
		this.textBox.setMaxLength(length);
	}

	public void setStyleName(String style) {
		this.container.setStyleName(style);
	}

	public String getStyleName() {
		return this.container.getStyleName();
	}

	public void setValue(String value) {
		this.textBox.setValue(value);
	}

	/**
	 * set text box visible or not
	 */
	public void setVisible(boolean visible) {
		this.container.setVisible(visible);
	}

	/**
	 * set TextBox width
	 */
	public void setWidth(String width) {
		this.textBox.setWidth(width);
	}

	public static TextBoxResources getResources() {
		return resource;
	}

	@UiFactory
	public static String getBasePath() {
		return GWT.getModuleBaseURL();
	}

}
