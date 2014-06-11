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
package com.agnie.common.shutdown;

/**
 * @author Pandurang Patil 15-Mar-2014
 * 
 */
public interface ShutdownHook {
	/**
	 * 
	 * @param sync represents call should be returned only after shutdown is done.
	 */
	void shutdown(boolean sync);
}
