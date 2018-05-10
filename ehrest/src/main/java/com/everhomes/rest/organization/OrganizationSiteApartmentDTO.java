package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>
 *      <li>buildingId: 楼栋id</li>
 *      <li>apartmentId: 门牌id</li>
 *      <li>buildingName: 楼栋名称</li>
 *      <li>apartmentName: 门牌名称</li>
 * </ul>
 */
public class OrganizationSiteApartmentDTO {
    private Long buildingId;
    private Long apartmentId;

    private String buildingName;
    private String apartmentName;


    public Long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
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
