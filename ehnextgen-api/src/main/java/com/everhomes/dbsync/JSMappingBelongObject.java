package com.everhomes.dbsync;

import com.everhomes.util.StringHelper;

public class JSMappingBelongObject {
	String table;
	String fieldA;
	String fieldB;
	String join;
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getFieldA() {
		return fieldA;
	}
	public void setFieldA(String fieldA) {
		this.fieldA = fieldA;
	}
	public String getFieldB() {
		return fieldB;
	}
	public void setFieldB(String fieldB) {
		this.fieldB = fieldB;
	}

    public String getJoin() {
		return join;
	}
	public void setJoin(String join) {
		this.join = join;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
