// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>doorId: 门禁id</li>
 * </ul>
 */
public class ListDoorManagementCommand {
    @NotNull
    private Long doorId;

    @NotNull
    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(@NotNull Long doorId) {
        this.doorId = doorId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

