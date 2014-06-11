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

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Singleton;

/**
 * This will facilitate grace full shutdown required for independent utilities that are being used. Utility or framework
 * that need to invoke graceful shutdown of the utility requires to implement ShutdownHook and register the same with
 * ShutdownProcessor. One can retrieve singleton instance of ShutdownProcessor from guice injector.
 * 
 * @author Pandurang Patil 15-Mar-2014
 * 
 */
@Singleton
public class ShutdownProcessor {
	// TODO: Make use of Event bus mechanism to avoid sequential intimation to individual hook.

	List<ShutdownHook>	hooks	= new ArrayList<ShutdownHook>();

	/**
	 * To register new shutdown hook which will be called when system (application either from inside web container or
	 * from act as independent application) is getting shutdown.
	 * 
	 * Note: You need to make sure your hook don't take much time. Other wise that may impact on shutdown
	 * for other hooks in queue.
	 * 
	 * @param hook
	 */
	public void register(ShutdownHook hook) {
		hooks.add(hook);
	}

	/**
	 * To initiate shutdown with shutdown hooks registered with processor. Ideally this has to be called from inside
	 * Servlet context listeners destroy method in case of web app. In case of independent app / tool you knwow the code
	 * better you should invoke it when you know your app is existing gracefully.
	 */
	public void shutdown(boolean sync) {
		for (ShutdownHook hook : hooks) {
			hook.shutdown(sync);
		}
	}

}
