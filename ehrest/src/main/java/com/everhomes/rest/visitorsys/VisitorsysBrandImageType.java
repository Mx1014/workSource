// @formatter:off
package com.everhomes.rest.visitorsys;

/**
 * <p>品牌形象字段</p>
 * <ul>
 * <li>NULL("null"),: 无</li>
 * <li>COMPANY_SHORT_NAME("company_short_name"): 公司简称</li>
 * <li>LOGO("logo"): logo</li>
 * </ul>
 */
public enum VisitorsysBrandImageType {
    NULL("null"),
    COMPANY_SHORT_NAME("company_short_name"),
    LOGO("logo");

    private String code;
    VisitorsysBrandImageType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static VisitorsysBrandImageType fromCode(String code) {
        if(code != null) {
            VisitorsysBrandImageType[] values = VisitorsysBrandImageType.values();
            for(VisitorsysBrandImageType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
