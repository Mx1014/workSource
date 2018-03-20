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
    UP((byte)1,"up"), DOWN((byte)2,"down");

    private byte code;
    private String status;
    private ParkingSpaceLockOperateType(byte code,String status) {
        this.code = code;
        this.status = status;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public String getStatus() {
        return this.status;
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
