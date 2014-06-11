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
package com.agnie.useradmin.common.client.helper;

import javax.validation.Validator;

import com.agnie.useradmin.persistance.client.service.dto.UserInfo;
import com.agnie.useradmin.persistance.shared.proxy.ApplicationPx;
import com.agnie.useradmin.persistance.shared.proxy.ContextPx;
import com.agnie.useradmin.persistance.shared.proxy.PermissionPx;
import com.agnie.useradmin.persistance.shared.proxy.RolePx;
import com.agnie.useradmin.persistance.shared.proxy.UserPx;
import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;

/**
 * Idealy this file should have been placed inside persistence module. But when this file is copied inside persistence
 * module because of some reason mvn-helper-plugin:1.0:requestFactory plugin fails and build cannot proceed. This issue
 * need to be fixed from plugin. Once this issue is fixed we need to move this back to persistence module.
 * 
 * @author Pandurang Patil 16-Feb-2014
 * 
 */
public class UserAdminValidatorFactory extends AbstractGwtValidatorFactory {
	/**
	 * Validator marker for the Validation Sample project. Only the classes and groups listed in the
	 * {@link GwtValidation} annotation can be validated.
	 */
	@GwtValidation(value = { UserInfo.class, UserPx.class, ApplicationPx.class, RolePx.class, PermissionPx.class, ContextPx.class })
	public interface GwtValidator extends Validator {
	}

	@Override
	public AbstractGwtValidator createValidator() {
		return GWT.create(GwtValidator.class);
	}

}
