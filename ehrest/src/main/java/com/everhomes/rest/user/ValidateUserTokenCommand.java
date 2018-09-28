package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>token: token</li>
 * </ul>
 */
public class ValidateUserTokenCommand {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
