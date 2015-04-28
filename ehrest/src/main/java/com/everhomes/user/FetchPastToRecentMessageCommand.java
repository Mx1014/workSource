// @formatter:off
package com.everhomes.user;

import com.everhomes.util.StringHelper;

public class FetchPastToRecentMessageCommand {
    private Integer namespaceId;
    private Long appId;
    private Long anchor;
    private Integer count;
    private Byte removeOld;

    public FetchPastToRecentMessageCommand() {
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

    public Byte getRemoveOld() {
        return removeOld;
    }

    public void setRemoveOld(Byte removeOld) {
        this.removeOld = removeOld;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
