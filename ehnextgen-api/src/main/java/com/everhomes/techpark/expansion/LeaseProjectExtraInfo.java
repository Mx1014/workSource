package com.everhomes.techpark.expansion;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/10/18.
 */
public class LeaseProjectExtraInfo {

    private String projectNo;
    private String completeTime;
    private String roomType;
    private String buildingNum;
    private String floorNum;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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
