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
package com.agnie.useradmin.persistance.server.common;

import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.agnie.useradmin.persistance.server.listrequest.PageNSort;

/**
 * Using it one can generated the required TypedQuery by setting pagination, sorting and filter clause. Additional
 * parameters other than that of filter clause need to be set by end user (developer) of this class. This query
 * generator expect the required .json file to be placed under same package as that of resultant class. And for every
 * key there should be separate json file created. e.g if resultant class is com.xyz.test.entity.Person.java then
 * 
 */
public class QueryGenerator {

	private EntityManager	em;

	/**
	 * @param em
	 */
	public QueryGenerator(EntityManager em) {
		this.em = em;
	}

	/**
	 * Create query will read respective meta info from corresponding json file with Query Key. It will take care of
	 * adding sorting parameters inside query. And also it will take care of adding pagination settings over TypedQuery.
	 * Its been assumed that filter parameters would be anded with other logical where clause. Other conditions can be
	 * specified inside where clause. You need to pass on additional parameters that need to be set on TypedQuery. Which
	 * you would like to pass on as parameters to TypedQuery.
	 * 
	 * @param <T>
	 * @param qk
	 *            Query Key for json property
	 * @param resultClass
	 * @param pageNSort
	 * @param queryParams
	 * @return
	 */
	public <T> TypedQuery<T> createQuery(String qk, Class<T> resultClass, PageNSort pageNSort, Map<String, Object> queryParams) {
		QueryStore qs = QueryStore.getInstance();
		MetaQuery mq = qs.getStore(resultClass, qk);
		Set<String> paramKeys = null;
		if (queryParams != null) {
			paramKeys = queryParams.keySet();
		}
		TypedQuery<T> query = em.createQuery(mq.getQuery(pageNSort, paramKeys), resultClass);
		if (queryParams != null) {
			for (String key : queryParams.keySet()) {
				query.setParameter(key, queryParams.get(key));
			}
		}
		this.setPageConfig(query, pageNSort);
		return query;
	}

	/**
	 * Set pagination related parameters to TypedQuery
	 * 
	 * @param query
	 * @param lParam
	 */
	protected void setPageConfig(TypedQuery<?> query, PageNSort lParam) {
		if (lParam != null && lParam.getPage() != null) {
			int rangeind = lParam.getPage().getStartIndex();
			query.setFirstResult(rangeind);
			rangeind = lParam.getPage().getPageSize();
			if (rangeind != 0) {
				query.setMaxResults(rangeind);
			}
		}
	}
}
