// @formatter:off
package com.everhomes.group;

import java.util.List;

import com.everhomes.util.StringHelper;

public class GroupDTO {
    private Long id;
    private Long owningForumId;
    private String name;
    private String avatar;
    private String description;
    private Long creatorUid;
    private String createTime;
    private Byte privateFlag;
    private Integer joinPolicy;
    private Long memberCount;
    private String tag;
    private Long categoryId;
    private String categoryName;
    
    //
    // requestor/group relationship information
    // 
    private Byte memberOf;
    private String memberNickName;
    private Long memberConfigFlag;
    private List<Long> memberGroupPrivileges;
    private List<Long> memberForumPrivileges;
    
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

    public String getMemberNickName() {
        return memberNickName;
    }

    public void setMemberNickName(String memberNickName) {
        this.memberNickName = memberNickName;
    }

    public List<Long> getMemberGroupPrivileges() {
        return memberGroupPrivileges;
    }

    public void setMemberGroupPrivileges(List<Long> memberGroupPrivileges) {
        this.memberGroupPrivileges = memberGroupPrivileges;
    }

    public List<Long> getMemberForumPrivileges() {
        return memberForumPrivileges;
    }

    public void setMemberForumPrivileges(List<Long> memberForumPrivileges) {
        this.memberForumPrivileges = memberForumPrivileges;
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

    public Long getOwningForumId() {
        return owningForumId;
    }

    public void setOwningForumId(Long owningForumId) {
        this.owningForumId = owningForumId;
    }

    public Long getMemberConfigFlag() {
        return memberConfigFlag;
    }

    public void setMemberConfigFlag(Long memberConfigFlag) {
        this.memberConfigFlag = memberConfigFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
