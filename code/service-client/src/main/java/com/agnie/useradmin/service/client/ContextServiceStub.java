package com.agnie.useradmin.service.client;

import javax.ws.rs.core.MediaType;

import com.agnie.common.injector.CommonModule;
import com.agnie.useradmin.service.ContextService;
import com.agnie.useradmin.service.client.entity.Context;
import com.agnie.useradmin.service.client.entity.PageNSort;
import com.agnie.useradmin.service.client.entity.PaginatedContext;
import com.agnie.useradmin.service.client.injector.RestConstants;
import com.agnie.useradmin.service.client.injector.ServiceClientModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author Pandurang Patil 08-Feb-2014
 * 
 */
@Singleton
public class ContextServiceStub extends BaseServiceStub implements ContextService {

	@Inject
	public ContextServiceStub(@Named(ServiceClientModule.USER_ADMIN_WEB_TARGET) WebResource target, @Named(ServiceClientModule.USER_ADMIN_SESSION_ID) Provider<String> sessionId) {
		super(target.path(RestConstants.CONTEXT_BASE), sessionId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.agnie.useradmin.service.ContextService#create(com.agnie.useradmin.service.client.entity.Context)
	 */
	@Override
	public String create(Context context) {
		ClientResponse rsp = getWebResource().type(MediaType.APPLICATION_JSON).post(ClientResponse.class, context);
		return handleResponse(rsp, String.class);
	}

	@Override
	public PaginatedContext getConnectedContexts(String search, PageNSort pagination) {
		ClientResponse rsp = fillPaginiationParameters(getWebResource(), search, pagination).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		return handleResponse(rsp, PaginatedContext.class);
	}

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new ServiceClientModule(""), new CommonModule());
		ContextService service = injector.getInstance(ContextService.class);
		String id = service.create(new Context("indrajit-Patil", "Indrajit Patil account"));
		System.out.println(id);
		PaginatedContext ctxs = service.getConnectedContexts(null, null);
		System.out.println(ctxs);
	}
}
