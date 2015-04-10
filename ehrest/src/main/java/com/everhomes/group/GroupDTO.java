// @formatter:off
package com.everhomes.group;

import com.everhomes.util.StringHelper;

public class GroupDTO {
    private Long id;
    private String name;
    private String avatar;
    private String description;
    private Long creatorUid;
    private String createTime;
    private Byte privateFlag;
    private Byte displayMode;
    private Integer joinPolicy;
    private Long memberCount;
    private Long roleForNewPost;
    private Long roleForDelPost;
    private String tag;
    private Long categoryId;
    private String categoryName;
    
    // requestor/group relationship
    private Byte memberOf;
    private Byte blockFlag;
    private String nickName;
    private Long role;
    
    public GroupDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Byte getPrivateFlag() {
        return privateFlag;
    }

    public void setPrivateFlag(Byte privateFlag) {
        this.privateFlag = privateFlag;
    }

    public Byte getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(Byte displayMode) {
        this.displayMode = displayMode;
    }

    public Integer getJoinPolicy() {
        return joinPolicy;
    }

    public void setJoinPolicy(Integer joinPolicy) {
        this.joinPolicy = joinPolicy;
    }

    public Long getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Long memberCount) {
        this.memberCount = memberCount;
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

    public Byte getMemberOf() {
        return memberOf;
    }

    public void setMemberOf(Byte memberOf) {
        this.memberOf = memberOf;
    }

    public Byte getBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(Byte blockFlag) {
        this.blockFlag = blockFlag;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }
    
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
