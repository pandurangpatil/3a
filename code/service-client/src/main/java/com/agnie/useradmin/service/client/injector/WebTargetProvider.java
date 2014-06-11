package com.agnie.useradmin.service.client.injector;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.agnie.common.gwt.serverclient.client.helper.URLGenerator;
import com.agnie.useradmin.service.UserAdminClientFilter;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author Pandurang Patil 07-Feb-2014
 * 
 */
public class WebTargetProvider implements Provider<WebResource> {
	private String					endpoint;
	
	private Client					target;
	private UserAdminClientFilter	clientFilter;

	@Inject
	public WebTargetProvider(@Named(URLGenerator.USER_ADMIN_ROOT_ENDPOINT) String endpoint, UserAdminClientFilter clientFilter) {
		this.endpoint = endpoint + "/rest";
		this.clientFilter = clientFilter;

	}

	@Override
	public WebResource get() {
		if (target == null) {
			ClientConfig cc = new DefaultClientConfig(JacksonJsonProvider.class);
			target = Client.create(cc);
			target.addFilter(clientFilter);
		}
		return target.resource(endpoint);
	}
}
