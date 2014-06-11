/*******************************************************************************
 * © 2014 Copyright Agnie Technologies
 *   
 * NOTICE: All information contained herein is, and remains the property of Agnie Technologies and its suppliers, if
 * any. The intellectual and technical concepts contained herein are proprietary to Agnie Technologies and its suppliers
 * and may be covered by Indian and Foreign Patents, patents in process, and are protected by trade secret or copyright
 * law. Dissemination of this information or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Agnie Technologies.
 ******************************************************************************/
package com.agnie.common.tools;

import com.beust.jcommander.Parameter;
import com.google.inject.Singleton;

@Singleton
public class MainArgs {
	@Parameter(names = "--help", help = true)
	private boolean	help;

	@Parameter(names = { "-v", "--version" }, description = "Version of utility")
	private boolean	version;

	/**
	 * @return the help
	 */
	public boolean isHelp() {
		return help;
	}

	/**
	 * @param help
	 *            the help to set
	 */
	public void setHelp(boolean help) {
		this.help = help;
	}

	/**
	 * @return the version
	 */
	public boolean isVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(boolean version) {
		this.version = version;
	}

}
