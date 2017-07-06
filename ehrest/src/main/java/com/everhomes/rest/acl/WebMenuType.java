package com.everhomes.rest.acl;

/**
 * <p>菜单类型</p>
 * <ul>
 * <li>ZUOLIN: 左邻web系统菜单</li>
 * <li>PARK: 园区web系统菜单</li>
 * <li>ORGANIZATION: 企业web系统菜单</li>
 * </ul>
 */
public enum WebMenuType {
	
    ZUOLIN("zuolin"), PARK("park"), ORGANIZATION("organization");
    
    private String code;
    private WebMenuType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static WebMenuType fromCode(String code) {
    	WebMenuType[] values = WebMenuType.values();
        for(WebMenuType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}