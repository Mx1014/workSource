package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>building: 楼栋号</li>
 * <li>apartment: 门牌号</li>
 * <li>address: 完整地址信息</li>
 * <li>addressId: 地址id</li>
 * <li>livingStatus: 是否在户, 不在, 在</li>
 * <li>authType: 认证类型, 已认证, 未认证</li>
 * </ul>
 */
public class OrganizationOwnerAddressDTO {

    private String building;
    private String apartment;
    private String address;
    private Long   addressId;
    private String livingStatus;
    private String authType;

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getLivingStatus() {
        return livingStatus;
    }

    public void setLivingStatus(String livingStatus) {
        this.livingStatus = livingStatus;
    }

    public String getAuthType() {
        return authType;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
