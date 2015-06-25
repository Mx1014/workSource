// @formatter:off
package com.everhomes.group;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId：group id</li>
 * <li>memberUuid：group成员UUID</li>
 * </ul>
 */
public class GetGroupMemberSnapshotCommand {
    private Long groupId;
    
    private String memberUuid;

    public GetGroupMemberSnapshotCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getMemberUuid() {
        return memberUuid;
    }

    public void setMemberUuid(String memberUuid) {
        this.memberUuid = memberUuid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
