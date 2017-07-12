package com.everhomes.rest.rentalv2.admin;
 
/**
 * <ul>
 * <li>NORMAL(2): 启用</li> 
 * <li>DISABLE(0):停用</li>
 * <li>CUSTOM(3):公司会议室</li>
 * </ul>
 */
public enum ResourceTypeStatus {
   
	NORMAL((byte)2),DISABLE((byte)0),CUSTOM((byte)3) ;
    
    private byte code;
    private ResourceTypeStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ResourceTypeStatus fromCode(byte code) {
        for(ResourceTypeStatus t : ResourceTypeStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
