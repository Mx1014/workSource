// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>月卡过期时充值，车进场标志
 * <li>NOT_PRESENCE((byte)0): 不在场</li>
 * <li>PRESENCE_BEFORE_VALIDITY((byte)1): 在场，在月卡有效之前进场</li>
 * <li>PRESENCE_AFTER_VALIDITY((byte)2): 在场, 在月卡过期之后进场</li>
 * </ul>
 */
public enum ParkingCarPresenceFlag {
    NOT_PRESENCE((byte)0), PRESENCE_BEFORE_VALIDITY((byte)1), PRESENCE_AFTER_VALIDITY((byte)2);

    private byte code;

    private ParkingCarPresenceFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingCarPresenceFlag fromCode(Byte code) {
        if(code != null) {
            ParkingCarPresenceFlag[] values = ParkingCarPresenceFlag.values();
            for(ParkingCarPresenceFlag value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
