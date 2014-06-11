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
package com.agnie.useradmin.session.server.auth;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlEngine;

import com.agnie.common.server.auth.ACLContext;

/**
 * This class will help you evaluate permission expression by passing permission expression and acl to
 * evaluatePermisisonExp() method. UPDATE UserAdmin.PERMISSION SET CODE = REPLACE(CODE,'.', '_');
 */
public class PermissionExpressionEvaluator {
	private static JexlEngine	jexl	= new JexlEngine();

	/**
	 * Evaluate Permission will make use of JEXL engine to evaluate the permission expression.
	 * 
	 * @param expression
	 * @param acl
	 * @return
	 */
	public boolean evaluatePermissionExp(String expression, ACLContext acljc) {
		Expression e = jexl.createExpression(expression);
		return (Boolean) e.evaluate(acljc);
	}

}
