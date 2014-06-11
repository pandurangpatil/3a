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
package com.agnie.common.gwt.serverclient.client.enums;

public enum Action {
	LIST("list"), VIEW("view"), NEW("new"), EDIT("edit");
	private String	action;

	private Action(String action) {
		this.action = action;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
}
