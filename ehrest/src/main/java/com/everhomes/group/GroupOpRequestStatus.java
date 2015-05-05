// @formatter:off
package com.everhomes.group;

/**
 * <p>成为管理员申请的状态</p>
 * <ul>
 * <li>REQUESTING: 申请中</li>
 * <li>ACCEPTED: 已批准</li>
 * <li>REJECTED: 已拒绝</li>
 * </ul>
 */
public enum GroupOpRequestStatus {
    REQUESTING((byte)0), ACCEPTED((byte)1), REJECTED((byte)2);
    
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
            return REQUESTING;
        case 1 :
            return ACCEPTED;
        case 2 :
            return REJECTED;
            
        default :
            break;
        }
        
        return null;
    }
}
