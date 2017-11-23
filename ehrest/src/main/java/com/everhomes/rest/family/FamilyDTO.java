// @formatter:off
package com.everhomes.rest.family;

import java.sql.Timestamp;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 家庭Id</li>
 * <li>uuid: 唯一标识</li>
 * <li>name: 家庭名称</li>
 * <li>displayName: 家庭显示名称，用于客户端显示</li>
 * <li>avatarUri: 家庭头像Id，图片上传到ContentServer得到的ID</li>
 * <li>avatarUri: 家庭头像url</li>
 * <li>description: 家庭描述</li>
 * <li>memberCount: 家庭成员数</li>
 * <li>address: 家庭所在地址详情</li>
 * <li>communityId: 小区Id</li>
 * <li>communityName: 小区名称</li>
 * <li>cityId: 城市Id</li>
 * <li>cityName: 城市名称</li>
 * <li>areaId: 区域Id（如南山区的Id）</li>
 * <li>areaName: 区域名称</li>
 * <li>membershipStatus: 请求者在家庭的状态， {@link com.everhomes.rest.group.GroupMemberStatus}</li>
 * <li>primaryFlag: 是否为常用家庭 0(非常用),1(常用)</li>
 * <li>adminStatus: 管理员状态0-非管理员，1-管理员</li>
 * <li>memberUid: 用户Id</li>
 * <li>memberNickName: 用户在家庭中的昵称</li>
 * <li>memberAvatarUri: 用户在家庭中的头像ID</li>
 * <li>memberAvatarUrl: 用户在家庭中的头像url</li>
 * <li>cellPhone: 用户电话号码</li>
 * <li>addressId: 地址Id</li>
 * <li>buildingName: 家庭地址楼栋号</li>
 * <li>apartmentName: 家庭地址门牌号</li>
 * <li>addressStatus: 地址状态, {@link com.everhomes.rest.address.AddressAdminStatus}</li>
 * <li>proofResourceUri: 用于加速审核的图片URI</li>
 * <li>proofResourceUrl: 用于加速审核的图片URI</li>
 * <li>createTime: 创建时间</li>
 * <li>communityType: 园区类型，参考{@link com.everhomes.rest.community.CommunityType}</li>
 * <li>defaultForumId: 默认论坛ID，每个园区都有一个自己的默认论坛用于放园区整体的帖子（如公告）</li>
 * <li>feedbackForumId: 意见论坛ID，每个园区都有一个自己的意见反馈论坛用于放园区意见反馈帖子</li>
 * </ul>
 */
public class FamilyDTO {
    private Long id;
//    private String uuid;
    private String name;
    private String displayName;
    private String avatarUri;
    private String avatarUrl;
    private String description;
    private Long memberCount;
    
    private String address;
    private Long communityId;
    private String communityName;
    private String communityAliasName;
    private Long cityId;
    private String cityName;
    private Long areaId;
    private String areaName;
    
    private Byte membershipStatus;  // membership relationship with requestor
    private Byte primaryFlag;
    private Byte adminStatus;
    
    private Long memberUid;
    private String memberNickName;
    private String memberAvatarUri;
    private String memberAvatarUrl;
    private String cellPhone;
    
    private Long addressId;
    private String buildingName;
    private String apartmentName;
    
    private Byte addressStatus;
    private String proofResourceUri;
    private String proofResourceUrl;
    
    private Timestamp createTime;
    
    private Byte communityType;
    private Long defaultForumId;
    private Long feedbackForumId;
    
    
    public FamilyDTO () {
    }
    

    public Timestamp getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}


	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public String getUuid() {
//        return uuid;
//    }
//
//    public void setUuid(String uuid) {
//        this.uuid = uuid;
//    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Byte getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(Byte membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    public Byte getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(Byte adminStatus) {
        this.adminStatus = adminStatus;
    }

    public Byte getPrimaryFlag() {
        return primaryFlag;
    }

    public void setPrimaryFlag(Byte primaryFlag) {
        this.primaryFlag = primaryFlag;
    }
    
    public String getMemberNickName() {
        return memberNickName;
    }

    public void setMemberNickName(String memberNickName) {
        this.memberNickName = memberNickName;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getMemberUid() {
        return memberUid;
    }

    public void setMemberUid(Long memberUid) {
        this.memberUid = memberUid;
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

    public Byte getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(Byte addressStatus) {
        this.addressStatus = addressStatus;
    }
    
    public String getProofResourceUri() {
        return proofResourceUri;
    }

    public void setProofResourceUri(String proofResourceUri) {
        this.proofResourceUri = proofResourceUri;
    }

    public String getProofResourceUrl() {
        return proofResourceUrl;
    }

    public void setProofResourceUrl(String proofResourceUrl) {
        this.proofResourceUrl = proofResourceUrl;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public Byte getCommunityType() {
        return communityType;
    }

    public void setCommunityType(Byte communityType) {
        this.communityType = communityType;
    }

    public Long getDefaultForumId() {
        return defaultForumId;
    }

    public void setDefaultForumId(Long defaultForumId) {
        this.defaultForumId = defaultForumId;
    }

    public Long getFeedbackForumId() {
        return feedbackForumId;
    }

    public void setFeedbackForumId(Long feedbackForumId) {
        this.feedbackForumId = feedbackForumId;
    }

    @Override
    public boolean equals(Object obj){
        if (! (obj instanceof FamilyDTO)) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getCommunityAliasName() {
        return communityAliasName;
    }

    public void setCommunityAliasName(String communityAliasName) {
        this.communityAliasName = communityAliasName;
    }
}
