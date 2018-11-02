// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>doorId: 门禁id</li>
 * <li>managerId：管理公司Id</li>
 * <li>managerType：管理公司类型</li>
 * </ul>
 */
public class AddDoorManagementCommand {
    @NotNull
    private Long doorId;
    @NotNull
    private Long managerId;
//    private String managerName;
    private Byte managerType;
    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    @NotNull
    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(@NotNull Long managerId) {
        this.managerId = managerId;
    }

    public void setManagerType(Byte managerType) {
        this.managerType = managerType;
    }

    public Byte getManagerType() {
        return managerType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

