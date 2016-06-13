// @formatter:off
package com.everhomes.rest.region;

/**
 * <ul>
 * <li>COUNTRY: 国家</li>
 * <li>PROVINCE: 省份</li>
 * <li>CITY: 城市</li>
 * <li>AREA: 区县</li>
 * <li>COMMUNITY: 小区</li>
 * </ul>
 */
public enum RegionScope {
    COUNTRY((byte)0), PROVINCE((byte)1), CITY((byte)2), AREA((byte)3), COMMUNITY((byte)4);
    
    private byte code;
    
    private RegionScope(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }

    public static RegionScope fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return COUNTRY;
            
        case 1 :
            return PROVINCE;
            
        case 2 :
            return CITY;
            
        case 3 :
            return AREA;
            
        case 4 :
            return COMMUNITY;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
