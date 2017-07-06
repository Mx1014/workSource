// @formatter:off
package com.everhomes.rest.ui.user;

/**
 * 
 * <ul>
 * <li>DEFAULT("default") : 老的认证提交方式,非表单</li>
 * <li>FORM("form") : 表单提交的认证方式</li>
 * </ul>
 *
 *  @author:dengs 2017年7月6日
 */
public enum AuthSumbitType {
	DEFAULT("default"),FORM("form");
	
    private String code;
    
    private AuthSumbitType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static AuthSumbitType fromCode(String code) {
        if(code != null) {
        	AuthSumbitType[] values = AuthSumbitType.values();
            for(AuthSumbitType value : values) {
                if(value.getCode().equals(code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
