package com.everhomes.rest.techpark.expansion;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.ProjectDTO;

import java.util.List;

/**
 * @author sw on 2017/10/17.
 */
public class LeaseProjectDTO {

    private Integer namespaceId;
    private Long projectId;
    private String name;
    private Long cityId;
    private String cityName;
    private Long areaId;
    private String areaName;
    private String address;
    private Double longitude;
    private Double latitude;
    private String contactName;
    private String contactPhone;
    private String description;
    private String trafficDescription;
    private String posterUri;
    private String posterUrl;
    @ItemType(ProjectDTO.class)
    private List<ProjectDTO> projectDTOS;
    @ItemType(BuildingForRentAttachmentDTO.class)
    private List<BuildingForRentAttachmentDTO> attachments;

    private String projectNo;
    private String completeTime;
    private String roomType;
    private String buildingNum;
    private String floorNum;
    private String floorHeight;
    private String areaSize;
    private String investmentArea;
    private String floorBearing;
    private String electricityEquipped;
    private String conditioningEquipped;
    private String liftEquipped;
    private String networkEquipped;
    private String referAmount;
    private String pmCompany;
    private String pmAmount;
    private String parkingSpaceNum;
    private String parkingSpaceAmount;
    private String parkingTempFee;
    private String enteredEnterprises;

    private String detailUrl;

    @ItemType(LeaseBuildingDTO.class)
    private List<LeaseBuildingDTO> buildings;

    public List<LeaseBuildingDTO> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<LeaseBuildingDTO> buildings) {
        this.buildings = buildings;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getFloorHeight() {
        return floorHeight;
    }

    public void setFloorHeight(String floorHeight) {
        this.floorHeight = floorHeight;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrafficDescription() {
        return trafficDescription;
    }

    public void setTrafficDescription(String trafficDescription) {
        this.trafficDescription = trafficDescription;
    }

    public String getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(String posterUri) {
        this.posterUri = posterUri;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public List<ProjectDTO> getProjectDTOS() {
        return projectDTOS;
    }

    public void setProjectDTOS(List<ProjectDTO> projectDTOS) {
        this.projectDTOS = projectDTOS;
    }

    public List<BuildingForRentAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<BuildingForRentAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(String buildingNum) {
        this.buildingNum = buildingNum;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(String areaSize) {
        this.areaSize = areaSize;
    }

    public String getInvestmentArea() {
        return investmentArea;
    }

    public void setInvestmentArea(String investmentArea) {
        this.investmentArea = investmentArea;
    }

    public String getFloorBearing() {
        return floorBearing;
    }

    public void setFloorBearing(String floorBearing) {
        this.floorBearing = floorBearing;
    }

    public String getElectricityEquipped() {
        return electricityEquipped;
    }

    public void setElectricityEquipped(String electricityEquipped) {
        this.electricityEquipped = electricityEquipped;
    }

    public String getConditioningEquipped() {
        return conditioningEquipped;
    }

    public void setConditioningEquipped(String conditioningEquipped) {
        this.conditioningEquipped = conditioningEquipped;
    }

    public String getLiftEquipped() {
        return liftEquipped;
    }

    public void setLiftEquipped(String liftEquipped) {
        this.liftEquipped = liftEquipped;
    }

    public String getNetworkEquipped() {
        return networkEquipped;
    }

    public void setNetworkEquipped(String networkEquipped) {
        this.networkEquipped = networkEquipped;
    }

    public String getReferAmount() {
        return referAmount;
    }

    public void setReferAmount(String referAmount) {
        this.referAmount = referAmount;
    }

    public String getPmCompany() {
        return pmCompany;
    }

    public void setPmCompany(String pmCompany) {
        this.pmCompany = pmCompany;
    }

    public String getPmAmount() {
        return pmAmount;
    }

    public void setPmAmount(String pmAmount) {
        this.pmAmount = pmAmount;
    }

    public String getParkingSpaceNum() {
        return parkingSpaceNum;
    }

    public void setParkingSpaceNum(String parkingSpaceNum) {
        this.parkingSpaceNum = parkingSpaceNum;
    }

    public String getParkingSpaceAmount() {
        return parkingSpaceAmount;
    }

    public void setParkingSpaceAmount(String parkingSpaceAmount) {
        this.parkingSpaceAmount = parkingSpaceAmount;
    }

    public String getParkingTempFee() {
        return parkingTempFee;
    }

    public void setParkingTempFee(String parkingTempFee) {
        this.parkingTempFee = parkingTempFee;
    }

    public String getEnteredEnterprises() {
        return enteredEnterprises;
    }

    public void setEnteredEnterprises(String enteredEnterprises) {
        this.enteredEnterprises = enteredEnterprises;
    }
}
