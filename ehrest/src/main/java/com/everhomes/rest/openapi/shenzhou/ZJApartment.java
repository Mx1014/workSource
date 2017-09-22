package com.everhomes.rest.openapi.shenzhou;

/**
 * Created by ying.xiong on 2017/8/9.
 */
public class ZJApartment {
    private String apartmentName;
    private String communityIdentifier;
    private Long communityId;
    private String buildingName;
    private String buildingIdentifier;
    private String apartmentIdentifier;
    private String cityName;
    private String areaName;
    private Byte livingStatus;
    private Integer apartmentFloor;
    private Double areaSize;
    private Double buildArea;
    private Double rentArea;
    private Double sharedArea;
    private Double chargeArea;
    private Byte decorateStatus;
    private String orientation;
    private String layout;
    private Boolean dealed;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getApartmentFloor() {
        return apartmentFloor;
    }

    public void setApartmentFloor(Integer apartmentFloor) {
        this.apartmentFloor = apartmentFloor;
    }

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

    public Boolean getDealed() {
        return dealed;
    }

    public void setDealed(Boolean dealed) {
        this.dealed = dealed;
    }

    public Byte getDecorateStatus() {
        return decorateStatus;
    }

    public void setDecorateStatus(Byte decorateStatus) {
        this.decorateStatus = decorateStatus;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public Byte getLivingStatus() {
        return livingStatus;
    }

    public void setLivingStatus(Byte livingStatus) {
        this.livingStatus = livingStatus;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
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
}
