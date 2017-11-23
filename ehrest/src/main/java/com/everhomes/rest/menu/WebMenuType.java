// @formatter:off
package com.everhomes.rest.menu;

/**
 * <ul>
 * <li>ZUOLIN：左邻运营后台管理菜单类型</li>
 * <li>PARK：园区后台管理菜单类型</li>
 * <li>ENTERPRISE：企业后台管理菜单类型</li>
 * </ul>
 */
public enum WebMenuType {

    ZUOLIN("zuolin"), PARK("park"), ENTERPRISE("enterprise");

    private String code;

    private WebMenuType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static WebMenuType fromCode(String code) {
        WebMenuType[] values = WebMenuType.values();
        for (WebMenuType value: values) {
            if(value.getCode().equals(code)){
                return value;
            }
        }
        return null;
    }
}
