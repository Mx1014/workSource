// @formatter:off
package com.everhomes.search;

import com.alibaba.fastjson.JSONArray;

public interface SearchProvider {

	String insertOrUpdate(String type, String id, String json);

	String getById(String type, String id);

	String deleteById(String type, String id);

	String bulk(String type, String json);

	JSONArray query(String type, String json, String fields);

	String clearType(String type);
	
	JSONArray queryTopHits(String type, String json, String fields);

}
