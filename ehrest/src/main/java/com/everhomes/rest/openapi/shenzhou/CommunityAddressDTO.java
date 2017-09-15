package com.everhomes.rest.openapi.shenzhou;

/**
 * Created by ying.xiong on 2017/8/28.
 */
public class CommunityAddressDTO {
    private String communityIdentifier;
    private String buildingIdentifier;
    private String apartmentIdentifier;
    private String communityName;
    private String buildingName;
    private String apartmentName;

    public String getApartmentIdentifier() {
        return apartmentIdentifier;
    }

    public void setApartmentIdentifier(String apartmentIdentifier) {
        this.apartmentIdentifier = apartmentIdentifier;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getBuildingIdentifier() {
        return buildingIdentifier;
    }

    public void setBuildingIdentifier(String buildingIdentifier) {
        this.buildingIdentifier = buildingIdentifier;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getCommunityIdentifier() {
        return communityIdentifier;
    }

    public void setCommunityIdentifier(String communityIdentifier) {
        this.communityIdentifier = communityIdentifier;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
}
