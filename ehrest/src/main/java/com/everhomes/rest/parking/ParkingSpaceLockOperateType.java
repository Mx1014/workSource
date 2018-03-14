// @formatter:off
package com.everhomes.rest.parking;

/**
 * <p>vip车位 锁操作类型</p>
 * <ul>
 * <li>UP("up"): 升起车锁</li>
 * <li>DOWN("down"): 降下车锁</li>
 * </ul>
 */
public enum ParkingSpaceLockOperateType {
    UP((byte)1), DOWN((byte)2);

    private byte code;
    private ParkingSpaceLockOperateType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingSpaceLockOperateType fromCode(Byte code) {
        if(code != null) {
            ParkingSpaceLockOperateType[] values = ParkingSpaceLockOperateType.values();
            for(ParkingSpaceLockOperateType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
