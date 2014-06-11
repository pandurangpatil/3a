package com.agnie.useradmin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.agnie.useradmin.service.client.injector.RestConstants;
import com.agnie.useradmin.service.client.injector.ServiceClientModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

/**
 * @author Pandurang Patil 07-Feb-2014
 * 
 */
@Singleton
public class UserAdminClientFilter extends ClientFilter {

	private Provider<String>	sessionId;
	private String				accessKey;
	private String				selDomain;

	@Inject
	public UserAdminClientFilter(@Named(ServiceClientModule.USER_ADMIN_SESSION_ID) Provider<String> sessionId, @Named(ServiceClientModule.USER_ADMIN_API_ACCESS_KEY) String accessKey,
			@Named(ServiceClientModule.USER_ADMIN_SEL_DOMAIN) String selDomain) {
		this.sessionId = sessionId;
		this.accessKey = accessKey;
		this.selDomain = selDomain;
	}

	@Override
	public ClientResponse handle(ClientRequest request) throws ClientHandlerException {
		final Map<String, List<Object>> headers = request.getHeaders();
		final List<Object> sessValue = new ArrayList<Object>();
		sessValue.add(sessionId.get());
		headers.put(RestConstants.HEAD_SESSIONID, sessValue);
		final List<Object> keyValue = new ArrayList<Object>();
		keyValue.add(accessKey);
		headers.put(RestConstants.HEAD_ACC_KEY, keyValue);
		final List<Object> domainValue = new ArrayList<Object>();
		domainValue.add(selDomain);
		headers.put(RestConstants.HEAD_SEL_DOMAIN, domainValue);
		return getNext().handle(request);
	}

}
