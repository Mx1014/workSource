// @formatter:off
package com.everhomes.rest.parking;

/**
 * <p>vip车位 锁操作类型</p>
 * <ul>
 * <li>UP("up"): 升起车锁</li>
 * <li>DOWN("down"): 降下车锁</li>
 * </ul>
 */
public enum ParkingSpaceLockOperateUserType {
    RESERVE_PERSON((byte)1), PLATE_OWNER((byte)2);

    private byte code;
    private ParkingSpaceLockOperateUserType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingSpaceLockOperateUserType fromCode(Byte code) {
        if(code != null) {
            ParkingSpaceLockOperateUserType[] values = ParkingSpaceLockOperateUserType.values();
            for(ParkingSpaceLockOperateUserType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
