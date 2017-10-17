package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>isAdmin: 是否为管理员: 0-否 1-是</li>
 * </ul>
 */
public class CheckContactAdminResponse {

    private Byte isAdmin;

    public CheckContactAdminResponse() {
    }

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
