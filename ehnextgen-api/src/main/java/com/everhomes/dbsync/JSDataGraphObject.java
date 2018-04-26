package com.everhomes.dbsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.everhomes.util.StringHelper;

public class JSDataGraphObject {
	private String appName;
	private String mapName;
	
	private List<String> tables;
	Map<String, JSMappingObjectItem> mapping;
	
	public JSDataGraphObject() {
		tables = new ArrayList<String>();
		mapping = new HashMap<String, JSMappingObjectItem>();
	}

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
