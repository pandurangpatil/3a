package com.agnie.useradmin.service;

import java.util.List;

import com.agnie.useradmin.service.client.entity.Context;
import com.agnie.useradmin.service.client.entity.UserApplicationRegistration;

/**
 * 
 * @author Pandurang Patil 06-Feb-2014
 * 
 */
public interface UserProfileService {

	UserApplicationRegistration getApplicationProfile();

	List<Context> getRegisteredContexts();
}
