package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

// {"ownerType":"organization","entityTag":"GACW"}
public class NearbyActivitiesActionData {
	private String scope; 
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    }
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	} 
	
}
