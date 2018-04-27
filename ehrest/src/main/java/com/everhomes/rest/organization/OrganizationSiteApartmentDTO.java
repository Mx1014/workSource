package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>
 * <li>buildingId: 楼栋id</li>
 * <li>apartmentId: 门牌id</li>
 * </ul>
 */
public class OrganizationSiteApartmentDTO {
    private Long buildingId;
    private Long apartmentId;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
