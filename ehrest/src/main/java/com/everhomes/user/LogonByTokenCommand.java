// @formatter:off
package com.everhomes.user;

import javax.validation.constraints.NotNull;

public class LogonByTokenCommand {
    @NotNull
    private String loginToken;
    
    public LogonByTokenCommand() {
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }
}
