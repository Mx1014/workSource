package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ACCOUNT_TYPE_SINGLE: 25方</li>
 * <li>ACCOUNT_TYPE_MULTIPLE: 100方</li>
 * </ul>
 */
public enum ConfCapacity {

	CONF_CAPACITY_25("25方"), CONF_CAPACITY_100("100方");
    
    private String code;
    private ConfCapacity(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }

	public static ConfCapacity fromCode(String code) {
		for(ConfCapacity v :ConfCapacity.values()){
			if(v.getCode().equals(code))
				return v;
		}
		return null;
	}

	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
