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
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * a common widget to display messages like info, error and warning.
 * 
 * 
 */
@Singleton
public class MessagePanel extends Composite {
	private static MessagePanelResources	resource	= MessagePanelResources.INSTANCE;

	static {
		resource.css().ensureInjected();
	}

	interface MyUiBinder extends UiBinder<Widget, MessagePanel> {
	}

	private static MyUiBinder	uiBinder			= GWT.create(MyUiBinder.class);

	protected HTMLPanel			container;
	@UiField
	HTMLPanel					messagePan;
	@UiField
	ImageElement				img;
	@UiField
	HTML						message;
	@UiField
	Anchor						close;
	public String				type;
	private Timer				timer				= null;
	private static final int	TIMEOUT				= 20000;
	private String				prvMessagePanStyle	= "";
	private String				prvImgStyle			= "";

	public MessagePanel() {
		container = (HTMLPanel) uiBinder.createAndBindUi(this);
		initWidget(container);

		close.addStyleName(resource.css().closeBtn());
		close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				container.setVisible(false);
			}
		});

		this.hide();

		timer = new Timer() {
			public void run() {
				MessagePanel.this.hide();
			}
		};
	}

	/**
	 * To show the MessagePanel
	 * 
	 * @param milisecond
	 *            delay to hide the MessagePanel
	 * @param autoHide
	 */

	public void show(int milisecond, boolean autoHide) {

		this.container.setVisible(true);
		if (autoHide) {
			timer.schedule(milisecond);
		}
	}

	public void show(boolean autoHide) {
		show(TIMEOUT, autoHide);
	}

	/**
	 * To Hide the MessagePanel
	 */
	public void hide() {
		this.container.setVisible(false);
	}

	/**
	 * 
	 * enum to set Message type Error Warning Information etc.
	 * 
	 */
	public static enum MessageType {
		ERROR("Error_Message"), WARNING("Warning_Message"), INFORMATION("Information_Message");

		private String	key;

		private MessageType(String key) {
			this.key = key;
		}

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}
	}

	/**
	 * sets Message in Message Panel.
	 * 
	 * @param String
	 *            message.
	 */
	public void setMessage(String message) {
		this.message.setText(message);
	}

	/**
	 * sets Message in MessagePanel
	 * 
	 * @param html
	 */

	public void setMessage(SafeHtml html) {
		this.message.setHTML(html);
	}

	/**
	 * sets height of Message Panel.
	 */
	public void setHeight(String height) {
		container.setHeight(height);
	}

	/**
	 * sets width of Message Panel.
	 */
	public void setWidth(String width) {
		container.setWidth(width);
	}

	/**
	 * sets type of message like ERROR,WARNING,INFO. It overrides existing style
	 * 
	 * @param enum
	 *            MessageType
	 */
	public void setType(MessageType mt) {

		switch (mt) {

		case ERROR: {

			if (prvMessagePanStyle != null && !(prvMessagePanStyle.isEmpty())) {
				messagePan.removeStyleName(prvMessagePanStyle);
			}
			prvMessagePanStyle = resource.css().errorMessagePan();
			messagePan.addStyleName(prvMessagePanStyle);

			if (prvImgStyle != null && !(prvImgStyle.isEmpty())) {
				img.removeClassName(prvImgStyle);
			}
			prvImgStyle = resource.css().error();
			img.addClassName(prvImgStyle);
		}
			break;
		case WARNING: {
			if (prvMessagePanStyle != null && !(prvMessagePanStyle.isEmpty())) {
				messagePan.removeStyleName(prvMessagePanStyle);
			}
			prvMessagePanStyle = resource.css().warningMessagePan();
			messagePan.addStyleName(prvMessagePanStyle);

			if (prvImgStyle != null && !(prvImgStyle.isEmpty())) {
				img.removeClassName(prvImgStyle);
			}
			prvImgStyle = resource.css().warning();
			img.addClassName(prvImgStyle);

		}
			break;
		case INFORMATION: {
			if (prvMessagePanStyle != null && !(prvMessagePanStyle.isEmpty())) {
				messagePan.removeStyleName(prvMessagePanStyle);
			}
			prvMessagePanStyle = resource.css().infoMessagePan();
			messagePan.addStyleName(prvMessagePanStyle);
			if (prvImgStyle != null && !(prvImgStyle.isEmpty())) {
				img.removeClassName(prvImgStyle);
			}
			prvImgStyle = resource.css().info();
			img.addClassName(prvImgStyle);
		}
			break;
		}
	}

	public static MessagePanelResources getResources() {
		return resource;
	}

	@UiFactory
	public static String getBasePath() {
		return GWT.getModuleBaseURL();
	}

}
