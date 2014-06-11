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
package com.agnie.useradmin.persistance.client.enums;

import java.io.Serializable;

import com.agnie.common.gwt.serverclient.client.renderer.Title;

/**
 * There is a corresponding Language enum in ServerClient client module of Commons project. If you make some changes here you need to also
 * copy the same changes over there also. We had to maintain these enums separately as there is an issue in
 * mvn-helper-plugin which generates RequestFactory interfaces.
 * 
 */
public enum Language implements Serializable, Title {
	MARATHI("mr", "मराठी"), HINDI("hi", "हिन्दी"), ENGLISH("en", "English");
	private String	code;
	private String	label;

	private Language(String code, String label) {
		this.code = code;
		this.label = label;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	@Override
	public String getTitle() {
		return label;
	}
}
