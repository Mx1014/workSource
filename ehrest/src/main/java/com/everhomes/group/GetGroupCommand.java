// @formatter:off
package com.everhomes.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupUuid：group uuid</li>
 * </ul>
 */
public class GetGroupCommand {
    @NotNull
    private String groupUuid;

    public GetGroupCommand() {
    }

    public String getGroupUuid() {
        return groupUuid;
    }

    public void setGroupUuid(String groupUuid) {
        this.groupUuid = groupUuid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
