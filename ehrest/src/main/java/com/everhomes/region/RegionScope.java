// @formatter:off
package com.everhomes.region;

public enum RegionScope {
    COUNTRY((byte)0), PROVINCE((byte)1), CITY((byte)2), AREA((byte)3), NEIGHBORHOOD((byte)4);
    
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
            return NEIGHBORHOOD;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
