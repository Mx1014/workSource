package com.everhomes.rest.enterprisemoment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>isAdmin: 0-不是管理员 1-是管理员</li>
 * </ul>
 */
public class CheckAdminResponse {
    private Byte isAdmin;

    public Byte getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Byte isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
