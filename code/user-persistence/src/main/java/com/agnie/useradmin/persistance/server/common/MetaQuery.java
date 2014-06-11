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

import com.agnie.useradmin.persistance.client.enums.SortOrder;
import com.agnie.useradmin.persistance.server.listrequest.PageNSort;
import com.agnie.useradmin.persistance.server.listrequest.Sort;

public class MetaQuery {

	private String				base;
	private String				where;
	private Map<String, String>	filters;
	private Map<String, String>	mapping;

	/**
	 * @return the base
	 */
	public String getBase() {
		return base;
	}

	/**
	 * @param base
	 *            the base to set
	 */
	public void setBase(String base) {
		this.base = base;
	}

	/**
	 * @return the where
	 */
	public String getWhere() {
		return where;
	}

	/**
	 * @param where
	 *            the where to set
	 */
	public void setWhere(String where) {
		this.where = where;
	}

	/**
	 * @return the filters
	 */
	public Map<String, String> getFilters() {
		return filters;
	}

	/**
	 * @param filters
	 *            the filters to set
	 */
	public void setFilters(Map<String, String> filters) {
		this.filters = filters;
	}

	/**
	 * @return the mapping
	 */
	public Map<String, String> getMapping() {
		return mapping;
	}

	/**
	 * @param mapping
	 *            the mapping to set
	 */
	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
	}

	/**
	 * Retrieve complete query built from parameter passed and query configured in json. Here its assumed that key
	 * passed through ListParam should match exactly with that of corresponding filter key in json config file. And
	 * while writing filter condition you need to take care of using same key as parameter in JPA query.
	 * 
	 * @param lparam
	 * @param paramKeys
	 * @return
	 */

	public String getQuery(PageNSort lparam, Set<String> paramKeys) {
		StringBuffer query = new StringBuffer(this.base);
		boolean addAnd = false;
		if (this.where != null && !("".equals(this.where.trim()))) {
			query.append(" WHERE " + this.where);
			addAnd = true;
		}
		if (this.filters != null && paramKeys != null) {
			for (String paramKey : filters.keySet()) {
				if (paramKeys.contains(paramKey)) {
					if (addAnd) {
						query.append(" AND " + filters.get(paramKey));
					} else {
						query.append(" WHERE " + filters.get(paramKey));
						addAnd = true;
					}
				}
			}
		}
		if (lparam != null) {
			Sort sort = lparam.getSort();
			if (sort != null && this.mapping != null) {
				String colmn = this.mapping.get(sort.getColumn());
				if (colmn != null && !("".equals(colmn))) {
					query.append(" ORDER BY " + colmn);
					query.append((SortOrder.ASC.equals(sort.getOrder()) ? " ASC" : " DESC"));
				}
			}
		}
		return query.toString();
	}

	@Override
	public String toString() {
		return "MetaQuery [base=" + base + ", where=" + where + ", filters=" + filters + ", mapping=" + mapping + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result + ((filters == null) ? 0 : filters.hashCode());
		result = prime * result + ((mapping == null) ? 0 : mapping.hashCode());
		result = prime * result + ((where == null) ? 0 : where.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaQuery other = (MetaQuery) obj;
		if (base == null) {
			if (other.base != null)
				return false;
		} else if (!base.equals(other.base))
			return false;
		if (filters == null) {
			if (other.filters != null)
				return false;
		} else if (!filters.equals(other.filters))
			return false;
		if (mapping == null) {
			if (other.mapping != null)
				return false;
		} else if (!mapping.equals(other.mapping))
			return false;
		if (where == null) {
			if (other.where != null)
				return false;
		} else if (!where.equals(other.where))
			return false;
		return true;
	}

}
