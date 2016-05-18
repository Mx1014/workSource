// @formatter:off
package com.everhomes.rest.parking;

/**
 * <p>归属类型</p>
 * <ul>
 * <li>COMMUNITY("community"): 小区</li>
 * </ul>
 */
public enum ParkingOwnerType {
    COMMUNITY("community");
    
    private String code;
    private ParkingOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ParkingOwnerType fromCode(String code) {
        if(code != null) {
            ParkingOwnerType[] values = ParkingOwnerType.values();
            for(ParkingOwnerType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
