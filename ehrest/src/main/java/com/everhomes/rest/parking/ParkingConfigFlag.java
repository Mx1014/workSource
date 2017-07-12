// @formatter:off
package com.everhomes.rest.parking;

/**
 * <li>NOTSUPPORT(0): 不支持</li>
 * <li>SUPPORT(1): 支持</li>
 * </ul>
 */
public enum ParkingConfigFlag {
	NOTSUPPORT((byte)0), SUPPORT((byte)1);

    private byte code;

    private ParkingConfigFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingConfigFlag fromCode(Byte code) {
        if(code != null) {
            ParkingConfigFlag[] values = ParkingConfigFlag.values();
            for(ParkingConfigFlag value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
