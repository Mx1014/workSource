// @formatter:off
package com.everhomes.rest.common;


/**
 * <p>门户类型:</p>
 * <ul>
 * <li>ZUOLIN：左邻后台管理系统</li>
 * <li>PM: 物业公司系统</li>
 * <li>ENTERPRISE: 普通企业公司系统</li>
 * <li>USER: 用户系统</li>
 * </ul>
 */
public enum PortalType {
    ZUOLIN("zuolin"),
    PM("pm"),
    ENTERPRISE("enterprise"),
    USER("user");

    private String code;

    private PortalType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PortalType fromCode(String code) {
        if(code == null)
            return null;
        for (PortalType portalType : PortalType.values()) {
            if (portalType.getCode().equals(code)) {
                return portalType;
            }
        }
        return null;
    }
}
