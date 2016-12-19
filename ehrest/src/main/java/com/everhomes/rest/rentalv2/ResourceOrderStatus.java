package com.everhomes.rest.rentalv2;
 
/**
 * <ul>
 * <li>NORMAL(0): 普通的</li> 
 * <li>DISABLE(1):不显示给用户的</li> 
 * </ul>
 */
public enum ResourceOrderStatus {
   
	NORMAL((byte)0),DISPLOY((byte)1), ;
    
    private byte code;
    private ResourceOrderStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ResourceOrderStatus fromCode(byte code) {
        for(ResourceOrderStatus t : ResourceOrderStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
