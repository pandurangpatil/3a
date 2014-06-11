package com.agnie.useradmin.service.client.entity;

import java.io.Serializable;

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
	public Page getPage() {
		return page;
	}

	/**
	 * @param page
	 *            the page to set
	 */
	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * @return the sort
	 */
	public Sort getSort() {
		return sort;
	}

	/**
	 * @param sort
	 *            the sort to set
	 */
	public void setSort(Sort sort) {
		this.sort = sort;
	}

}
