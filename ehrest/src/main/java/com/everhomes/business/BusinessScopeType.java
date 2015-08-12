// @formatter:off
package com.everhomes.business;

/**
 * <ul>
 * <li>ALL(0): 国家</li>
 * <li>CITY(2): 城市</li>
 * <li>COMMUNITY(1): 小区</li>
 * </ul>
 */
public enum BusinessScopeType {
    ALL((byte)0), COMMUNITY((byte)1), CITY((byte)2);
    
    private byte code;
    
    private BusinessScopeType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }

    public static BusinessScopeType fromCode(Byte code) {
        if(code == null)
            return null;
        BusinessScopeType[] values = BusinessScopeType.values();
        for(BusinessScopeType value : values){
            if(value.getCode() == code.byteValue())
                return value;
        }
        
        return null;
    }
}
