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
package com.agnie.useradmin.common.client.ui;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UACommonViewFactory {
	/*
	 * TODO: This need to be converted to have platform specific view factory.
	 */
	/**
	 * Views will be instantiated only on its first time usage.
	 */
	@Inject
	private HeaderView	headerView;
	@Inject
	private FooterView	footerView;

	/**
	 * @return the headerView
	 */
	public HeaderView getHeaderView() {
		return headerView;
	}

	/**
	 * @return the footerView
	 */
	public FooterView getFooterView() {
		return footerView;
	}

}
