// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id：group与成员关系ID</li>
 * <li>uuid：group与成员关系UUID</li>
 * <li>groupId：group ID</li>
 * <li>groupName：group名称</li>
 * <li>memberType：成员类型，参考{@link com.everhomes.entity.EntityType}</li>
 * <li>memberId：成员ID</li>
 * <li>memberRole：成员在group内的角色</li>
 * <li>memberNickName：成员在group内的昵称</li>
 * <li>memberAvatar：成员在group内的头像URI</li>
 * <li>memberAvatarUrl：成员在group内的头像URL</li>
 * <li>memberStatus：成员在group内的状态，参考{@link com.everhomes.rest.group.GroupMemberStatus}</li>
 * <li>inviterUid：被邀请人用户ID，有邀请人时才有效</li>
 * <li>inviterNickName：被邀请人用户昵称，有邀请人时才有效</li>
 * <li>inviterAvatar：被邀请人用户头像URI，有邀请人时才有效</li>
 * <li>inviterAvatarUrl：被邀请人用户头像URL，有邀请人时才有效</li>
 * <li>inviteTime：被邀请时间，有邀请人时才有效</li>
 * <li>createTime：成员加入group的时间</li>
 * <li>phonePrivateFlag: group成员是否显示手机号标记，{@link com.everhomes.rest.group.GroupMemberPhonePrivacy}</li>
 * <li>cellPhone: group成员手机码，当<code>phonePrivateFlag</code>设置为公开手机号时才有效</li>
 * <li>muteNotificationFlag: group成员是否免打扰标记，{@link com.everhomes.rest.group.GroupMemberMuteNotificationFlag}</li>
 * </ul>
 */
public class GroupMemberDTO {
    private Long id;
    private String uuid;
    private Long groupId;
    private String groupName;
    private String memberType;
    private Long memberId;
    private Long memberRole;
    private String memberNickName;
    private String memberAvatar;
    private String memberAvatarUrl;
    private Byte memberStatus;
    private Long inviterUid;
    private String inviterNickName;
    private String inviterAvatar;
    private String inviterAvatarUrl;
    private Timestamp inviteTime;
    private Timestamp createTime;
    private Timestamp approveTime;
    private String operatorName;
    private String operatorPhone;
    private Timestamp updateTime;
    private Byte phonePrivateFlag;
    private String cellPhone;
    private Byte muteNotificationFlag;
    
    private Long addressId;
    private String buildingName;
    private String apartmentName;
    private String communityName;
    private String areaName;
    private String cityName;

    public GroupMemberDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorPhone() {
        return operatorPhone;
    }

    public void setOperatorPhone(String operatorPhone) {
        this.operatorPhone = operatorPhone;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public Long getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(Long memberRole) {
        this.memberRole = memberRole;
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

    public Long getInviterUid() {
        return inviterUid;
    }

    public void setInviterUid(Long inviterUid) {
        this.inviterUid = inviterUid;
    }

    public String getInviterNickName() {
        return inviterNickName;
    }

    public void setInviterNickName(String inviterNickName) {
        this.inviterNickName = inviterNickName;
    }

    public String getInviterAvatar() {
        return inviterAvatar;
    }

    public void setInviterAvatar(String inviterAvatar) {
        this.inviterAvatar = inviterAvatar;
    }

    public String getInviterAvatarUrl() {
        return inviterAvatarUrl;
    }

    public void setInviterAvatarUrl(String inviterAvatarUrl) {
        this.inviterAvatarUrl = inviterAvatarUrl;
    }

    public Timestamp getInviteTime() {
        return inviteTime;
    }

    public void setInviteTime(Timestamp inviteTime) {
        this.inviteTime = inviteTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Timestamp approveTime) {
        this.approveTime = approveTime;
    }

    public Byte getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(Byte memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Byte getPhonePrivateFlag() {
        return phonePrivateFlag;
    }

    public void setPhonePrivateFlag(Byte phonePrivateFlag) {
        this.phonePrivateFlag = phonePrivateFlag;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public Byte getMuteNotificationFlag() {
        return muteNotificationFlag;
    }

    public void setMuteNotificationFlag(Byte muteNotificationFlag) {
        this.muteNotificationFlag = muteNotificationFlag;
    }

    public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
