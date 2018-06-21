// @formatter:off
package com.everhomes.rest.visitorsys;

/**
 * <p>设备类型</p>
 * <ul>
 * <li>IPAD("ipad"): ipad</li>
 * <li>PRINTER("printer"): 打印机</li>
 * </ul>
 */
public enum VisitorsysDeviceType {
    IPAD("ipad"),
    PRINTER("printer");

    private String code;
    VisitorsysDeviceType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static VisitorsysDeviceType fromCode(String code) {
        if(code != null) {
            VisitorsysDeviceType[] values = VisitorsysDeviceType.values();
            for(VisitorsysDeviceType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
