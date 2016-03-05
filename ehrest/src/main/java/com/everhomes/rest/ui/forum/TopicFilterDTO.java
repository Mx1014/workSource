// @formatter:off
package com.everhomes.rest.ui.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>论坛帖子查询条件过滤器
 * <li>name: 显示名称</li>
 * <li>avatar: 显示头像URI</li>
 * <li>avatarUrl: 显示头像URL</li>
 * <li>actionUrl: 点击触发的链接，与标准HTTP请求URL一致（需要补充上前面那段Host），如果值没有或没空则说明不能点击</li>
 * <li>isDefault: 默认触发该过滤器</li>
 * <li>isLeaf: 是否叶子节点，非叶子节点则可折叠</li>
 * </ul>
 */
public class TopicFilterDTO {
    private String name;
    private String avatar;
    private String avatarUrl;
    private String actionUrl;
    private boolean isDefault;
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

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
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
