package com.everhomes.rest.aclink;

import java.util.List;
import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>privilegeType[Boolean]:权限检验状态 true有权限 false缺失参数 抛出异常无权限</li>
 * </ul>
 */
public class CheckMobilePrivilegeResponse {
    public Boolean privilegeType;

    public Boolean getPrivilegeType() {
        return privilegeType;
    }

    public void setPrivilegeType(Boolean privilegeType) {
        this.privilegeType = privilegeType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

