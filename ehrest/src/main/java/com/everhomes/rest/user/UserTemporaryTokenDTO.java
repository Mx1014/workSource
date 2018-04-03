// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

import java.io.Serializable;

/**
 *
 */
public class UserTemporaryTokenDTO {

    private Long userId;
    private Integer namespaceId;
    private Long startTime;
    private Long interval;
    private String info;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
