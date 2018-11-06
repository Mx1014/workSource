// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>doorId:  门禁Id</li>
 * </ul>
 */
public class DeleteDoorGroupRelCommand {

    private Long doorId;
    private Long groupId;

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
