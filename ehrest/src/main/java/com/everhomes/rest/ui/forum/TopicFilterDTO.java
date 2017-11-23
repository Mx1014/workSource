// @formatter:off
package com.everhomes.rest.ui.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>论坛帖子查询条件过滤器
 * <li>id: 过滤器的ID，用于标识过滤器，顺序由代码逻辑决定</li>
 * <li>parentId: 过滤器的父亲ID，用于标识过滤器的层级，父亲ID为0则没有上一层</li>
 * <li>name: 显示名称</li>
 * <li>description: 描述</li>
 * <li>avatar: 显示头像URI</li>
 * <li>avatarUrl: 显示头像URL</li>
 * <li>actionUrl: 点击触发的链接，与标准HTTP请求URL一致（需要补充上前面那段Host），如果值没有或没空则说明不能点击</li>
 * <li>defaultFlag: 默认触发该过滤器，{@link com.everhomes.rest.ui.forum.SelecterBooleanFlag}</li>
 * <li>leafFlag: 是否叶子节点，非叶子节点则可折叠，{@link com.everhomes.rest.ui.forum.SelecterBooleanFlag}</li>
 * <li>forumId: 论坛Id，新建帖子的选择范围的时候可以参考这里的forumId初始化默认范围</li>
 * </ul>
 */
public class TopicFilterDTO {
    private Long id;
    private Long parentId;
    private String name;
    private String description;
    private String avatar;
    private String avatarUrl;
    private String actionUrl;
    private Byte defaultFlag;
    private Byte leafFlag;
    private Long forumId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Byte getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Byte defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public Byte getLeafFlag() {
        return leafFlag;
    }

    public void setLeafFlag(Byte leafFlag) {
        this.leafFlag = leafFlag;
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
