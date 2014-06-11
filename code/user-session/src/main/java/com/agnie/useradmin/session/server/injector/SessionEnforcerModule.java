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
package com.agnie.useradmin.session.server.injector;

/**
 * Except user admin module servlet module all other project's servlet module will extend from SessionEnforcerModule.
 * And User admin servlet module will directly extend from SessionservletModule and it has separate filter bound.
 */
public class SessionEnforcerModule extends SessionServletModule {

	@Override
	protected void configureServlets() {
		super.configureServlets();
		filter("/*").through(AgnieAuthCheckFilter.class);
	}
}
