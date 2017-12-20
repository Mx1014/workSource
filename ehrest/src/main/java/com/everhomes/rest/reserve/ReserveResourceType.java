// @formatter:off
package com.everhomes.rest.reserve;

/**
 * <p>归属类型</p>
 * <ul>
 * <li>VIP_PARKING("vip_parking"): vip车位预约</li>
 * </ul>
 */
public enum ReserveResourceType {
    VIP_PARKING("vip_parking");

    private String code;
    private ReserveResourceType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ReserveResourceType fromCode(String code) {
        if(code != null) {
            ReserveResourceType[] values = ReserveResourceType.values();
            for(ReserveResourceType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
