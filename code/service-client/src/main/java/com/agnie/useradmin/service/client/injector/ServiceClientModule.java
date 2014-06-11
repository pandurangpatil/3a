package com.agnie.useradmin.service.client.injector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.agnie.common.injector.AgnieDeploymentConfig;
import com.agnie.useradmin.service.ContextService;
import com.agnie.useradmin.service.UserProfileService;
import com.agnie.useradmin.service.client.ContextServiceStub;
import com.agnie.useradmin.service.client.UserProfileServiceStub;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.sun.jersey.api.client.WebResource;

/**
 * @author Pandurang Patil 06-Feb-2014
 * 
 */
public class ServiceClientModule extends AbstractModule {
	private static Logger		logger						= Logger.getLogger(ServiceClientModule.class);
	private String				parentModuleConfigLocation;
	public static final String	USER_ADMIN_API_ACCESS_KEY	= "3a.api.access.key";
	public static final String	USER_ADMIN_SEL_DOMAIN		= "3a.selected.domain";
	// keeping this value same as that of com.agnie.useradmin.session.server.injector.SessionServletModule.SESSION_ID so
	// that the required value will made available by SessionFilter itself when it is being used from Agnie projects.
	public static final String	USER_ADMIN_SESSION_ID		= "session-id";
	public static final String	USER_ADMIN_WEB_TARGET		= "3a.web.target";

	/**
	 * @param parentModuleConfigLocation
	 */
	public ServiceClientModule(String parentModuleConfigLocation) {
		this.parentModuleConfigLocation = parentModuleConfigLocation;
	}

	@Override
	protected void configure() {
		AgnieDeploymentConfig config = new AgnieDeploymentConfig(parentModuleConfigLocation + "/3A/ServiceClient");
		Properties configuration = new Properties();
		try {
			configuration.load(new FileInputStream(new File(config.getConfig() + "/client-config.properties")));
		} catch (IOException e) {
			logger.error("Error while reading client-config.properties : ", e);
		}
		if (!configuration.isEmpty()) {
			Names.bindProperties(binder(), configuration);
		}
		bind(WebResource.class).annotatedWith(Names.named(USER_ADMIN_WEB_TARGET)).toProvider(WebTargetProvider.class);
		// bind(String.class).annotatedWith(Names.named(USER_ADMIN_SESSION_ID)).toInstance("4fd0eaf8-db45-4b0a-9a87-d6c1a2f37570");
		bind(UserProfileService.class).to(UserProfileServiceStub.class);
		bind(ContextService.class).to(ContextServiceStub.class);
	}

}
