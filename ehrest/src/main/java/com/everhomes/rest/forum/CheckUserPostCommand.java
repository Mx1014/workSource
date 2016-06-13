// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 命名空间ID</li>
 * <li>communityId: 小区ID</li>
 * <li>forumId: 论坛ID</li>
 * <li>categoryId: 类型ID</li>
 * <li>timestamp: 客户端最新帖子创建时间</li>
 * </ul>
 */
public class CheckUserPostCommand {
    private Integer namespaceId;
    private Long communityId;
    private Long forumId;
    private Long categoryId;
    private Long timestamp;
    
    public CheckUserPostCommand() {
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
