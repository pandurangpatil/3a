package com.agnie.useradmin.service.client;

import java.util.List;

import javax.ws.rs.core.MediaType;

import com.agnie.common.injector.CommonModule;
import com.agnie.useradmin.service.UserProfileService;
import com.agnie.useradmin.service.client.entity.Context;
import com.agnie.useradmin.service.client.entity.UserApplicationRegistration;
import com.agnie.useradmin.service.client.injector.RestConstants;
import com.agnie.useradmin.service.client.injector.ServiceClientModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

/**
 * @author Pandurang Patil 07-Feb-2014
 * 
 */
@Singleton
public class UserProfileServiceStub extends BaseServiceStub implements UserProfileService {

	@Inject
	public UserProfileServiceStub(@Named(ServiceClientModule.USER_ADMIN_WEB_TARGET) WebResource target, @Named(ServiceClientModule.USER_ADMIN_SESSION_ID) Provider<String> sessionId) {
		super(target.path(RestConstants.USER_PROF_BASE), sessionId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.agnie.useradmin.service.UserProfileService#getApplicationProfile()
	 */
	@Override
	public UserApplicationRegistration getApplicationProfile() {
		ClientResponse response = getWebResource().accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		return handleResponse(response, UserApplicationRegistration.class);
	}

	@Override
	public List<Context> getRegisteredContexts() {
		ClientResponse rsp = getWebResource().path(RestConstants.CONTEXT).type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		return handleResponse(rsp, new GenericType<List<Context>>() {
		});
	}

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new ServiceClientModule(""), new CommonModule());
		UserProfileService service = injector.getInstance(UserProfileService.class);
		UserApplicationRegistration uar = service.getApplicationProfile();
		System.out.println(uar);
		
		List<Context> contexs = service.getRegisteredContexts();
		System.out.println(contexs);
	}

}
