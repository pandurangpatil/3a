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
package com.agnie.useradmin.service.injector;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.agnie.useradmin.service.auth.ApplicationAccessFilter;
import com.agnie.useradmin.service.client.injector.RestConstants;
import com.agnie.useradmin.service.exception.ExceptionIntercepter;
import com.agnie.useradmin.service.rest.ApplicationResource;
import com.agnie.useradmin.service.rest.ContextResource;
import com.agnie.useradmin.service.rest.UserProfileResource;
import com.google.inject.matcher.Matchers;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class UserAdminJerseyServletModule extends JerseyServletModule implements RestConstants {

	@Override
	protected void configureServlets() {

		// Bind jackson converters for JAXB/JSON serialization
		// If you don't do these JSON related setting, you will
		// endup using the default JSON library of Jersey.
		bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
		bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);
		Map<String, String> map = new HashMap<String, String>();
		map.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");

		// Route all requests through GuiceContainer
		filterRegex("(/" + REST_BASE + "/)(.)*").through(ApplicationAccessFilter.class);
		serveRegex("(/" + REST_BASE + "/)(.)*").with(GuiceContainer.class, map);
		bind(UserProfileResource.class);
		bind(ContextResource.class);
		bind(ApplicationResource.class);
		// Here its assumed that all the resource classes are annotated with @Path annotation, so subscribing this
		// intercepter for those methods of resource which are annotated with either GET, PUT, POST or DELETE. First
		// parameter will match for class and second parameter will match for methods.
		ExceptionIntercepter loggerInterceptor = new ExceptionIntercepter();
		requestInjection(loggerInterceptor);
		bindInterceptor(Matchers.annotatedWith(Path.class), Matchers.annotatedWith(GET.class), loggerInterceptor);
		bindInterceptor(Matchers.annotatedWith(Path.class), Matchers.annotatedWith(PUT.class), loggerInterceptor);
		bindInterceptor(Matchers.annotatedWith(Path.class), Matchers.annotatedWith(POST.class), loggerInterceptor);
		bindInterceptor(Matchers.annotatedWith(Path.class), Matchers.annotatedWith(DELETE.class), loggerInterceptor);
	}
}
