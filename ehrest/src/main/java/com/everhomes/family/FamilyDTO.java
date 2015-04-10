// @formatter:off
package com.everhomes.family;

import com.everhomes.util.StringHelper;

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
