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
package com.agnie.useradmin.service.rest.bean;

import java.util.List;

import com.agnie.useradmin.persistance.server.listrequest.PageNSort;

public class PaginatedEntity<E> {

	private PageNSort	pageNSort;
	private List<E>		list;

	public PaginatedEntity() {
	}

	/**
	 * @param pageNSort
	 * @param list
	 */
	public PaginatedEntity(PageNSort pageNSort, List<E> list) {
		super();
		this.pageNSort = pageNSort;
		this.list = list;
	}

	/**
	 * @return the pageNSort
	 */
	public PageNSort getPageNSort() {
		return pageNSort;
	}

	/**
	 * @param pageNSort
	 *            the pageNSort to set
	 */
	public void setPageNSort(PageNSort pageNSort) {
		this.pageNSort = pageNSort;
	}

	/**
	 * @return the list
	 */
	public List<E> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<E> list) {
		this.list = list;
	}

}
