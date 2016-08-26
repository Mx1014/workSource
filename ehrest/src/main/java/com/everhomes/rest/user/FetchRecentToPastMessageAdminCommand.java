package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class FetchRecentToPastMessageAdminCommand {
    private Integer namespaceId;
    private Long appId;
    private Long anchor;
    private Integer count;
    private Long userId;
    private Integer loginId;

    public FetchRecentToPastMessageAdminCommand() {
    }
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getLoginId() {
        return loginId;
    }

    public void setLoginId(Integer loginId) {
        this.loginId = loginId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getAnchor() {
        return anchor;
    }

    public void setAnchor(Long anchor) {
        this.anchor = anchor;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
