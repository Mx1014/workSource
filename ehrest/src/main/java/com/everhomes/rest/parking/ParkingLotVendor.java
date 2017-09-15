// @formatter:off
package com.everhomes.rest.parking;

/**
 * <p>停车场厂商</p>
 * <ul>
 * <li>BOSIGAO("BOSIGAO"): 博思高（科技园）</li>
 * <li>BOSIGAO2("BOSIGAO2"): 博思高新接口(深业)</li>
 * <li>KETUO("KETUO"): 科拓（科兴）</li>
 * <li>KETUO2("KETUO"): 科拓（储能）</li>
 * <li>WANKE("WANKE"): 万科()</li>
 * <li>INNOSPRING("INNOSPRING"): 创源</li>
 * <li>JIN_YI("JIN_YI"): 金溢(清华信息港)</li>
 * </ul>
 */
public enum ParkingLotVendor {
    BOSIGAO("BOSIGAO"), BOSIGAO2("BOSIGAO2"), KETUO("KETUO"), KETUO2("KETUO2"), WANKE("WANKE"),
    INNOSPRING("INNOSPRING"), JIN_YI("JIN_YI"), XIAOMAO("XIAOMAO"), MYBAY("Mybay");
    
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
