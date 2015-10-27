package com.everhomes.techpark.rental;
 
/**
 * <ul>
 * <li>NORMAL(0): 正常</li> 
 * <li>DELETED(-1):已删除</li> 
 * </ul>
 */
public enum RentalSiteStatus {
   
	NORMAL((byte)0),DELETED((byte)-1) ;
    
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
