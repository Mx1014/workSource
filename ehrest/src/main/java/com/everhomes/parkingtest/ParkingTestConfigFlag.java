// @formatter:off
package com.everhomes.parkingtest;

/**
 * <li>NOTSUPPORT(0): 不支持</li>
 * <li>SUPPORT(1): 支持</li>
 * </ul>
 */
public enum ParkingTestConfigFlag {
	NOTSUPPORT((byte)0), SUPPORT((byte)1);

    private byte code;

    private ParkingTestConfigFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingTestConfigFlag fromCode(Byte code) {
        if(code != null) {
            ParkingTestConfigFlag[] values = ParkingTestConfigFlag.values();
            for(ParkingTestConfigFlag value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
