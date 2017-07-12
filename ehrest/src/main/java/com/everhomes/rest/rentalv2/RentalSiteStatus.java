package com.everhomes.rest.rentalv2;
 
/**
 * <ul>
 * <li>NORMAL(0): 启用</li> 
 * <li>DISABLE(-1):停用</li> 
 * <li>DISPLOY(-2):前端不展示</li> 
 * </ul>
 */
public enum RentalSiteStatus {
   
	NORMAL((byte)0),DISABLE((byte)-1),DISPLOY((byte)-2);
    
    private byte code;
    private RentalSiteStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static RentalSiteStatus fromCode(byte code) {
        for(RentalSiteStatus t : RentalSiteStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
