// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>
 * <li>NOTDELETE(0): 不支持临时缴费</li>
 * <li>DELETE(1): 支持临时缴费</li>
 * </ul>
 */
public enum ParkingTempFeeFlag {
    NOTSUPPORT((byte)0), SUPPORT((byte)1);
    
    private byte code;
    
    private ParkingTempFeeFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingTempFeeFlag fromCode(Byte code) {
        if(code != null) {
            ParkingTempFeeFlag[] values = ParkingTempFeeFlag.values();
            for(ParkingTempFeeFlag value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
