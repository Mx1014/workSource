package com.everhomes.dbsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.everhomes.util.StringHelper;

public class JSQueryObjectItem {
	String tableName;
	List<String> conditions;
	Map<String, String> defaults;

	public JSQueryObjectItem() {
		this.conditions = new ArrayList<String>();
		this.defaults = new HashMap<String, String>();
	}
	
    public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getConditions() {
		return conditions;
	}

	public void setConditions(List<String> conditions) {
		this.conditions = conditions;
	}

	public Map<String, String> getDefaults() {
		return defaults;
	}

	public void setDefaults(Map<String, String> defaults) {
		this.defaults = defaults;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
