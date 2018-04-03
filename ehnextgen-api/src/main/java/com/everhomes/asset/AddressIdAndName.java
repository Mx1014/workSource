//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2017/8/18.
 */

public class AddressIdAndName {
    private Long addressId;
    private String buildingName;
    private String apartmentName;

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
}
