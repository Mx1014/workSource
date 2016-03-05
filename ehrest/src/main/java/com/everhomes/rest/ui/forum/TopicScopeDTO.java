// @formatter:off
package com.everhomes.rest.ui.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>论坛发帖范围
 * <li>name: 显示名称</li>
 * <li>avatar: 显示头像URI</li>
 * <li>avatarUrl: 显示头像URL</li>
 * <li>description: 范围描述</li>
 * <li>forumId: 论坛ID</li>
 * <li>isLeaf: 是否叶子节点，非叶子节点则可折叠</li>
 * </ul>
 */
public class TopicScopeDTO {
    private String name;
    private String avatar;
    private String avatarUrl;
    private String description;
    private Long forumId;
    private boolean isLeaf;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
