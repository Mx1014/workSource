package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>supperAdminFlag: 1 表示是超级管理员，0 表示不是超级管理员</li>
 * </ul>
 * @author janson
 *
 */
public class GetAdminTypeResponse {
    private Byte superAdminFlag;

    public Byte getSuperAdminFlag() {
        return superAdminFlag;
    }

    public void setSuperAdminFlag(Byte supperAdminFlag) {
        this.superAdminFlag = supperAdminFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
