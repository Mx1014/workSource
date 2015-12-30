// @formatter:off
package com.everhomes.rest.family;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type: 类型，详情{@link com.everhomes.rest.family.BaseCommand}</li>
 * <li>id: 类型对应的Id，详情{@link com.everhomes.rest.family.BaseCommand}</li>
 * <li>memberUid: 成员Id</li>
 * <li>reason: 拒绝原因（可选）</li>
 * <li>operatorRole: 操作者角色</li>
 * </ul>
 */
public class RejectMemberCommand extends BaseCommand{
    @NotNull
    private Long memberUid;
    private String reason;
    private Long operatorRole;
    
    public RejectMemberCommand() {
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

    public Long getOperatorRole() {
        return operatorRole;
    }

    public void setOperatorRole(Long operatorRole) {
        this.operatorRole = operatorRole;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
