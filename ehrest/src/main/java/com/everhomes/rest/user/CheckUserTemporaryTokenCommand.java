package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * 检验用户临时token，标准是：1、是本系统加密的token，2、token未过期
 * <ul>
 *     <li>userToken: 用户临时token</li>
 * </ul>
 */
public class CheckUserTemporaryTokenCommand {
    @NotNull
    private String userToken;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
