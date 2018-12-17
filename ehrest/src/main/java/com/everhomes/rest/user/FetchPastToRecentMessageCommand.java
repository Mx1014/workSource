// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 命名空间</li>
 * <li>appId: 应用ID</li>
 * <li>anchor: 消息游标</li>
 * <li>count: 获取消息的数量</li>
 * <li>removeOld: 是否删除旧的消息</li>
 * <li>userId: 测试专用，默认为 null</li>
 * <li>loginId: 测试专用，默认为 null</li>
 * </ul>
 *
 */
public class FetchPastToRecentMessageCommand {
    private Integer namespaceId;
    private Long appId;
    private Long anchor;
    private Integer count;
    private Byte removeOld;

    private Long userId;
    private Integer loginId;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
