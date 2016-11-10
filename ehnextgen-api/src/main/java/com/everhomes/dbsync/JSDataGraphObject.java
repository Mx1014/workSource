package com.everhomes.dbsync;

import java.util.List;
import java.util.Map;

import com.everhomes.util.StringHelper;

public class JSDataGraphObject {
	private String appName;
	private String mapName;
	
	private List<String> tables;
	Map<String, JSMappingObjectItem> mapping;
	Map<String, JSQueryObjectItem> query;

    public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	
	public Map<String, JSMappingObjectItem> getMapping() {
		return mapping;
	}
	public void setMapping(Map<String, JSMappingObjectItem> mapping) {
		this.mapping = mapping;
	}
	public Map<String, JSQueryObjectItem> getQuery() {
		return query;
	}
	public void setQuery(Map<String, JSQueryObjectItem> query) {
		this.query = query;
	}

    public List<String> getTables() {
		return tables;
	}

	public void setTables(List<String> tables) {
		this.tables = tables;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
