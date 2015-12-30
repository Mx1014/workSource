// @formatter:off
package com.everhomes.rest.family;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>id: 家庭与用户的关联Id</li>
 * <li>familyId: 家庭与用户的关联familyId</li>
 * <li>familyName: 家庭名称</li>
 * <li>address: 家庭所在地址详情</li>
 * <li>communityId: 小区Id</li>
 * <li>communityName: 小区名称</li>
 * <li>cityId: 城市Id</li>
 * <li>cityName: 城市名称</li>
 * <li>areaId: 区域Id（如南山区的Id）</li>
 * <li>areaName: 区域名称</li>
 * <li>membershipStatus: 请求者在家庭的状态， {@link com.everhomes.rest.group.GroupMemberStatus}</li>
 * <li>memberUid: 用户Id</li>
 * <li>memberNickName: 用户在家庭中的昵称</li>
 * <li>addressId: 地址Id</li>
 * <li>buildingName: 家庭地址楼栋号</li>
 * <li>apartmentName: 家庭地址门牌号</li>
 * <li>addressStatus: 地址状态, {@link com.everhomes.rest.address.AddressAdminStatus}</li>
 * <li>cellPhone:用户电话号码</li>
 * </ul>
 */
public class FamilyMemberFullDTO {
    private Long id;
    private Long familyId;
    private String familyName;
    
    private Long communityId;
    private String communityName;
    private Long cityId;
    private String cityName;
    private Long areaId;
    private String areaName;
    
    private Byte membershipStatus;
    private Long memberUid;
    private String memberNickName;
    private String cellPhone;
    
    private String buildingName;
    private String apartmentName;
    private Byte addressStatus;
    private Timestamp createTime;
    
    
    
    public FamilyMemberFullDTO() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }
    
    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
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

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
