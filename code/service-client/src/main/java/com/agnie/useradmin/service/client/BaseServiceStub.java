package com.agnie.useradmin.service.client;

import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.agnie.useradmin.service.client.entity.Error;
import com.agnie.useradmin.service.client.entity.Page;
import com.agnie.useradmin.service.client.entity.PageNSort;
import com.agnie.useradmin.service.client.entity.Sort;
import com.agnie.useradmin.service.client.injector.RestConstants;
import com.google.inject.Provider;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

/**
 * @author Pandurang Patil 08-Feb-2014
 * 
 */
public class BaseServiceStub {
	private static org.apache.log4j.Logger	logger	= Logger.getLogger(BaseServiceStub.class);
	private Provider<String>				sessionId;
	private WebResource						target;

	protected BaseServiceStub(WebResource target, Provider<String> sessionId) {
		this.target = target;
		this.sessionId = sessionId;
	}

	protected WebResource getWebResource() {
		return target.queryParam(RestConstants.HEAD_SESSIONID, sessionId.get());
	}

	protected <T> T handleResponse(ClientResponse resp, Class<T> cls) {
		return handleResponse(resp, cls, null);
	}

	protected <T> T handleResponse(ClientResponse resp, GenericType<T> type) {
		return handleResponse(resp, null, type);
	}

	private <T> T handleResponse(ClientResponse resp, Class<T> cls, GenericType<T> type) {
		Status status = Status.fromStatusCode(resp.getStatus());
		switch (status) {
		case OK:
		case CREATED:
		case ACCEPTED:
			if (cls != null)
				return resp.getEntity(cls);
			else if (type != null)
				return resp.getEntity(type);
		case NO_CONTENT:
			return null;
		case BAD_REQUEST:
		case INTERNAL_SERVER_ERROR:
			throw new ServerFault(status, resp.getEntity(Error.class));
		default:
			logger.error("Status code '" + status.getStatusCode() + " - " + status.getReasonPhrase() + "' is not handled");
			throw new ServerFault(status);
		}
	}

	protected WebResource fillPaginiationParameters(WebResource target, String search, PageNSort pagination) {
		WebResource webTarget = target;
		if (search != null && !search.isEmpty()) {
			webTarget = webTarget.queryParam("search", search);
		}
		if (pagination != null) {
			Page page = pagination.getPage();
			if (page != null) {
				webTarget = webTarget.queryParam("startIndex", page.getStartIndex() + "").queryParam("pageSize", page.getPageSize() + "");
			}
			Sort sort = pagination.getSort();
			if (sort != null) {
				webTarget = webTarget.queryParam("sortColumn", sort.getColumn()).queryParam("sortOrder", sort.getOrder().name());
			}
		}
		return webTarget;
	}
}
