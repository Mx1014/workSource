// @formatter:off
package com.everhomes.rest.community.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>communityId: 被审核小区Id</li>
 * <li>operatorRole: 操作者角色</li>
 * <li>reason: 通过原因</li>
 * </ul>
 */
public class RejectCommunityAdminCommand {
    @NotNull
    private Long communityId;
    private Long operatorRole;
    
    private String reason;
    
    public RejectCommunityAdminCommand() {
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOperatorRole() {
        return operatorRole;
    }

    public void setOperatorRole(Long operatorRole) {
        this.operatorRole = operatorRole;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
