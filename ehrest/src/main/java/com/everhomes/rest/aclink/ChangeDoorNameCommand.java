// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>doorId：门禁Id</li>
 * <li>doorName：要修改的门禁名称</li>
 * <li>doorAddress：门禁地址</li>
 * <li>doorDescription：门禁说明</li>
 * </ul>
 */
public class ChangeDoorNameCommand {
    private Long doorId;
    private String name;
    private String doorAddress;
    private String doorDescription;

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoorAddress() {
        return doorAddress;
    }

    public void setDoorAddress(String doorAddress) {
        this.doorAddress = doorAddress;
    }

    public String getDoorDescription() {
        return doorDescription;
    }

    public void setDoorDescription(String doorDescription) {
        this.doorDescription = doorDescription;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
