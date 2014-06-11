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
package com.agnie.useradmin.persistance.server.listrequest;

import java.io.Serializable;

import open.pandurang.gwt.helper.requestfactory.marker.RFProxyMethod;
import open.pandurang.gwt.helper.requestfactory.marker.RFValueProxy;

@RFValueProxy
public class PageNSort implements Serializable {

	private static final long	serialVersionUID	= 1L;

	private Page				page;
	private Sort				sort;

	public PageNSort() {
	}

	/**
	 * @param page
	 * @param sort
	 */
	public PageNSort(Page page, Sort sort) {
		super();
		this.page = page;
		this.sort = sort;
	}

	/**
	 * @return the page
	 */
	@RFProxyMethod
	public Page getPage() {
		return page;
	}

	/**
	 * @param page
	 *            the page to set
	 */
	@RFProxyMethod
	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * @return the sort
	 */
	@RFProxyMethod
	public Sort getSort() {
		return sort;
	}

	/**
	 * @param sort
	 *            the sort to set
	 */
	@RFProxyMethod
	public void setSort(Sort sort) {
		this.sort = sort;
	}

}
