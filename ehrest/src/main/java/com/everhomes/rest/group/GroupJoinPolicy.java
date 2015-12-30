// @formatter:off
package com.everhomes.rest.group;

/**
 * <p>组加入策略:</p>
 * <ul>
 * <li>FREE: 自由加入，不需要审批 </li>
 * <li>NEED_APPROVE: 需要审批 </li>
 * <li>INVITE_ONLY: 邀请加入，不需要审批 </li>
 * </ul>
 */
public enum GroupJoinPolicy {
    FREE(0), NEED_APPROVE(1), INVITE_ONLY(2);
    
    private int code;
    private GroupJoinPolicy(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return this.code;
    }
    
    public static GroupJoinPolicy fromCode(Integer code) {
        if(code == null)
            return null;
        
        switch(code.intValue()) {
        case 0 :
            return FREE;
        
        case 1 :
            return NEED_APPROVE;
            
        case 2 :
            return INVITE_ONLY;
            
        default :
            break;
        }
        
        return null;
    }
}
