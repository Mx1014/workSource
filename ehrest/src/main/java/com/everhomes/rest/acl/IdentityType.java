package com.everhomes.rest.acl;

/**
 * <p>身份</p>
 * <ul>
 * <li>MANAGE: 管理人员</li>
 * <li>ordinary: 普通人员</li>
 * </ul>
 */
public enum IdentityType {

    MANAGE("manage"), ORDINARY("ordinary");

    private String code;
    private IdentityType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static IdentityType fromCode(String code) {
    	IdentityType[] values = IdentityType.values();
        for(IdentityType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}