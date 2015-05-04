// @formatter:off
package com.everhomes.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区名称</li>
 * <li>targetType：注册用户类型</li>
 * <li>targetId：注册用户对应的userId</li>
 * <li>pmGroup：物业角色类型.MANAGER: 物业管理员,REPAIR: 维修人员,CLEANING: 保洁人员,KEFU: 客服人员</li>
 * <li>contactName：未注册成员名称</li>
 * <li>contactType：未注册成员类型：0-手机，1-邮箱</li>
 * <li>contactToken：未注册成员标识</li>
 * <li>contactDescription：描述</li>
 * <li>status：状态</li>
 * </ul>
 */
public class CreatePropMemberCommand {
    @NotNull
    private String name;
    private String description;
    private String avatar;
    private Byte visibilityScope;
    private Long visibilityScopeId;
    private Byte privateFlag;
    private Long categoryId;
    private String tag;

    // json of List<RegionDescriptor>
    private String explicitRegionDescriptorsJson;
    
    public CreatePropMemberCommand() {
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

    public Byte getPrivateFlag() {
        return privateFlag;
    }

    public void setPrivateFlag(Byte privateFlag) {
        this.privateFlag = privateFlag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public Byte getVisibilityScope() {
        return visibilityScope;
    }

    public void setVisibilityScope(Byte visibilityScope) {
        this.visibilityScope = visibilityScope;
    }

    public Long getVisibilityScopeId() {
        return visibilityScopeId;
    }

    public void setVisibilityScopeId(Long visibilityScopeId) {
        this.visibilityScopeId = visibilityScopeId;
    }

    public String getExplicitRegionDescriptorsJson() {
        return explicitRegionDescriptorsJson;
    }

    public void setExplicitRegionDescriptorsJson(String explicitRegionDescriptorsJson) {
        this.explicitRegionDescriptorsJson = explicitRegionDescriptorsJson;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
