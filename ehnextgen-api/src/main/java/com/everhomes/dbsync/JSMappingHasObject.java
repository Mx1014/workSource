package com.everhomes.dbsync;

import com.everhomes.util.StringHelper;

public class JSMappingHasObject {
	String table;
	String fieldB;
	String join;

	public String getJoin() {
		return join;
	}
	public void setJoin(String join) {
		this.join = join;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getFieldB() {
		return fieldB;
	}
	public void setFieldB(String fieldB) {
		this.fieldB = fieldB;
	}
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
