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
package com.agnie.useradmin.persistance.server.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * Store which will parse and cache queries in json format
 * 
 */
public class QueryStore {

	private static org.apache.log4j.Logger			logger		= Logger.getLogger(QueryStore.class);
	private static QueryStore						instance;

	private Map<Class<?>, Map<String, MetaQuery>>	cache		= new HashMap<Class<?>, Map<String, MetaQuery>>();
	private ObjectMapper							mapper		= new ObjectMapper();
	private JsonFactory								jsonFactory	= new JsonFactory();

	private QueryStore() {

	}

	public static QueryStore getInstance() {
		if (instance == null) {
			synchronized (QueryStore.class) {
				if (instance == null) {
					instance = new QueryStore();
				}
			}

		}
		return instance;
	}

	/**
	 * Retrieve cached MetaQuery instance which contains all the information which is there in corresponding .json.
	 * 
	 * @param path
	 * @return
	 */
	public <T> MetaQuery getStore(Class<T> resultClass, String qk) {
		if (resultClass != null && qk != null && !("".equals(qk))) {
			Map<String, MetaQuery> qryStr = cache.get(resultClass);
			if (qryStr == null) {
				try {
					qryStr = getMetaQueryFromJson(getPath(resultClass, qk));
					cache.put(resultClass, qryStr);
				} catch (Exception e) {
					logger.error("Critical error", e);
					e.printStackTrace();
				}
			}
			return qryStr.get(qk);
		}
		return null;
	}

	/**
	 * Generate .json file path. It is expected that the given required josn file must be placed under same package
	 * structure as that of resultant class
	 * 
	 * @param <T>
	 * @param resultClass
	 * @param qk
	 * @return
	 * @throws IOException
	 */
	protected <T> InputStream getPath(Class<T> resultClass, String qk) throws IOException {
		String path = resultClass.getCanonicalName();
		path = "/" + path.replace(".", "/") + ".json";
		return this.getClass().getResourceAsStream(path);
	}

	/**
	 * Read the json data parse it and populate MetaQuery object and return it
	 * 
	 * @param path
	 *            fully qualified system path of json file
	 * @return populated MetaQuery object
	 * @throws JsonParseException
	 * @throws IOException
	 */
	private Map<String, MetaQuery> getMetaQueryFromJson(InputStream path) throws JsonParseException, IOException {
		Map<String, MetaQuery> resp = new HashMap<String, MetaQuery>();
		JsonParser jp = jsonFactory.createJsonParser(path);
		jp.nextToken();
		while (jp.nextToken() != JsonToken.END_OBJECT) {
			String fieldname = jp.getCurrentName();
			jp.nextToken();
			MetaQuery mq = new MetaQuery();
			while (jp.nextToken() != JsonToken.END_OBJECT) {
				String inrFldName = jp.getCurrentName();
				jp.nextToken();
				if ("base".equals(inrFldName)) {
					mq.setBase(jp.getText());
				} else if ("where".equals(inrFldName)) {
					mq.setWhere(jp.getText());
				} else if ("filter".equals(inrFldName)) {
					Map<String, String> filter = mapper.readValue(jp, new TypeReference<Map<String, String>>() {
					});
					mq.setFilters(filter);
				} else if ("mapping".equals(inrFldName)) {
					Map<String, String> mappings = mapper.readValue(jp, new TypeReference<Map<String, String>>() {
					});
					mq.setMapping(mappings);
				}
			}
			resp.put(fieldname, mq);
		}
		return resp;
	}

	public static void main(String[] args) throws Exception {
		String path = "/com/agnie/useradmin/persistance/server/entity/user.all.json";
		URL url = QueryStore.class.getResource(path);
		System.out.println("Path => " + url.getPath());
		System.out.println("file = > " + url.getFile());
		System.out.println(QueryStore.class.getCanonicalName());
	}
}
