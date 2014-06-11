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

import javax.xml.bind.annotation.XmlTransient;

import open.pandurang.gwt.helper.requestfactory.marker.RFProxyMethod;
import open.pandurang.gwt.helper.requestfactory.marker.RFValueProxy;

@RFValueProxy
public class Page implements Serializable {

	private static final long	serialVersionUID	= 1L;
	private int					startIndex;
	private int					pageSize;
	private int					totalNoOfRecords;

	public Page() {
	}

	/**
	 * @param startIndex
	 * @param pageSize
	 */
	public Page(int startIndex, int pageSize) {
		super();
		this.startIndex = startIndex;
		this.pageSize = pageSize;
	}

	/**
	 * @param startIndex
	 *            the startIndex to set
	 */
	@RFProxyMethod
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @return the startIndex
	 */
	@RFProxyMethod
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @return the pageSize
	 */
	@RFProxyMethod
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	@RFProxyMethod
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the totalNoOfRecords
	 */
	@XmlTransient
	public int getTotalNoOfRecords() {
		return totalNoOfRecords;
	}

	/**
	 * @param totalNoOfRecords
	 *            the totalNoOfRecords to set
	 */
	public void setTotalNoOfRecords(int totalNoOfRecords) {
		this.totalNoOfRecords = totalNoOfRecords;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Page [startIndex=" + startIndex + ", pageSize=" + pageSize + ", totalNoOfRecords=" + totalNoOfRecords + "]";
	}

}
