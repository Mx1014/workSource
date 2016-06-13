package com.everhomes.rest.enterprise;

import java.sql.Timestamp;

/**
 * <ul> 用户请求公司信息
 * <li>id: Enterprise Id</li>
 * <li>name: Enterprise Name</li>
 * <li>displayName: Enterprise display name</li>
 * <li>avatarUri: the Id from content server for Enterprise image</li>
 * <li>avatarUri: enterprise url</li>
 * <li>description: enterprise description</li>
 * <li>memberCount: members' count</li>
 * <li>membershipStatus: membership status， {@link com.everhomes.Enterprise.EnterpriseMemberStatus}</li>
 * <li>primaryFlag: TODO</li>
 * <li>adminStatus: 管理员状态0-非管理员，1-管理员</li>
 * <li>memberUid: 用户Id</li>
 * <li>memberNickName: 用户在公司中的昵称</li>
 * <li>memberAvatarUri: 用户在公司中的头像ID</li>
 * <li>memberAvatarUrl: 用户在公司中的头像url</li>
 * <li>cellPhone: 用户电话号码</li>
 * <li>createTime: 创建时间</li>
 * </ul>
 */
public class EnterpriseMemberInfoDTO {
    private Long id;
    private String name;
    private String displayName;
    private String avatarUri;
    private String avatarUrl;
    private String description;
    private Long memberCount;

    private Byte membershipStatus;  //TODO membership relationship with requestor
    private Byte primaryFlag;
    private Byte adminStatus;
  
    private Long memberUid;
    private String memberNickName;
    private String memberAvatarUri;
    private String memberAvatarUrl;
    private String cellPhone;

    private Timestamp createTime;

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
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

    public Long getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Long memberCount) {
        this.memberCount = memberCount;
    }

    public Byte getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(Byte membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    public Byte getPrimaryFlag() {
        return primaryFlag;
    }

    public void setPrimaryFlag(Byte primaryFlag) {
        this.primaryFlag = primaryFlag;
    }

    public Byte getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(Byte adminStatus) {
        this.adminStatus = adminStatus;
    }

    public Long getMemberUid() {
        return memberUid;
    }

    public void setMemberUid(Long memberUid) {
        this.memberUid = memberUid;
    }

    public String getMemberNickName() {
        return memberNickName;
    }

    public void setMemberNickName(String memberNickName) {
        this.memberNickName = memberNickName;
    }

    public String getMemberAvatarUri() {
        return memberAvatarUri;
    }

    public void setMemberAvatarUri(String memberAvatarUri) {
        this.memberAvatarUri = memberAvatarUri;
    }

    public String getMemberAvatarUrl() {
        return memberAvatarUrl;
    }

    public void setMemberAvatarUrl(String memberAvatarUrl) {
        this.memberAvatarUrl = memberAvatarUrl;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
}
