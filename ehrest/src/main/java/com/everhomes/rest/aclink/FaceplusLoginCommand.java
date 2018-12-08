// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>username[String]：邮箱 face++提供</li>
 * <li>password[String]: 密码 face++提供</li>
 * </ul>
 *
 */
public class FaceplusLoginCommand {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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