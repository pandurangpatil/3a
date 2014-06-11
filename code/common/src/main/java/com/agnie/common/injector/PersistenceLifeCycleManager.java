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

/**
 * Defined a interface to start and stop persistence service also to begin and end unit of work. In multiple Persistent
 * unit environment.
 * 
 */
public interface PersistenceLifeCycleManager {

	public void startService();

	public void stopService();

	public void beginUnitOfWork();

	public void endUnitOfWork();
}
