// @formatter:off
package com.everhomes.messaging;

/**
 * <p>Meta对象类型</p>
 * <ul>
 * <li>USER: 普通用户</li>
 * <li>BIZ: 商家</li>
 * <li>PM: 物业</li>
 * <li>GARC: 业委，Government Agency - Resident Committee</li>
 * <li>GANC: 居委，Government Agency - Neighbor Committee</li>
 * <li>GAPS: 公安，Government Agency - Police Station</li>
 * </ul>
 */
public enum MetaObjectType {
    GROUP_REQUEST_TO_JOIN("group.requestToJoin"), 
    GROUP_INVITE_TO_JOIN("group.inviteToJoin"), 
    GROUP_REQUEST_TO_BE_ADMIN("group.requestToBeAdmin"), 
    GROUP_INVITE_TO_BE_ADMIN("group.inviteToBeAdmin");
    
    private String code;
    private MetaObjectType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static MetaObjectType fromCode(String code) {
        MetaObjectType[] values = MetaObjectType.values();
        for(MetaObjectType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
