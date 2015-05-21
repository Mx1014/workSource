// @formatter:off
package com.everhomes.address;

/**
 * <p>地址居住状态:0-未知、1-自住、2-出租、3-空置、4-装修、5-待售</p>
 * <ul>
 * <li>UNKNOWN: 未知 </li>
 * <li>LIVINGSELF: 自住 </li>
 * <li>RENT: 出租 </li>
 * </ul>
 */
public enum AddressLivingStatus {
    UNKNOWN((byte)0), LIVINGSELF((byte)1),RENT((byte)2);
    
    private byte code;
    private AddressLivingStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static AddressLivingStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return UNKNOWN;
            
        case 1 :
            return LIVINGSELF;
            
        case 2 :
            return RENT;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
