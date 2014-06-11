package com.agnie.useradmin.service.client.entity;

import java.io.Serializable;

public class Page implements Serializable {

	private static final long	serialVersionUID	= 1L;
	private int					startIndex;
	private int					pageSize;

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
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
