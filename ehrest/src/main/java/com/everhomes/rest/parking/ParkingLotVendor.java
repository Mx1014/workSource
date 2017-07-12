// @formatter:off
package com.everhomes.rest.parking;

/**
 * <p>停车场厂商</p>
 * <ul>
 * <li>BOSIGAO("BOSIGAO"): 博思高</li>
 * <li>BOSIGAO2("BOSIGAO2"): 博思高新接口</li>
 * <li>ETCP("ETCP"): ETCP</li>
 * <li>KETUO("KETUO"): 科拓</li>
 * </ul>
 */
public enum ParkingLotVendor {
    BOSIGAO("BOSIGAO"), ETCP("ETCP"), BOSIGAO2("BOSIGAO2"), KETUO("KETUO"), KETUO2("KETUO2"), WANKE("WANKE"),
    INNOSPRING("INNOSPRING");
    
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
