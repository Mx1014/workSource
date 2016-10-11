package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

// {"ownerType":"organization","entityTag":"GACW"}
public class NearbyActivitiesActionData {
	private Integer scope; 
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    }
	public Integer getScope() {
		return scope;
	}
	public void setScope(Integer scope) {
		this.scope = scope;
	} 
	
}
