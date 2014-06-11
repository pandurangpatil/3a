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
package com.agnie.useradmin.session.server.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import com.agnie.common.cache.CacheService;
import com.agnie.common.gwt.serverclient.client.dto.UserAccount;
import com.agnie.common.time.DateService;
import com.agnie.useradmin.session.server.entity.UserAuthSession;
import com.agnie.useradmin.session.server.injector.SessionCommonModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.persist.Transactional;

@Singleton
public class UserAuthSessionManagerImpl implements UserAuthSessionManager {
	private static org.apache.log4j.Logger	logger	= Logger.getLogger(UserAuthSessionManagerImpl.class);

	@Inject
	Provider<EntityManager>					em;

	@Inject
	CacheService							cacheService;

	@Inject
	DateService								dateService;

	@Inject
	@Named(SessionCommonModule.DEFAULT_EXP_TIME)
	private int								defaultExpTime;

	@Inject
	@Named(SessionCommonModule.REMEMBER_EXP_TIME)
	private int								rememberExpTime;

	private EntityManager getEntityManager() {
		return em.get();
	}

	/**
	 * Create User session and return session Id
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional
	public String createSession(String userId, boolean rememberMe) {
		try {
			UserAuthSession uas = new UserAuthSession();
			uas.setId(java.util.UUID.randomUUID().toString());
			uas.setUserId(userId);
			Date currentdt = dateService.getCurrentDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentdt);
			if (rememberMe)
				cal.add(Calendar.SECOND, rememberExpTime);
			else
				cal.add(Calendar.SECOND, defaultExpTime);
			uas.setExpiryTimeStamp(cal.getTime());
			getEntityManager().persist(uas);
			return uas.getId();
		} catch (Exception e) {
			logger.error("getUserAuthBySessionId", e);
		}
		return null;
	}

	/**
	 * Retrieve UserAuthSession object from sessionId
	 * 
	 * @param sessionId
	 * @return
	 */
	@Transactional
	public UserAuthSession getUserAuthBySessionId(String sessionId) {
		UserAuthSession uas = null;
		try {
			uas = getEntityManager().find(UserAuthSession.class, sessionId);
		} catch (Exception e) {
			logger.error("getUserAuthBySessionId", e);
		}
		return uas;
	}

	/**
	 * to retrieve all expired user sessions.
	 * 
	 * @return
	 */
	@Transactional
	public List<UserAuthSession> getExpiredUserSessions() {
		List<UserAuthSession> resp = new ArrayList<UserAuthSession>();
		try {
			TypedQuery<UserAuthSession> qr = getEntityManager().createNamedQuery("UserAuthSession.getAllExpiredSessions", UserAuthSession.class);
			DateService dtSer = new DateService();
			Date currentdt = dtSer.getCurrentDate();
			qr.setParameter("currentTime", currentdt);
			if (qr != null) {
				resp = qr.getResultList();
			}
			return resp;
		} catch (Exception n) {
			logger.error("getExpiredUserSessions", n);
		}
		return resp;
	}

	/**
	 * Retrieve all the sessions associated with given User.
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional
	public List<UserAuthSession> getUserSessionsByUser(String userId) {
		List<UserAuthSession> resp = new ArrayList<UserAuthSession>();
		try {
			TypedQuery<UserAuthSession> qr = getEntityManager().createNamedQuery("UserAuthSession.getByUser", UserAuthSession.class);
			qr.setParameter("userId", userId);
			if (qr != null) {
				resp = qr.getResultList();
			}
			return resp;
		} catch (Exception n) {
			logger.error("getUserSessionsByuserId", n);
		}
		return resp;
	}

	@Transactional
	public void updateSession(UserAuthSession uas, UserAccount ua) {
		Date currentdt = dateService.getCurrentDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentdt);
		ua.setLastUpdateTime(currentdt);
		// saving updated user account in cache.
		cacheService.put(uas.getId(), ua);
		cal.add(Calendar.SECOND, defaultExpTime);
		if (uas.getExpiryTimeStamp() == null || uas.getExpiryTimeStamp().before(cal.getTime())) {
			uas.setUpdateTimeStamp(currentdt);
			uas.setExpiryTimeStamp(cal.getTime());
			getEntityManager().persist(uas);
		}
	}

	@Override
	@Transactional
	public void removeExpiredSessions() {
		TypedQuery<UserAuthSession> qr = getEntityManager().createNamedQuery("UserAuthSession.removeExpired", UserAuthSession.class);
		DateService dtSer = new DateService();
		Date currentdt = dtSer.getCurrentDate();
		qr.setParameter("currentTime", currentdt);
		qr.executeUpdate();
	}
}
