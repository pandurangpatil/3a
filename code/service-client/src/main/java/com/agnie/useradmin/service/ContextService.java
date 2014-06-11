package com.agnie.useradmin.service;

import com.agnie.useradmin.service.client.entity.Context;
import com.agnie.useradmin.service.client.entity.PageNSort;
import com.agnie.useradmin.service.client.entity.PaginatedContext;

/**
 * 
 * @author Pandurang Patil 08-Feb-2014
 * 
 */
public interface ContextService {

	public String create(Context context);

	public PaginatedContext getConnectedContexts(String search, PageNSort pagination);
}
