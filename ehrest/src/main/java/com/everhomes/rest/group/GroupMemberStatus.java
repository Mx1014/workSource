// @formatter:off
package com.everhomes.rest.group;

/**
 * <ul>group成员状态
 * <li>INACTIVE: 0-无效的</li>
 * <li>WAITING_FOR_APPROVAL: 1-等待批准加入（自己主动申请加入、且未被批准加入时为此状态）</li>
 * <li>WAITING_FOR_ACCEPTANCE: 2-邀请后等待被邀请人同意加入（邀请别人加入、且被邀请人未同时加入时为此状态）</li>
 * <li>ACTIVE: 3-正常成员</li>
 * <li>REJECT: 4-被驳回</li>
 * </ul>
 */
public enum GroupMemberStatus {
    INACTIVE((byte)0), WAITING_FOR_APPROVAL((byte)1), WAITING_FOR_ACCEPTANCE((byte)2), ACTIVE((byte)3), REJECT((byte)4);
    
    private byte code;
    private GroupMemberStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static GroupMemberStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return INACTIVE;
            
        case 1:
            return WAITING_FOR_APPROVAL;
            
        case 2:
            return WAITING_FOR_ACCEPTANCE;
            
        case 3:
            return ACTIVE;
            
        case 4:
            return REJECT;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
