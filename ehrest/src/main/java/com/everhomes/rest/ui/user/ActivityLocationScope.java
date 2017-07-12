// @formatter:off
package com.everhomes.rest.ui.user;

/**
 * <ul>
 * <li>ALL(0): 所有</li>
 * <li>NEARBY(1): 周边</li>
 * <li>SAME_CITY(2): 同城</li>
 * <li>COMMUNITY(3): 当前小区</li>
 * </ul>
 */
public enum ActivityLocationScope {
    ALL((byte)0), NEARBY((byte)1), SAME_CITY((byte)2), COMMUNITY((byte)3);
    
    private byte code;
    
    private ActivityLocationScope(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ActivityLocationScope fromCode(Byte code) {
        if(code != null) {
            ActivityLocationScope[] values = ActivityLocationScope.values();
            for(ActivityLocationScope value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
