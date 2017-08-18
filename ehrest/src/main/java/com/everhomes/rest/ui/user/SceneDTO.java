// @formatter:off
package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些</li>
 * <li>entityType: 实体类型，{@link com.everhomes.rest.user.UserCurrentEntityType}</li>
 * <li>entityContent: 实体内容，会根据具体实体类型有不同的JSON结构: </li>
 * <li> entityType为family时，参考{@link com.everhomes.rest.family.FamilyDTO}</li>
 * <li> entityType为organization时，参考{@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li> entityType为community_residential或community_commercial或community时，参考{@link com.everhomes.rest.address.CommunityDTO}</li>
 * <li>name: 显示名称</li>
 * <li>aliasName: 别名</li>
 * <li>avatar: 显示头像URI</li>
 * <li>avatarUrl: 显示头像URL</li>
 * <li>sceneType: 场景类型，由该值来判断属于哪个场景，为了客户端操作简单，客户端可以直接使用该值替代entityType，也就是每种场景背后对应的实体对象是固定的，
 *     根据场景类型中的定义{@link com.everhomes.rest.ui.user.SceneType}</li>
 * <li>communityType : 园区类型</li>
 * <li>status : 认证状态</li>
 * </ul>
 */
public class SceneDTO {
    private String sceneToken;
    private String entityType;
    private String entityContent;
    private String name;
    private String aliasName;
    private String avatar;
    private String avatarUrl;
    private String sceneType;
    private Byte communityType;
    private Byte status;

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityContent() {
        return entityContent;
    }

    public void setEntityContent(String entityContent) {
        this.entityContent = entityContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
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

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Byte getCommunityType() {
        return communityType;
    }

    public void setCommunityType(Byte communityType) {
        this.communityType = communityType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
