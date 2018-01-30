package com.everhomes.rest.rentalv2.admin;

/**
 * <ul>
 * <li>DEFAULT(1): 通用</li>
 * <li>ENTERPRISE(2): 公司会议室</li>
 * </ul>
 */
public enum ResourceMenuType {

	DEFAULT((byte)1), ENTERPRISE((byte)2) ;

    private byte code;
    private ResourceMenuType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ResourceMenuType fromCode(byte code) {
        for(ResourceMenuType t : ResourceMenuType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
