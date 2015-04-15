// @formatter:off
package com.everhomes.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class UpdateGroupCommand {
    @NotNull
    private Long groupId;
    
    private String name;
    private String description;
    private String avatar;
    
    private Byte visibilityScope;
    private Long visibilityScopeId;
    
    private Long categoryId;
    
    private String tag;

    public UpdateGroupCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
