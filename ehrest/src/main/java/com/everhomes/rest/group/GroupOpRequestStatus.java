// @formatter:off
package com.everhomes.rest.group;

/**
 * <p>成为管理员申请的状态</p>
 * <ul>
 * <li>NONE: 无记录</li>
 * <li>REQUESTING: 申请中</li>
 * <li>ACCEPTED: 已批准</li>
 * <li>REJECTED: 已拒绝</li>
 * </ul>
 */
public enum GroupOpRequestStatus {
    NONE((byte)0), REQUESTING((byte)1), ACCEPTED((byte)2), REJECTED((byte)3);
    
    private byte code;
    
    private GroupOpRequestStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static GroupOpRequestStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return NONE;
        case 1 :
            return REQUESTING;
        case 2 :
            return ACCEPTED;
        case 3 :
            return REJECTED;
            
        default :
            break;
        }
        
        return null;
    }
}
