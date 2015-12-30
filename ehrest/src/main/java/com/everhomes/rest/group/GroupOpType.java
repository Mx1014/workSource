// @formatter:off
package com.everhomes.rest.group;

/**
 * <p>成为group管理员申请的类型</p>
 * <ul>
 * <li>REQUEST_ADMIN_ROLE: 自己申请成为管理员</li>
 * <li>INVITE_ADMIN_ROLE: 被邀请成为管理员</li>
 * </ul>
 */
public enum GroupOpType {
    REQUEST_ADMIN_ROLE((byte)1), INVITE_ADMIN_ROLE((byte)2);
    
    private byte code;
    private GroupOpType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static GroupOpType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 1:
            return REQUEST_ADMIN_ROLE;
        case 2:
            return INVITE_ADMIN_ROLE;
        default :
            break;
        }
        
        return null;
    }
}
