// @formatter:off
package com.everhomes.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 家庭Id</li>
 * <li>name: 家庭名称</li>
 * <li>displayName: 家庭显示名称，用于客户端显示</li>
 * <li>avatar: 家庭头像Id，图片上传到ContentServer得到的ID</li>
 * <li>description: 家庭描述</li>
 * <li>memberCount: 家庭成员数</li>
 * <li>address: 家庭所在地址详情</li>
 * <li>communityId: 小区Id</li>
 * <li>communityName: 小区名称</li>
 * <li>cityId: 城市Id</li>
 * <li>cityName: 城市名称</li>
 * <li>areaId: 区域Id（如南山区的Id）</li>
 * <li>areaName: 区域名称</li>
 * <li>membershipStatus: 待定??</li>
 * <li>primaryFlag: 是否为常用家庭 0(非常用),1(常用)</li>
 * <li>adminStatus: 管理员状态0-非管理员，1-管理员</li>
 * </ul>
 */
public class FamilyDTO {
    private Long id;
    private String name;
    private String displayName;
    private String avatar;
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
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
