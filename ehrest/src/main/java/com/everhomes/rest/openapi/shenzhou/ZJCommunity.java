package com.everhomes.rest.openapi.shenzhou;

/**
 * Created by ying.xiong on 2017/8/9.
 */
public class ZJCommunity {
    private String communityName;
    private String communityIdentifier;
    private Byte communityType;
    private Double longitude;
    private Double latitude;
    private String cityName;
    private String areaName;
    private int aptCount;
    private Double areaSize;
    private Double buildArea;
    private Double rentArea;
    private Double sharedArea;
    private Double chargeArea;
    private String description;
    private String zipcode;
    private String address;
    private Boolean dealed;

    public Boolean getDealed() {
        return dealed;
    }

    public void setDealed(Boolean dealed) {
        this.dealed = dealed;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAptCount() {
        return aptCount;
    }

    public void setAptCount(int aptCount) {
        this.aptCount = aptCount;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Double getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(Double areaSize) {
        this.areaSize = areaSize;
    }

    public Double getBuildArea() {
        return buildArea;
    }

    public void setBuildArea(Double buildArea) {
        this.buildArea = buildArea;
    }

    public Double getChargeArea() {
        return chargeArea;
    }

    public void setChargeArea(Double chargeArea) {
        this.chargeArea = chargeArea;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public Byte getCommunityType() {
        return communityType;
    }

    public void setCommunityType(Byte communityType) {
        this.communityType = communityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getRentArea() {
        return rentArea;
    }

    public void setRentArea(Double rentArea) {
        this.rentArea = rentArea;
    }

    public Double getSharedArea() {
        return sharedArea;
    }

    public void setSharedArea(Double sharedArea) {
        this.sharedArea = sharedArea;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
