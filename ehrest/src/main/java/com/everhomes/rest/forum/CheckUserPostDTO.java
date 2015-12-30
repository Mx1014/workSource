// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 命名空间ID</li>
 * <li>communityId: 小区ID</li>
 * <li>forumId: 论坛ID</li>
 * <li>timestamp: 客户端最新帖子创建时间</li>
 * <li>status: 是否有新帖状态，参考{@link com.everhomes.rest.forum.CheckUserPostStatus}</li>
 * </ul>
 */
public class CheckUserPostDTO {
    private Integer namespaceId;
    private Long communityId;
    private Long forumId;
    private Long timestamp;
    private Byte status;
    
    public CheckUserPostDTO() {
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
