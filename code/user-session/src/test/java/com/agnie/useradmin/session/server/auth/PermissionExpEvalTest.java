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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;
import com.agnie.common.server.auth.ACLContext;

public class PermissionExpEvalTest {

	@Test
	public void goodExpTest() {
		try {
			AccessControlList acl = new AccessControlList();
			acl.addPermission("perm_domain_create");
			acl.addPermission("perm_domain_edit");
			acl.addPermission("perm_domain_edit_user_view");
			acl.addPermission("perm_domain_create");
			acl.addPermission("perm_domain_role_view");

			String exp = "(perm_domain_create || perm_domain_edit) && perm_domain_role";

			PermissionExpressionEvaluator pee = new PermissionExpressionEvaluator();
			ACLContext aclCtx = new ACLContext(acl);
			Assert.assertTrue(pee.evaluatePermissionExp(exp, aclCtx));

			exp = "perm_domain_role";

			pee = new PermissionExpressionEvaluator();
			aclCtx.clear();
			Assert.assertTrue(pee.evaluatePermissionExp(exp, aclCtx));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void badExpTest() {
		try {
			AccessControlList acl = new AccessControlList();
			acl.addPermission("perm_domain_create");
			acl.addPermission("perm_domain_edit");
			acl.addPermission("perm_domain_edit_user_view");
			acl.addPermission("perm_domain_create");
			acl.addPermission("perm_domain_role_view");

			String exp = "(perm_domain_create || perm_domain_edit) && perm_domain_role_create";
			ACLContext aclCtx = new ACLContext(acl);
			PermissionExpressionEvaluator pee = new PermissionExpressionEvaluator();
			aclCtx.clear();
			Assert.assertFalse(pee.evaluatePermissionExp(exp, aclCtx));
			List<String> expectedListperms = new ArrayList<String>();
			expectedListperms.add("perm_domain_create");
			expectedListperms.add("perm_domain_role_create");
			Assert.assertEquals(expectedListperms, aclCtx.getCheckPerms());
			exp = "perm_domain_role || perm_user";

			pee = new PermissionExpressionEvaluator();
			aclCtx.clear();
			Assert.assertTrue(pee.evaluatePermissionExp(exp, aclCtx));

			exp = "perm_domain_role_edit || perm_user";

			pee = new PermissionExpressionEvaluator();
			aclCtx.clear();
			Assert.assertFalse(pee.evaluatePermissionExp(exp, aclCtx));
			expectedListperms = new ArrayList<String>();
			expectedListperms.add("perm_domain_role_edit");
			expectedListperms.add("perm_user");
			Assert.assertEquals(expectedListperms, aclCtx.getCheckPerms());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
