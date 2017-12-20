// @formatter:off
package com.everhomes.rest.parking;

/**
 * <p>vip车位 锁状态</p>
 * <ul>
 * <li>OPEN((byte)1): 开放</li>
 * <li>CLOSE((byte)0): 关闭</li>
 * </ul>
 */
public enum ParkingSpaceStatus {
    OPEN((byte)1), CLOSE((byte)0);

    private byte code;
    private ParkingSpaceStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingSpaceStatus fromCode(Byte code) {
        if(code != null) {
            ParkingSpaceStatus[] values = ParkingSpaceStatus.values();
            for(ParkingSpaceStatus value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
