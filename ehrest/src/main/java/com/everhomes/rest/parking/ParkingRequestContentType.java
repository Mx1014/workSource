// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>
 * <li>IDCARD(1): 身份证</li>
 * <li>TRAVEL_LICENSE(2): 行驶证</li>
 * <li>DRIVING_LICENSE(3): 驾驶证</li>
 * </ul>
 */
public enum ParkingRequestContentType {
    
    ID_CARD((byte)1), TRAVEL_LICENSE((byte)2), DRIVING_LICENSE((byte)3);
    
    private byte code;
    
    private ParkingRequestContentType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingRequestContentType fromCode(Byte code) {
        if(code != null) {
            ParkingRequestContentType[] values = ParkingRequestContentType.values();
            for(ParkingRequestContentType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
