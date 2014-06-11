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
package com.agnie.useradmin.session.server.mybatis;

import java.io.InputStream;
import java.util.Properties;

import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.log4j.Logger;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import com.agnie.useradmin.session.server.mybatis.mapper.UserAccountMapper;
import com.google.inject.name.Names;

public class SessionMyBatisModule extends MyBatisModule {
	private static org.apache.log4j.Logger	logger	= Logger.getLogger(SessionMyBatisModule.class);

	// This mode refers to Junit testing mode or not.
	private boolean							mode	= false;

	public SessionMyBatisModule() {

	}

	public SessionMyBatisModule(boolean mode) {
		this.mode = mode;
	}

	@Override
	protected void initialize() {
		Properties myBatisProperties = new Properties();
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream is = cl.getResourceAsStream("SessionMyBatis.properties");
			myBatisProperties.load(is);
		} catch (Exception e) {
			logger.error(e);
		}
		if (mode) {
			myBatisProperties.setProperty("JDBC.schema", "UserAdminTest");
		}
		Names.bindProperties(binder(), myBatisProperties);
		install(JdbcHelper.MySQL);
		bindDataSourceProviderType(PooledDataSourceProvider.class);
		bindTransactionFactoryType(JdbcTransactionFactory.class);
		addMapperClass(UserAccountMapper.class);
	}

}
