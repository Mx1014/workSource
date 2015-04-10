// @formatter:off
package com.everhomes.group;

import com.everhomes.util.StringHelper;

public class UpdateGroupCommand {
    private Long groupId;
    private String name;
    private String description;
    private String avatar;
    private Byte regionScope;
    private Long regionId;
    private Byte privateFlag;
    
    private Long roleForNewPost;
    private Long roleForDelPost;
    
    private Long categoryId;
    
    private String tag;

    // json of List<RegionDescriptor>
    private String jsonVisibleRegionDescriptors;
    
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

    public Byte getRegionScope() {
        return regionScope;
    }

    public void setRegionScope(Byte regionScope) {
        this.regionScope = regionScope;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Byte getPrivateFlag() {
        return privateFlag;
    }

    public void setPrivateFlag(Byte privateFlag) {
        this.privateFlag = privateFlag;
    }

    public Long getRoleForNewPost() {
        return roleForNewPost;
    }

    public void setRoleForNewPost(Long roleForNewPost) {
        this.roleForNewPost = roleForNewPost;
    }

    public Long getRoleForDelPost() {
        return roleForDelPost;
    }

    public void setRoleForDelPost(Long roleForDelPost) {
        this.roleForDelPost = roleForDelPost;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getJsonVisibleRegionDescriptors() {
        return jsonVisibleRegionDescriptors;
    }

    public void setJsonVisibleRegionDescriptors(String jsonVisibleRegionDescriptors) {
        this.jsonVisibleRegionDescriptors = jsonVisibleRegionDescriptors;
    }
    
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
