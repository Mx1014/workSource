//@formatter:off
package com.everhomes.asset.zjgkVOs;

import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2017/9/5.
 */

public class CommunityAddressDTO {
    private String communityIdentifier;
    private String apartmentIdentifier;
    private String buildingIdentifier;
    private String communityName;
    private String buildingName;
    private String apartmentName;

    public String getCommunityIdentifier() {
        return communityIdentifier;
    }

    public void setCommunityIdentifier(String communityIdentifier) {
        this.communityIdentifier = communityIdentifier;
    }

    public String getApartmentIdentifier() {
        return apartmentIdentifier;
    }

    public void setApartmentIdentifier(String apartmentIdentifier) {
        this.apartmentIdentifier = apartmentIdentifier;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getBuildingIdentifier() {
        return buildingIdentifier;
    }

    public void setBuildingIdentifier(String buildingIdentifier) {
        this.buildingIdentifier = buildingIdentifier;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
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
