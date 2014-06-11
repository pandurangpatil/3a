/*******************************************************************************
 * © 2014 Copyright Agnie Technologies
 *   
 * NOTICE: All information contained herein is, and remains the property of Agnie Technologies and its suppliers, if
 * any. The intellectual and technical concepts contained herein are proprietary to Agnie Technologies and its suppliers
 * and may be covered by Indian and Foreign Patents, patents in process, and are protected by trade secret or copyright
 * law. Dissemination of this information or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Agnie Technologies.
 ******************************************************************************/
package com.agnie.common.test.injector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.Module;

/**
 * <p>
 * This <code>@WithModule</code> annotation is used when writing test that requires Guice. It indicates which modules
 * should be loaded.
 * </p>
 * 
 * @author Antoine DESSAIGNE
 * @see com.google.inject.Module
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface WithModules {
	/**
	 * Array of all the modules' classes to load for configuring the Guice injector.
	 * 
	 * @return Modules' classes to load.
	 */
	public Class<? extends Module>[] value();
}
