package com.agnie.useradmin.service.client.entity;

import java.util.List;

public class PaginatedContext {

	private PageNSort		pageNSort;
	private List<Context>	list;

	public PaginatedContext() {
	}

	/**
	 * @param pageNSort
	 * @param list
	 */
	public PaginatedContext(PageNSort pageNSort, List<Context> list) {
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
	public List<Context> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<Context> list) {
		this.list = list;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PaginatedContext [pageNSort=" + pageNSort + ", list=" + list + "]";
	}

}
