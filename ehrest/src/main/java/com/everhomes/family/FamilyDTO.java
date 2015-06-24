// @formatter:off
package com.everhomes.family;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 家庭Id</li>
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
 * <li>membershipStatus: 请求者在家庭的状态， {@link com.everhomes.group.GroupMemberStatus}</li>
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
 * <li>addressStatus: 地址状态, {@link com.everhomes.address.AddressAdminStatus}</li>
 * <li>proofResourceUrl: 存在该字段有值表名是加速审核的</li>
 * </ul>
 */
public class FamilyDTO {
    private Long id;
    private String name;
    private String displayName;
    private String avatarUri;
    private String avatarUrl;
    private String description;
    private Long memberCount;
    
    private String address;
    private Long communityId;
    private String communityName;
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
    private String proofResourceUrl;
    
    public FamilyDTO () {
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

    @Override
    public boolean equals(Object obj){
        if (! (obj instanceof FamilyDTO)) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, obj,new String[]{"adminStatus","primaryFlag"});
    }
    
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this,new String[]{"adminStatus","primaryFlag"});
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
