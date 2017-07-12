// @formatter:off
package com.everhomes.rest.parking;

/**
 * <li>NOTSUPPORT(0): 不支持</li>
 * <li>SUPPORT(1): 支持</li>
 * </ul>
 */
public enum ParkingSupportRequestConfigStatus {
	NOTSUPPORT((byte)0), SUPPORT((byte)1);
    
    private byte code;
    
    private ParkingSupportRequestConfigStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingSupportRequestConfigStatus fromCode(Byte code) {
        if(code != null) {
            ParkingSupportRequestConfigStatus[] values = ParkingSupportRequestConfigStatus.values();
            for(ParkingSupportRequestConfigStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
