// @formatter:off
package com.everhomes.rest.parking;

/**
 * <p>停车场厂商</p>
 * <ul>
 * <li>DEFAULT("DEFAULT"): 默认为左邻，停车场数据放左邻、且ID由左邻产生时为默认</li>
 * <li>BOSIGAO("BOSIGAO"): 博思高</li>
 * <li>ETCP("ETCP"): ETCP</li>
 * </ul>
 */
public enum ParkingLotVendor {
    DEFAULT("DEFAULT"), BOSIGAO("BOSIGAO"), ETCP("ETCP");
    
    private String code;
    private ParkingLotVendor(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ParkingLotVendor fromCode(String code) {
        if(code != null) {
            ParkingLotVendor[] values = ParkingLotVendor.values();
            for(ParkingLotVendor value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
