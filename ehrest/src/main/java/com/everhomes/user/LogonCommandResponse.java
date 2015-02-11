// @formatter:off
package com.everhomes.user;

import java.util.List;

import com.everhomes.discover.ItemType;

public class LogonCommandResponse {
    private long uid;
    private String loginToken;

    @ItemType(String.class)
    private List<String> accessPoints;
    
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
    
    public List<String> getAccessPoints() {
        return this.accessPoints;
    }
    
    public void setAccessPoints(List<String> accessPoints) {
        this.accessPoints = accessPoints;
    }
}
