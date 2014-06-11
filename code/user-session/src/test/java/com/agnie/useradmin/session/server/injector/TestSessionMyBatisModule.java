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

import com.agnie.useradmin.session.server.mybatis.SessionMyBatisModule;

/**
 * 
 * @author Pandurang Patil 04-Feb-2014
 * 
 */
public class TestSessionMyBatisModule extends SessionMyBatisModule {

	public TestSessionMyBatisModule() {
		super(true);
	}

}
