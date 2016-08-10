package com.everhomes.rest.rentalv2;
 
/**
 * <ul>
 * <li>VISIBLE(0): 用户预订记录可见 </li>
 * <li>UNVISIBLE(1): 用户预订记录不可见</li> 
 * </ul>
 */
public enum VisibleFlag {
   
    VISIBLE((byte)0),UNVISIBLE((byte)1);
    
    private byte code;
    private VisibleFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static VisibleFlag fromCode(byte code) {
        for(VisibleFlag t : VisibleFlag.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
