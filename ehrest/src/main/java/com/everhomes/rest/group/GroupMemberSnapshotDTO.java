// @formatter:off
package com.everhomes.rest.group;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：group与成员关系ID</li>
 * <li>groupId：group ID</li>
 * <li>groupName：group名称</li>
 * <li>memberType：成员类型，参考{@link com.everhomes.entity.EntityType}</li>
 * <li>memberId：成员ID</li>
 * <li>memberNickName：成员在group内的昵称</li>
 * <li>memberAvatar：成员在group内的头像URI</li>
 * <li>memberAvatarUrl：成员在group内的头像URL</li>
 * <li>updateTime：更新时间</li>
 * <li>createTime：创建时间</li>
 * <li>discriminator: group标识，参考{@link com.everhomes.group.GroupDiscriminator}</li>
 * </ul>
 */
public class GroupMemberSnapshotDTO {
    private Long id;
    private Long groupId;
    private String groupName;
    private String memberType;
    private Long memberId;
    private String memberNickName;
    private String memberAvatar;
    private String memberAvatarUrl;
    private Timestamp updateTime;
    private Timestamp createTime;
    
    private String discriminator;
    
    public GroupMemberSnapshotDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    
    public String getMemberAvatar() {
        return memberAvatar;
    }

    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = memberAvatar;
    }

    public String getMemberAvatarUrl() {
        return memberAvatarUrl;
    }

    public void setMemberAvatarUrl(String memberAvatarUrl) {
        this.memberAvatarUrl = memberAvatarUrl;
    }

    public String getMemberNickName() {
        return memberNickName;
    }

    public void setMemberNickName(String memberNickName) {
        this.memberNickName = memberNickName;
    }
    
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
