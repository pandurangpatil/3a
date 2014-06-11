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
/**
 * 
 */
package com.agnie.common.requestfactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.shared.Locator;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;

/**
 *
 */
public class AgnieRFServiceLayerDecorator extends ServiceLayerDecorator {

	@Inject
	private Injector	injector;

	@Override
	public <T extends Locator<?, ?>> T createLocator(Class<T> clazz) {
		return injector.getInstance(clazz);
	}

	@Override
	public Object createServiceInstance(Class<? extends RequestContext> requestContext) {

		Class<? extends ServiceLocator> serviceLocatorClass;

		if ((serviceLocatorClass = getTop().resolveServiceLocator(requestContext)) != null) {

			return injector.getInstance(serviceLocatorClass).getInstance(requestContext.getAnnotation(Service.class).value());

		} else {

			return null;

		}

	}
}
