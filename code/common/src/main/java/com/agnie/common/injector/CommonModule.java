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
package com.agnie.common.injector;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.inject.Named;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.agnie.common.email.GmailSender;
import com.agnie.common.email.MailSender;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class CommonModule extends AbstractModule {
	private static Logger		logger					= Logger.getLogger(CommonModule.class);
	public static final String	AGNIE_PROJECT			= "agnie-project";
	public static final String	GMAIL_SENDER			= "gmail";
	public static final String	AGNIE_HOME				= "agnie-home";
	public static final String	AGNIE_HOME_ENV			= "AGNIE_HOME";
	public static final String	VERSION					= "version";
	public static final String	CLIENT_PROXY_TIMEOUT	= "client-proxy-timeout";
	public static final String	agnieHome				= System.getenv(AGNIE_HOME_ENV);
	public static final String	SERVER_IP				= "server.ip";

	@Provides
	@Named(AGNIE_HOME)
	public String getAdworksHome() {
		return agnieHome;
	}

	@Override
	protected void configure() {
		bind(ObjectMapper.class).asEagerSingleton();
		bind(MailSender.class).annotatedWith(Names.named(GMAIL_SENDER)).to(GmailSender.class);

		Properties version = new Properties();
		try {
			version.load(getClass().getResourceAsStream("version.properties"));
			Names.bindProperties(binder(), version);
		} catch (Exception e) {
			logger.error("Error while reading version.properties file: ", e);
		}

		Properties globalConfig = new Properties();
		try {
			globalConfig.load(new FileInputStream(new File(agnieHome + "/global/config/global-config.properties")));
			Names.bindProperties(binder(), globalConfig);
		} catch (Exception e) {
			logger.error("Error while reading global-config.properties file: ", e);
		}
	}
}
