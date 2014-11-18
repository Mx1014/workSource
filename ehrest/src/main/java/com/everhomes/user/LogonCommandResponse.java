package com.everhomes.user;

public class LogonCommandResponse {
    private long uid;
    private String loginToken;
    
    public LogonCommandResponse() {
    }
    
    public LogonCommandResponse(long uid, String loginToken) {
        this.uid = uid;
        this.loginToken = loginToken;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }
}
