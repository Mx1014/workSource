// @formatter:off
package com.everhomes.rest.ui.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>论坛发帖范围
 * <li>id: 发帖范围的ID，用于标识发帖范围，顺序由代码逻辑决定</li>
 * <li>parentId: 发帖范围的父亲ID，用于标识发帖范围的层级，父亲ID为0则没有上一层</li>
 * <li>name: 显示名称</li>
 * <li>avatar: 显示头像URI</li>
 * <li>avatarUrl: 显示头像URL</li>
 * <li>description: 范围描述</li>
 * <li>sceneToken: 场景token</li>
 * <li>forumId: 论坛ID</li>
 * <li>targetTag: 帖子接收者标签，该标签仍然需要客户端填写，参考{@link com.everhomes.rest.forum.PostEntityTag}</li>
 * <li>leafFlag: 是否叶子节点，非叶子节点则可折叠，{@link com.everhomes.rest.ui.forum.SelectorBooleanFlag}</li>
 * <li>defaultFlag: 默认触发该过滤器，{@link com.everhomes.rest.ui.forum.SelectorBooleanFlag}</li>
 * </ul>
 */
public class TopicScopeDTO {
    private Long id;
    private Long parentId;
    private String name;
    private String avatar;
    private String avatarUrl;
    private String description;
    private String sceneToken;
    private Long forumId;
    private String targetTag;
    private Byte leafFlag;
    private Byte defaultFlag;
    private Long visibleRegionId;
    
    private Byte visibleRegionType;

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

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public String getTargetTag() {
        return targetTag;
    }

    public void setTargetTag(String targetTag) {
        this.targetTag = targetTag;
    }

    public Byte getLeafFlag() {
        return leafFlag;
    }

    public void setLeafFlag(Byte leafFlag) {
        this.leafFlag = leafFlag;
    }

    public Byte getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(Byte defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	public Long getVisibleRegionId() {
		return visibleRegionId;
	}

	public void setVisibleRegionId(Long visibleRegionId) {
		this.visibleRegionId = visibleRegionId;
	}

	public Byte getVisibleRegionType() {
		return visibleRegionType;
	}

	public void setVisibleRegionType(Byte visibleRegionType) {
		this.visibleRegionType = visibleRegionType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
