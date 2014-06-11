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
package com.agnie.useradmin.service.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.agnie.common.gwt.serverclient.client.enums.QueryString;
import com.agnie.useradmin.persistance.server.auth.DomainAuthorizer;
import com.agnie.useradmin.persistance.server.mybatis.mapper.ApplicationMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class ApplicationAccessFilter implements Filter {
	private static org.apache.log4j.Logger	logger	= Logger.getLogger(ApplicationAccessFilter.class);
	@Inject
	@Named(DomainAuthorizer.SELECTED_DOMAIN)
	private Provider<String>				domain;

	@Inject
	protected Provider<ApplicationMapper>	appMapper;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		try {
			if (domain.get() != null && !domain.get().isEmpty()) {
				String accessKey = httpRequest.getHeader(QueryString.API_ACCESS_KEY.getKey());
				if (accessKey != null && !accessKey.isEmpty()) {
					String origAccessKey = appMapper.get().getAPIAccessKey(domain.get());
					if (origAccessKey != null && !origAccessKey.isEmpty()) {
						if (origAccessKey.equals(accessKey)) {
							chain.doFilter(request, response);
							return;
						}
					} else {
						logger.error("Access key is not configured because of some reason");
					}
				} else {
					logger.error(QueryString.API_ACCESS_KEY.getKey() + " header is not present");
				}
			} else {
				logger.error("application / domain is not selected");
			}
			throw new WebApplicationException(Status.FORBIDDEN);
		} catch (WebApplicationException e) {
			logger.error(e);
			// In most of the case code will reach hear only if there is any exception raised before request processing
			// is passed on to GuiceContainer. And here I am assuming such situations will be very minimal with response
			// to be sent only in the form of status code.
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(e.getResponse().getStatus());
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
