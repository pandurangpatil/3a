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
package com.agnie.common.requestfactory;

import org.apache.log4j.Logger;

import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class AgnieRFExceptionHandler implements ExceptionHandler {
	private static org.apache.log4j.Logger	logger	= Logger.getLogger(AgnieRFExceptionHandler.class);

	public ServerFailure createServerFailure(Throwable throwable) {
		throwable.printStackTrace();
		logger.error(throwable);
		return new ServerFailure((throwable == null ? null : throwable.getMessage()), (throwable == null ? null : throwable.getClass().getName()), null, true);
	}

}
