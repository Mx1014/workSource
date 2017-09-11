package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>password: 密码</li>
 * </ul>
 */
public class VerifyPersonnelByPasswordCommand {

    private String password;

    public VerifyPersonnelByPasswordCommand() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
