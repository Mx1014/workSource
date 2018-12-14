package com.everhomes.rest.officecubicle;
 
/**
 * <ul>
 * <li>RENTED(0): 已出租</li> 
 * <li>RENTIONG(1):待出租</li>
 * <li>UNOPENED(2):未开放</li>
 * </ul>
 */
public enum OfficeCubicleSiteTypes {
   
	RENTED((byte)0), RENTIONG((byte)1), UNOPENED((byte)2);
    
    private byte code;
    private OfficeCubicleSiteTypes(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OfficeCubicleSiteTypes fromCode(byte code) {
        for(OfficeCubicleSiteTypes t : OfficeCubicleSiteTypes.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
