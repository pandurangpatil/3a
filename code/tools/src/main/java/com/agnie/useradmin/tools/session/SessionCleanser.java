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
package com.agnie.useradmin.tools.session;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.agnie.common.time.DateService;
import com.agnie.useradmin.session.server.entity.UserAuthSession;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

/**
 * 
 * @author Pandurang Patil 04-Mar-2014
 * 
 */
@Singleton
public class SessionCleanser {

	@Inject
	Provider<EntityManager>	em;
	@Inject
	DateService				dateService;

	@Transactional
	public void removeExpiredSessions() {
		TypedQuery<UserAuthSession> qr = em.get().createNamedQuery("UserAuthSession.removeExpired", UserAuthSession.class);
		Date currentdt = dateService.getCurrentDate();
		qr.setParameter("currentTime", currentdt);
		qr.executeUpdate();
	}
}
