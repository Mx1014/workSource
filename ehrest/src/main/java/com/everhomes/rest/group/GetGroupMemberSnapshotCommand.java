// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId：group id</li>
 * <li>memberId：group成员ID</li>
 * </ul>
 */
public class GetGroupMemberSnapshotCommand {
    private Long groupId;
    
    private Long memberId;

    public GetGroupMemberSnapshotCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
