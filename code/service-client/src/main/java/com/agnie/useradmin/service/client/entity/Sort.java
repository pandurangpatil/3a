package com.agnie.useradmin.service.client.entity;

import java.io.Serializable;

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
	public String getColumn() {
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(String column) {
		this.column = column;
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
