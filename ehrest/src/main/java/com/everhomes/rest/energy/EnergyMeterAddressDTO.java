package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>meterId: 表记id</li>
 *     <li>buildingId: 楼栋id</li>
 *     <li>buildingName: 楼栋名</li>
 *     <li>addressId: 门牌id</li>
 *     <li>apartmentName: 门牌名</li>
 *     <li>apartmentFloor: 楼层</li>
 * </ul>
 * Created by ying.xiong on 2017/10/16.
 */
public class EnergyMeterAddressDTO {
    private Long id;
    private Long meterId;
    private Long buildingId;
    private String buildingName;
    private Long addressId;
    private String apartmentName;
    private String apartmentFloor;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getApartmentFloor() {
        return apartmentFloor;
    }

    public void setApartmentFloor(String apartmentFloor) {
        this.apartmentFloor = apartmentFloor;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
