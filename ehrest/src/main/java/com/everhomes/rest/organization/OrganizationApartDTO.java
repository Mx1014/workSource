package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>buildingName: 楼栋名称</li>
 *     <li>apartmentName: 门牌名称</li>
 * </ul>
 */
public class OrganizationApartDTO {
    private String buildingName;
    private String apartmentName;

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
