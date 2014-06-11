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
package com.agnie.useradmin.session;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.agnie.common.injector.PersistenceLifeCycleManager;
import com.agnie.useradmin.session.server.entity.UserAuthSession;
import com.agnie.useradmin.session.server.injector.SessionPersistService;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

public class BaseSessionTest {
	@Inject
	protected Injector						injector;
	@Inject
	protected Provider<EntityManager>		em;
	@Inject
	@SessionPersistService
	protected PersistenceLifeCycleManager	manager;

	protected static final String			SAMPLE_USER_ID	= "sample-user-id";

	@BeforeClass
	public static void setup() {
	}

	@AfterClass
	public static void clean() throws Exception {
	}

	public EntityManager getEntityManager() {
		return em.get();
	}

	@Before
	public void before() {
		manager.beginUnitOfWork();
	}

	@After
	public void after() {
		manager.endUnitOfWork();
	}

	@Transactional
	public void removeUserSessions(String userId) {
		List<UserAuthSession> uaslist;
		TypedQuery<UserAuthSession> qry = getEntityManager().createNamedQuery("UserAuthSession.getByUser", UserAuthSession.class);
		qry.setParameter("userId", userId);
		if (qry != null) {
			uaslist = qry.getResultList();
			if (uaslist != null && uaslist.size() > 0) {
				for (UserAuthSession userAuthSession : uaslist) {
					getEntityManager().remove(userAuthSession);
				}
			}
		}
	}

	@Transactional
	public String createSession() {
		UserAuthSession uas = new UserAuthSession();
		uas.setId(java.util.UUID.randomUUID().toString());
		uas.setUserId(SAMPLE_USER_ID);
		getEntityManager().persist(uas);
		return uas.getId();
	}

}
