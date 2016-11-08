// @formatter:off
package com.everhomes.rest.region;

/**
 * <ul>
 * <li>UNACTIVE: </li>
 * <li>ACTIVE: 正常</li>
 * </ul>
 */
public enum RegionCodeStatus {
    UNACTIVE((byte)0), ACTIVE((byte)1);

    private byte code;

    private RegionCodeStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static RegionCodeStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return UNACTIVE;
            
        case 1:
            return ACTIVE;
            
        default :
            assert(false);
            break;
        }
        return null;
    }
}
