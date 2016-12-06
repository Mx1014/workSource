// @formatter:off
package com.everhomes.rest.parking;

/**
 * <li>NOTSUPPORT(0): 不支持</li>
 * <li>SUPPORT(1): 支持</li>
 * </ul>
 */
public enum ParkingSupportRechargeStatus {
	NOTSUPPORT((byte)0), SUPPORT((byte)1);
    
    private byte code;
    
    private ParkingSupportRechargeStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingSupportRechargeStatus fromCode(Byte code) {
        if(code != null) {
            ParkingSupportRechargeStatus[] values = ParkingSupportRechargeStatus.values();
            for(ParkingSupportRechargeStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
