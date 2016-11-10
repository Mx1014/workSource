package com.everhomes.dbsync;

import java.util.List;

import com.everhomes.util.StringHelper;

public class JSQueryObjectItem {
	String tableName;
	List<String> conditions;
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
