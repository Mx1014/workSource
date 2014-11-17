package com.everhomes.user.rest;

public class LogonByTokenCommand {
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
