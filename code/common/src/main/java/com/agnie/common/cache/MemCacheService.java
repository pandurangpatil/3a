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
package com.agnie.common.cache;

import java.io.IOException;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.spy.memcached.MemcachedClient;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.agnie.common.shutdown.ShutdownHook;
import com.agnie.common.shutdown.ShutdownProcessor;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Implements connector to Memcache server.
 * 
 */
@Singleton
public class MemCacheService implements CacheService, ShutdownHook {
	private static Map<String, Class<?>>	classMapping			= new HashMap<String, Class<?>>();
	private MemcachedClient					client;
	private ObjectMapper					mapper;
	public static final int					DEFAULT_EXPIRATION_TIME	= 3600;
	public static final int					MAX_KEY_SIZE			= 250;
	// timeout in seconds.
	public static final int					DEFAULT_TIMEOUT			= 1;

	/**
	 * Construct will list of memcache servers to use.
	 * 
	 * @param hostPortInp
	 *            Memcache servers specified as "host1:port1, host2:port2, ..."
	 */
	@Inject
	public MemCacheService(@Named("CacheServers") String hostPortInp, ShutdownProcessor shutdownProcessor) {
		shutdownProcessor.register(this);
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Inclusion.NON_NULL);
		String[] hostPorts = hostPortInp.split(",");
		List<InetSocketAddress> addrList = new ArrayList<InetSocketAddress>();

		for (String hostPort : hostPorts) {
			int port;
			if (hostPort == null) {
				throw new IllegalArgumentException("CacheServers not specified in configuration");
			}
			hostPort = hostPort.trim();

			String[] parts = hostPort.split(":");
			if (parts.length != 2) {
				throw new IllegalArgumentException("CacheServers - host:port information is in invalid format: " + hostPort);
			}

			try {
				port = Integer.parseInt(parts[1]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("CacheServers - Port number was not numeric in host:port format - " + hostPort);
			}
			addrList.add(new InetSocketAddress(parts[0], port));
		}
		try {
			client = new MemcachedClient(addrList);
		} catch (IOException e) {
			throw new IllegalArgumentException("CacheServers - Connection to " + hostPortInp + " failed", e);
		}

	}

	public void put(String key, Object value) {
		put(key, value, DEFAULT_EXPIRATION_TIME);
	}

	/**
	 * Set a JSON like object into the cache. This method will take care of serializing the object into actual JSON.
	 * 
	 * @param key
	 *            The key to be stored under.
	 * @param value
	 *            The value being stored in cache.
	 * @param expTime
	 *            The expiration time for the entry.
	 */
	public void put(String key, Object value, Integer expTime) {
		if (expTime == null) {
			expTime = DEFAULT_EXPIRATION_TIME;
		}

		if (key.length() > MAX_KEY_SIZE) {
			throw new IllegalArgumentException("Size of key being pushed into cache too large: " + key.length());
		}
		if (value == null) {
			client.delete(key);
			return;
		}
		Class<?> objCls = value.getClass();
		if (objCls.getCanonicalName() == null) {
			throw new IllegalArgumentException("Cannot serialize object of class which doesn't have any name");
		}
		if (!classMapping.values().contains(objCls)) {
			classMapping.put(objCls.getCanonicalName(), objCls);
		}
		client.set(key, expTime, serialize(new CachedValue(objCls.getCanonicalName(), serialize(value))));
	}

	/**
	 * Converts Java objects (maps, lists and basic types) into JSON string
	 * 
	 * @param value
	 *            Java objects.
	 * @return Byte array containing JSON.
	 */
	public String serialize(Object value) {
		StringWriter sw = new StringWriter();

		try {
			// write JSON to a string
			mapper.writeValue(sw, value);
		} catch (JsonGenerationException e) {
			throw new IllegalArgumentException("Failed to serialize: " + value, e);
		} catch (JsonMappingException e) {
			throw new IllegalArgumentException("Failed to serialize: " + value, e);
		} catch (IOException e) {
			throw new IllegalArgumentException("Failed to serialize: " + value, e);
		}

		return sw.toString();
	}

	public Object get(String key) {
		return get(key, 1);
	}

	/**
	 * Fetch from cache with specified key.
	 * 
	 * @param key
	 *            The key being used to fetch.
	 * @return The corresponding value if cache entry exists, else null.
	 */
	public Object get(String key, int timeout) {

		String value = (String) client.get(key);
		if (value == null || value.isEmpty()) {
			return null;
		}
		CachedValue cached = deserialize(value, CachedValue.class);
		if (cached != null) {
			Object val = deserialize(cached.getV(), getClass(cached.getC()));
			return val;
		}
		return null;
	}

	/**
	 * Convert byte array with JSON format into Java objects.
	 * 
	 * @param data
	 *            json byte string
	 * @return Java objects.
	 */
	public <T> T deserialize(String data, Class<T> cls) {

		try {
			return mapper.readValue(data, cls);
		} catch (JsonGenerationException e) {
			throw new IllegalArgumentException("Failed to deserialize: " + data, e);
		} catch (JsonMappingException e) {
			throw new IllegalArgumentException("Failed to deserialize: " + data, e);
		} catch (IOException e) {
			throw new IllegalArgumentException("Failed to deserialize: " + data, e);
		}
	}

	/**
	 * Retrieve Class instance from class name.
	 * 
	 * @param classname
	 * @return
	 */
	private Class<?> getClass(String classname) {
		Class<?> cls = classMapping.get(classname);
		if (cls == null) {
			try {
				cls = Class.forName(classname);
				classMapping.put(classname, cls);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Error in mem cache service: Class not Found '" + classname + "'");
			}
		}
		return cls;
	}

	private static class CachedValue {
		private String	c;
		private String	v;

		@SuppressWarnings("unused")
		public CachedValue() {
			super();
		}

		public CachedValue(String c, String v) {
			super();
			this.c = c;
			this.v = v;
		}

		public String getC() {
			return c;
		}

		@SuppressWarnings("unused")
		public void setC(String c) {
			this.c = c;
		}

		public String getV() {
			return v;
		}

		@SuppressWarnings("unused")
		public void setV(String v) {
			this.v = v;
		}

		@Override
		public String toString() {
			return "CachedValue [c=" + c + ", v=" + v + "]";
		}
	}

	@Override
	public void shutdown(boolean sync) {
		client.shutdown();
	}
}
