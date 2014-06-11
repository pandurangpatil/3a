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
package com.agnie.useradmin.persistance.server.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class ContextRole extends BaseEntity {

	@OneToOne
	private UserApplicationCtxRegistration	usApplicationCtxRegistration;
	@ManyToMany
	private List<Role>						roles;

	/**
	 * @return the roles
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @return the usApplicationCtxRegistration
	 */
	public UserApplicationCtxRegistration getUsApplicationCtxRegistration() {
		return usApplicationCtxRegistration;
	}

	/**
	 * @param usApplicationCtxRegistration
	 *            the usApplicationCtxRegistration to set
	 */
	public void setUsApplicationCtxRegistration(UserApplicationCtxRegistration usApplicationCtxRegistration) {
		this.usApplicationCtxRegistration = usApplicationCtxRegistration;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContextRole other = (ContextRole) obj;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		return true;
	}

}
