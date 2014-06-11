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
package com.agnie.common.injector;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * 
 * @author Pandurang Patil 30-Jan-2014
 * 
 */
@Singleton
public class AgnieDeploymentConfig {

	private String	agnieHome;
	@SuppressWarnings("unused")
	private String	project;
	private String	projectHome;
	private String	projectConfig;
	private String	projectBin;

	@Inject
	public AgnieDeploymentConfig(@Named(CommonModule.AGNIE_PROJECT) String project) {
		this.agnieHome = CommonModule.agnieHome;
		this.project = project;
		this.projectHome = agnieHome + "/" + project;
		this.projectConfig = projectHome + "/config";

	}

	public String getAgnieHome() {
		return agnieHome;
	}

	public String getHome() {
		return projectHome;
	}

	public String getConfig() {
		return projectConfig;
	}

	public String getBin() {
		return projectBin;
	}

}
