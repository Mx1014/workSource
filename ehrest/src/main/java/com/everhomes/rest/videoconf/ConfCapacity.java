package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>CONF_CAPACITY_25: 25方 0</li>
 * <li>CONF_CAPACITY_100: 100方 1</li>
 * <li>CONF_CAPACITY_50: 50方 3</li>
 * <li>CONF_CAPACITY_6: 6方 2</li>
 * </ul>
 */
public enum ConfCapacity {

	CONF_CAPACITY_25("25方", (byte)0), CONF_CAPACITY_100("100方", (byte)1), CONF_CAPACITY_50("50方", (byte)3), CONF_CAPACITY_6("6方", (byte)2);
    
    private String code;
    private byte status;
    
    private ConfCapacity(String code, byte status) {
        this.code = code;
        this.status = status;
    }
    
    public String getCode() {
        return this.code;
    }

    public byte getStatus() {
		return status;
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
