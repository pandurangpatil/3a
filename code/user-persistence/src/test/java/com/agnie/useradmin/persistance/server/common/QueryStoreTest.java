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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.agnie.useradmin.persistance.client.enums.SortOrder;
import com.agnie.useradmin.persistance.server.listrequest.PageNSort;
import com.agnie.useradmin.persistance.server.listrequest.Sort;

public class QueryStoreTest {

	@Test
	public void readTest() {
		MetaQuery expected = new MetaQuery();
		expected.setBase("SELECT te FROM TestEntity te");
		expected.setWhere("te.status='ACTIVE'");
		Map<String, String> filters = new HashMap<String, String>();
		filters.put("search", "te.fname LIKE '%:search%'");
		filters.put("lname", "te.lname = :lname");
		expected.setFilters(filters);
		MetaQuery actual = QueryStore.getInstance().getStore(TestEntity.class, "active");
		Assert.assertEquals(expected, actual);

	}

	@Test
	public void queryTest1() {
		MetaQuery expected = new MetaQuery();
		expected.setBase("SELECT te FROM TestEntity te");
		expected.setWhere("te.status='ACTIVE'");
		Map<String, String> filters = new HashMap<String, String>();
		filters.put("search", "te.fname LIKE '%:search%'");
		filters.put("lname", "te.lname = :lname");
		expected.setFilters(filters);
		MetaQuery actual = QueryStore.getInstance().getStore(TestEntity.class, "active");
		Assert.assertEquals(expected, actual);

		PageNSort lparam = new PageNSort();
		Assert.assertEquals("SELECT te FROM TestEntity te WHERE te.status='ACTIVE'", actual.getQuery(lparam, null));
	}

	@Test
	public void queryTest2() {
		MetaQuery expected = new MetaQuery();
		expected.setBase("SELECT te FROM TestEntity te");
		Map<String, String> filters = new HashMap<String, String>();
		filters.put("search", "te.fname LIKE '%:search%'");
		filters.put("lname", "te.lname = :lname");
		expected.setFilters(filters);

		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("fname", "te.fname");
		mapping.put("lname", "te.lname");
		expected.setMapping(mapping);

		MetaQuery actual = QueryStore.getInstance().getStore(TestEntity.class, "all");
		Assert.assertEquals(expected, actual);

		PageNSort lparam = new PageNSort();
		Assert.assertEquals("SELECT te FROM TestEntity te", actual.getQuery(lparam, null));
	}

	@Test
	public void queryTest3() {
		MetaQuery expected = new MetaQuery();
		expected.setBase("SELECT te FROM TestEntity te");
		Map<String, String> filters = new HashMap<String, String>();
		filters.put("search", "te.fname LIKE '%:search%'");
		filters.put("lname", "te.lname = :lname");
		expected.setFilters(filters);

		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("fname", "te.fname");
		mapping.put("lname", "te.lname");
		expected.setMapping(mapping);
		MetaQuery actual = QueryStore.getInstance().getStore(TestEntity.class, "all");
		Assert.assertEquals(expected, actual);

		PageNSort lparam = new PageNSort();
		Set<String> paramKeys = new HashSet<String>();
		paramKeys.add("search");
		Assert.assertEquals("SELECT te FROM TestEntity te WHERE te.fname LIKE '%:search%'", actual.getQuery(lparam, paramKeys));
	}

	@Test
	public void queryTest4() {
		MetaQuery expected = new MetaQuery();
		expected.setBase("SELECT te FROM TestEntity te");
		Map<String, String> filters = new HashMap<String, String>();
		filters.put("search", "te.fname LIKE '%:search%'");
		filters.put("lname", "te.lname = :lname");
		expected.setFilters(filters);
		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("fname", "te.fname");
		mapping.put("lname", "te.lname");
		expected.setMapping(mapping);

		MetaQuery actual = QueryStore.getInstance().getStore(TestEntity.class, "all");
		Assert.assertEquals(expected, actual);

		PageNSort lparam = new PageNSort();
		Set<String> paramKeys = new HashSet<String>();
		paramKeys.add("search");
		paramKeys.add("lname");
		Assert.assertEquals("SELECT te FROM TestEntity te WHERE te.fname LIKE '%:search%' AND te.lname = :lname", actual.getQuery(lparam, paramKeys));
	}

	@Test
	public void queryTest5() {
		MetaQuery expected = new MetaQuery();
		expected.setBase("SELECT te FROM TestEntity te");
		expected.setWhere("te.status='ACTIVE'");
		Map<String, String> filters = new HashMap<String, String>();
		filters.put("search", "te.fname LIKE '%:search%'");
		filters.put("lname", "te.lname = :lname");
		expected.setFilters(filters);
		MetaQuery actual = QueryStore.getInstance().getStore(TestEntity.class, "active");
		Assert.assertEquals(expected, actual);

		PageNSort lparam = new PageNSort();
		Set<String> paramKeys = new HashSet<String>();
		paramKeys.add("search");
		paramKeys.add("lname");
		Assert.assertEquals("SELECT te FROM TestEntity te WHERE te.status='ACTIVE' AND te.fname LIKE '%:search%' AND te.lname = :lname", actual.getQuery(lparam, paramKeys));
	}

	@Test
	public void queryTest6() {
		MetaQuery expected = new MetaQuery();
		expected.setBase("SELECT te FROM TestEntity te");
		Map<String, String> filters = new HashMap<String, String>();
		filters.put("search", "te.fname LIKE '%:search%'");
		filters.put("lname", "te.lname = :lname");
		expected.setFilters(filters);

		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("fname", "te.fname");
		mapping.put("lname", "te.lname");
		expected.setMapping(mapping);
		MetaQuery actual = QueryStore.getInstance().getStore(TestEntity.class, "all");
		Assert.assertEquals(expected, actual);

		PageNSort lparam = new PageNSort();
		Set<String> paramKeys = new HashSet<String>();
		paramKeys.add("search");
		paramKeys.add("lname");
		Sort srt = new Sort();
		srt.setColumn("fname");
		srt.setOrder(SortOrder.ASC);
		lparam.setSort(srt);
		Assert.assertEquals("SELECT te FROM TestEntity te WHERE te.fname LIKE '%:search%' AND te.lname = :lname ORDER BY te.fname ASC", actual.getQuery(lparam, paramKeys));
	}
}
