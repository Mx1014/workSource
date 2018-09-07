// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>
 * <li>CLIENT((byte)0): 客户端请求</li>
 * <li>BACKGROUND((byte)1): 后台请求</li>
 * </ul>
 */
public enum ParkingSourceRequestType {
    CLIENT((byte)0), BACKGROUND((byte)1);

    private byte code;

    private ParkingSourceRequestType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingSourceRequestType fromCode(Byte code) {
        if(code != null) {
            ParkingSourceRequestType[] values = ParkingSourceRequestType.values();
            for(ParkingSourceRequestType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
