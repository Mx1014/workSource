// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: group ID</li>
 * <li>name: group名称</li>
 * <li>avatar: group头像URI</li>
 * <li>avatarUrl: group头像URL</li>
 * <li>description: group描述</li>
 * <li>createTime: group创建时间</li>
 * <li>privateFlag: group公有、私有标记，0-公有、1-私有</li>
 * </ul>
 */
public class GroupCardDTO {
    private Long id;
    private String name;
    private String avatar;
    private String avatarUrl;
    private String description;
    private Long creatorUid;
    private String createTime;
    private Byte privateFlag;
    
    public GroupCardDTO() {
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
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
