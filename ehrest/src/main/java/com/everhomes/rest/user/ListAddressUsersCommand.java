package com.everhomes.rest.user;

import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>type: 类型，0-家庭、1-公司，参考{@link AddressUserType}</li>
 *     <li>status: status  {@link GroupMemberStatus}</li>
 *     <li>workPlatformFlag: 是否开启工作台标志，参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ListAddressUsersCommand {
    private Byte type;
    private Byte status;
    private Byte workPlatformFlag;

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getWorkPlatformFlag() {
        return workPlatformFlag;
    }

    public void setWorkPlatformFlag(Byte workPlatformFlag) {
        this.workPlatformFlag = workPlatformFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}