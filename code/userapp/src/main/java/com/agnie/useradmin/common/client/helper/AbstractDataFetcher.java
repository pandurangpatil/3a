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
package com.agnie.useradmin.common.client.helper;

import com.agnie.gwt.common.client.rpc.AsyncDP;
import com.agnie.useradmin.common.client.base.CellTableView;
import com.agnie.useradmin.common.client.base.Sort;
import com.agnie.useradmin.persistance.shared.proxy.PageNSortVpx;
import com.agnie.useradmin.persistance.shared.proxy.PageVpx;
import com.agnie.useradmin.persistance.shared.proxy.SortVpx;
import com.google.gwt.view.client.Range;
import com.google.web.bindery.requestfactory.shared.RequestContext;

public abstract class AbstractDataFetcher<T> implements AsyncDP.DataFetcher<T> {

	protected PageNSortVpx getPaging(Range range, CellTableView view, RequestContext ctx) {
		PageNSortVpx paging = ctx.create(PageNSortVpx.class);
		PageVpx page = ctx.create(PageVpx.class);
		page.setStartIndex(range.getStart());
		page.setPageSize(range.getLength());
		paging.setPage(page);
		Sort sortInfo = view.getSortInfo();
		if (sortInfo != null) {
			SortVpx sort = ctx.create(SortVpx.class);
			sort.setColumn(sortInfo.getSortColumn());
			sort.setOrder(sortInfo.getOrder());
			paging.setSort(sort);
		}
		return paging;
	}
}
