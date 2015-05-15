// @formatter:off
package com.everhomes.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>familyId: 家庭Id</li>
 * <li>memberUid: 成员Id</li>
 * <li>reason: 拒绝原因（可选）</li>
 * <li>operatorRole: 操作者角色</li>
 * </ul>
 */
public class RejectMemberCommand {
    private Long familyId;
    private Long memberUid;
    private String reason;
    private Byte operatorRole;
    
    public RejectMemberCommand() {
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }
    
    public Long getMemberUid() {
        return memberUid;
    }

    public void setMemberUid(Long memberUid) {
        this.memberUid = memberUid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Byte getOperatorRole() {
        return operatorRole;
    }

    public void setOperatorRole(Byte operatorRole) {
        this.operatorRole = operatorRole;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
