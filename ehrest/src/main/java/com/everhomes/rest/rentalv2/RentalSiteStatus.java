package com.everhomes.rest.rentalv2;
 
/**
 * <ul>
 * <li>NORMAL(0): 启用</li> 
 * <li>DELETED(-1):删除</li>
 * <li>DISABLE(-2):停用</li>
 * </ul>
 */
public enum RentalSiteStatus {
   
	NORMAL((byte)0), DELETED((byte)-1), DISABLE((byte)-2);
    
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
