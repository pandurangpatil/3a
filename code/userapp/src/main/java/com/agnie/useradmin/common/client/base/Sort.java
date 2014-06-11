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
package com.agnie.useradmin.common.client.base;

import com.agnie.useradmin.persistance.client.enums.SortOrder;

public class Sort {
	private String		sortColumn;
	private SortOrder	order;

	/**
	 * @param sortColumn
	 * @param order
	 */
	public Sort(String sortColumn, SortOrder order) {
		super();
		this.sortColumn = sortColumn;
		this.order = order;
	}

	/**
	 * @return the sortColumn
	 */
	public String getSortColumn() {
		return sortColumn;
	}

	/**
	 * @param sortColumn
	 *            the sortColumn to set
	 */
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	/**
	 * @return the order
	 */
	public SortOrder getOrder() {
		return order;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setOrder(SortOrder order) {
		this.order = order;
	}

}
