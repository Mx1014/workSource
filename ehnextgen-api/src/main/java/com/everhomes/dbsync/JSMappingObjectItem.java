package com.everhomes.dbsync;

import java.util.List;

import com.everhomes.util.StringHelper;

public class JSMappingObjectItem {
	List<String> fields;
	String primary;
	List<JSMappingBelongObject> belong;
	List<JSMappingHasObject> hasMany;
	List<JSMappingHasObject> hasOne;

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public String getPrimary() {
		return primary;
	}

	public void setPrimary(String primary) {
		this.primary = primary;
	}

	public List<JSMappingBelongObject> getBelong() {
		return belong;
	}

	public void setBelong(List<JSMappingBelongObject> belong) {
		this.belong = belong;
	}

	public List<JSMappingHasObject> getHasMany() {
		return hasMany;
	}

	public void setHasMany(List<JSMappingHasObject> hasMany) {
		this.hasMany = hasMany;
	}

	public List<JSMappingHasObject> getHasOne() {
		return hasOne;
	}

	public void setHasOne(List<JSMappingHasObject> hasOne) {
		this.hasOne = hasOne;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
