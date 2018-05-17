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
    private Byte supperAdminFlag;

    public Byte getSupperAdminFlag() {
        return supperAdminFlag;
    }

    public void setSupperAdminFlag(Byte supperAdminFlag) {
        this.supperAdminFlag = supperAdminFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
