// @formatter:off
package com.everhomes.rest.ui;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>scene: 场景标识，参考{@link com.everhomes.rest.ui.SceneType}</li>
 * <li>entityType: 实体类型</li>
 * <li>entityId: 实体ID</li>
 * <li>name: 显示名称</li>
 * <li>avatar: 显示头像URI</li>
 * <li>avatarUrl: 显示头像URL</li>
 * </ul>
 */
public class SceneDTO {
    private String scene;
    private String entityType;
    private Long entityId;
    private String name;
    private String avatar;
    private String avatarUrl;

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
