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
package com.agnie.useradmin.tools.dbinit;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.log4j.Logger;

import com.agnie.common.helper.SHA256;
import com.agnie.useradmin.persistance.client.enums.AuthLevel;
import com.agnie.useradmin.persistance.client.enums.GeneralStatus;
import com.agnie.useradmin.persistance.client.enums.RequestStatus;
import com.agnie.useradmin.persistance.client.enums.UserStatus;
import com.agnie.useradmin.persistance.server.common.digester.DummyRole;
import com.agnie.useradmin.persistance.server.entity.Application;
import com.agnie.useradmin.persistance.server.entity.DefaultAppRole;
import com.agnie.useradmin.persistance.server.entity.Permission;
import com.agnie.useradmin.persistance.server.entity.Role;
import com.agnie.useradmin.persistance.server.entity.User;
import com.agnie.useradmin.persistance.server.entity.UserApplicationRegistration;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

/**
 * One need to first clean the data base and then add roles and permission. It can be done by having drop and create
 * option in persistence.xml
 * 
 */
public class DBInitializer {
	@SuppressWarnings("unused")
	private static Logger				logger	= Logger.getLogger(DBInitializer.class);
	@Inject
	protected Provider<EntityManager>	em;

	private Properties					initProperties;

	public void setInitProperties(Properties initProperties) {
		this.initProperties = initProperties;
	}

	@Transactional
	public void IntializeData() throws Exception {
		User root = createUser();
		Application dm = new Application();
		dm.setId("1");
		dm.setBusinessName(initProperties.getProperty("top.app.name"));
		dm.setDomain(initProperties.getProperty("top.app.domain"));
		dm.setStatus(GeneralStatus.ACTIVE);
		dm.setURL("/userapp/landing.jsp");
		dm.setIconURL("/userapp/images/logo.png");
		dm.setContactEmail(initProperties.getProperty("top.app.email"));
		em.get().persist(dm);
		UserApplicationRegistration uar = new UserApplicationRegistration();
		uar.setApp(dm);
		uar.setUser(root);
		uar.setStatus(RequestStatus.ACTIVE);
		uar.setOwner(true);
		em.get().persist(uar);
		createPermissionsRoles(dm);
		Role rl = getRoleByNameAndApplication("Regular-User", dm.getId());
		List<Role> rls = new ArrayList<Role>();
		rls.add(rl);
		DefaultAppRole dar = new DefaultAppRole();
		dar.setRoles(rls);
		dar.setLevel(AuthLevel.APPLICATION);
		dar.setApplication(dm);
		em.get().persist(dar);
	}

	@Transactional
	private Role getRoleByNameAndApplication(String name, String appId) {

		TypedQuery<Role> qry = em.get().createNamedQuery("Role.getByApplicationAndName", Role.class);
		qry.setParameter("name", name);
		qry.setParameter("applicationId", appId);
		return qry.getSingleResult();
	}

	@Transactional
	public User createUser() {
		User user = new User();
		user.setId("1");
		user.setFirstName("Pandurang");
		user.setLastName("Patil");
		user.setUserName(initProperties.getProperty("first.user.username"));
		user.setEmailId(initProperties.getProperty("first.user.email"));
		user.setPassword(SHA256.getHashedString(initProperties.getProperty("first.user.password")));
		user.setStatus(UserStatus.ACTIVE);
		user.setTitle("Mr");
		em.get().persist(user);
		return user;
	}

	@Transactional
	public void createPermissionsRoles(Application dm) throws Exception {
		HashMap<String, Permission> permMapping = new HashMap<String, Permission>();
		List<Permission> pms = new ArrayList<Permission>();
		InputStream input = getClass().getResourceAsStream("permissions.xml");
		URL rules = getClass().getResource("permission-digester-rule.xml");
		Digester digester = DigesterLoader.createDigester(rules);
		digester.push(pms);
		digester.parse(input);
		for (Permission pm : pms) {
			pm.setId(java.util.UUID.randomUUID().toString());
			pm.setApplication(dm);
			em.get().persist(pm);
			permMapping.put(pm.getCode(), pm);
		}
		createRole(permMapping, dm);
	}

	@Transactional
	public void createRole(HashMap<String, Permission> permMapping, Application dm) throws Exception {
		List<DummyRole> rls = new ArrayList<DummyRole>();
		InputStream input = getClass().getResourceAsStream("roles.xml");
		URL rules = getClass().getResource("role-digester-rule.xml");
		Digester digester = DigesterLoader.createDigester(rules);
		digester.push(rls);
		digester.parse(input);
		List<Permission> pms = null;
		for (DummyRole dummyRole : rls) {
			pms = new ArrayList<Permission>();
			Role rl = new Role();
			rl.setId(java.util.UUID.randomUUID().toString());
			rl.setName(dummyRole.getName());
			rl.setDescription(dummyRole.getDescription());
			rl.setLevel(dummyRole.getAuthLevel());
			rl.setApplication(dm);
			List<String> rlPms = dummyRole.getPermissions();
			for (String rlpm : rlPms) {
				pms.add(permMapping.get(rlpm));
			}
			rl.setPermissions(pms);
			em.get().persist(rl);
		}

	}

	public static void process() throws Exception {

	}
}
