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

import com.agnie.useradmin.persistance.client.enums.SortOrder;

@RFValueProxy
public class Sort implements Serializable {

	private static final long	serialVersionUID	= 1L;

	private String				column;
	private SortOrder			order				= SortOrder.ASC;

	public Sort() {
	}

	/**
	 * @param column
	 * @param order
	 */
	public Sort(String column, SortOrder order) {
		this.column = column;
		this.order = order;
	}

	/**
	 * @return the column
	 */
	@RFProxyMethod
	public String getColumn() {
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	@RFProxyMethod
	public void setColumn(String column) {
		this.column = column;
	}

	/**
	 * @return the order
	 */
	@RFProxyMethod
	public SortOrder getOrder() {
		return order;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	@RFProxyMethod
	public void setOrder(SortOrder order) {
		this.order = order;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Sort [column=" + column + ", order=" + order + "]";
	}

}
