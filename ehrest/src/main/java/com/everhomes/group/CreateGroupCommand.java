// @formatter:off
package com.everhomes.group;

import com.everhomes.util.StringHelper;

public class CreateGroupCommand {
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
    
    public CreateGroupCommand() {
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
