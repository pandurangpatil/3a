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
package com.agnie.useradmin.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.agnie.common.tools.BaseAdminTools;
import com.agnie.common.tools.CommandProcessor;
import com.agnie.common.tools.Commander;
import com.agnie.useradmin.tools.dbinit.DBInit;
import com.agnie.useradmin.tools.session.ClearSession;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Names;

public class AdminTools extends BaseAdminTools {
	public static final String	CMD_DBINIT		= "dbinit";
	public static final String	CLEAR_SESSION	= "clearsession";

	@Inject
	public AdminTools(DBInit init, ClearSession session, Commander commander) {
		super(commander);
		Map<String, CommandProcessor> cmds = new HashMap<String, CommandProcessor>();
		cmds.put(CMD_DBINIT, init);
		cmds.put(CLEAR_SESSION, session);
		super.setCommands(cmds);
	}

	public static void main(String[] args) throws Exception {
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				Properties prop = new Properties();
				try {
					prop.load(this.getClass().getResourceAsStream("version.txt"));
				} catch (IOException e) {
					binder().addError("Error while loading version.txt");
				}

				Names.bindProperties(binder(), prop);
			}
		});
		AdminTools tools = injector.getInstance(AdminTools.class);
		tools.processArguments(args);
	}

}
