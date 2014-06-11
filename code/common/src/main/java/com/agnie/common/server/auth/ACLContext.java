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
/**
 * 
 */
package com.agnie.common.server.auth;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jexl2.JexlContext;

import com.agnie.common.gwt.serverclient.client.dto.AccessControlList;

/**
 * Implementation of JexlContext which will help in evaluating permission expression. Every individual permission from
 * logical expression of the permission will be checked and evaluated if that permission is present in ACL. If present
 * it will return true.
 */
public class ACLContext implements JexlContext {

	private AccessControlList	acl;

	private List<String>		checkPerms	= new ArrayList<String>();

	public ACLContext() {

	}

	/**
	 * @param acl
	 */
	public ACLContext(AccessControlList acl) {
		this.acl = acl;
	}

	/**
	 * @return the acl
	 */
	public AccessControlList getAcl() {
		return acl;
	}

	public void clear() {
		checkPerms.clear();
	}

	/**
	 * @param acl
	 *            the acl to set
	 */
	public void setAcl(AccessControlList acl) {
		this.acl = acl;
	}

	/**
	 * /** If you call this method it will return you list of permissions that were checked while evaluating the
	 * permissions expression. You need to call this method immediately after evaluatePermissionExp() other wise you
	 * will lose the information for given permission expression if evaluatePermissionExp() called once again to
	 * evaluate other expression.
	 * 
	 * @return the checkPerms
	 */
	public List<String> getCheckPerms() {
		return checkPerms;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.jexl2.JexlContext#get(java.lang.String)
	 */
	public Object get(String perm) {
		checkPerms.add(perm);
		if (acl != null)
			return acl.check(perm);
		else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.jexl2.JexlContext#set(java.lang.String, java.lang.Object)
	 */
	public void set(String name, Object value) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.jexl2.JexlContext#has(java.lang.String)
	 */
	public boolean has(String name) {
		// TODO Auto-generated method stub
		return false;
	}

}
