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

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * Simple DialogBox with all PopupPanel features.
 * <br>Note:Default close button is invisible,visible only if we call method 'addCloseHandler(ClickHandler handler)'
 *
 */
public class DialogBox extends PopupPanel{
	
	public DecoratedPanel decPanel=new DecoratedPanel();
	
	public DialogBox() {
		this.add(decPanel);
	}
	
	public DialogBox(String header,Widget content) {
		this.decPanel.setHeader(header);
		this.decPanel.addContent(content);
		this.add(decPanel);
		this.setGlassEnabled(true);
		this.setGlassStyleName(DecoratedPanel.getResources().css().whiteout());
	}
	
	public void addDialogBoxStyleName(String styleName) {
		this.addStyleName(styleName);
	}
	
	public void addDecPanStyleName(String styleName) {
		this.decPanel.addStyleName(styleName);
	}
	
	public void setHeader(String header){
		decPanel.setHeader(header);
	}
	
	public void addContent(Widget content) {
		decPanel.addContent(content);
	}

	/**
	 * remove an widget from panel content
	 * 
	 * @param content
	 *            widget to remove
	 */
	public void removeContent(Widget content) {
		if (content != null) {
			this.decPanel.contentPan.remove(content);
		}
	}

	/**
	 * clears the content element of decorated panel
	 */
	public void clear() {
		this.decPanel.contentPan.clear();
	}
	
	/**
	 * clickHandler for close button.Also sets close button visible.
	 * 
	 * @param handler
	 *            click handler for close button.
	 */
	public void addCloseHandler(ClickHandler handler) {
		//this.decPanel.close.setVisible(true);
		this.decPanel.addCloseHandler(handler);
	}
}
