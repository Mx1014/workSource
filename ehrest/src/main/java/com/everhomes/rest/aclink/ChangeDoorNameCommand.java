// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>doorId：门禁Id</li>
 * <li>doorName：要修改的门禁名称</li>
 * <li>doorAddress：门禁地址</li>
 * <li>doorExplain：门禁说明</li>
 * </ul>
 */
public class ChangeDoorNameCommand {
    private Long doorId;
    private String doorName;
    private String doorAddress;
    private String doorExplain;

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public String getDoorName() {
        return doorName;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }

    public String getDoorAddress() {
        return doorAddress;
    }

    public void setDoorAddress(String doorAddress) {
        this.doorAddress = doorAddress;
    }

    public String getDoorExplain() {
        return doorExplain;
    }

    public void setDoorExplain(String doorExplain) {
        this.doorExplain = doorExplain;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
