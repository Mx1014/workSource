// @formatter:off
package com.everhomes.rest.parking;

/**
 * <p>vip车位 锁状态</p>
 * <ul>
 * <li>UP("up"): 车锁升起</li>
 * <li>DOWN("down"): 车锁降下</li>
 * </ul>
 */
public enum ParkingSpaceLockStatus {
    UP("up"), DOWN("down");

    private String code;
    private ParkingSpaceLockStatus(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ParkingSpaceLockStatus fromCode(String code) {
        if(code != null) {
            ParkingSpaceLockStatus[] values = ParkingSpaceLockStatus.values();
            for(ParkingSpaceLockStatus value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
