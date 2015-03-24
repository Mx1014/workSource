// @formatter:off
package com.everhomes.region;

public enum RegionScope {
    country((byte)0), province((byte)1), city((byte)2), area((byte)3);
    
    private byte code;
    
    private RegionScope(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }

    public static RegionScope fromCode(byte code) {
        switch(code) {
        case 0 :
            return country;
            
        case 1 :
            return province;
            
        case 2 :
            return city;
            
        case 3 :
            return area;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
