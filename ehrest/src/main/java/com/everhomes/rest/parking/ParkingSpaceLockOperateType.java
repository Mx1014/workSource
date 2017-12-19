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
    UP("up"), DOWN("down");

    private String code;
    private ParkingSpaceLockOperateType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ParkingSpaceLockOperateType fromCode(String code) {
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
