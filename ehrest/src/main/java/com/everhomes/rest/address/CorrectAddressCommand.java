// @formatter:off
package com.everhomes.rest.address;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区Id</li>
 * <li>addressId: 地址Id</li>
 * <li>buildingName: 楼栋号</li>
 * <li>apartmentName: 门牌号</li>
 * </ul>
 */
public class CorrectAddressCommand {
    @NotNull
    private Long communityId;
    
    @NotNull
    private Long addressId;
    
    @NotNull
    private String buildingName;
    
    @NotNull
    private String apartmentName;

    public CorrectAddressCommand() {
    }
    
    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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
