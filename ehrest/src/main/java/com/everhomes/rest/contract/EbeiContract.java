package com.everhomes.rest.contract;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by ying.xiong on 2017/11/21.
 */
public class EbeiContract {
    private String startDate;
    private String endDate;
    private String ownerId;
    private String companyName;
    private String state;
    private String serialNumber;
    private String contractId;
    private String quitDate;
    private String quitFlag;
    private String contractStatus;
    private String version;
    private String buildingRename;
    private String signDate;
    private String totalArea;
    private String projectName;
    @ItemType(EbeiContractRoomInfo.class)
    private List<EbeiContractRoomInfo> houseInfoList;

    public String getBuildingRename() {
        return buildingRename;
    }

    public void setBuildingRename(String buildingRename) {
        this.buildingRename = buildingRename;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<EbeiContractRoomInfo> getHouseInfoList() {
        return houseInfoList;
    }

    public void setHouseInfoList(List<EbeiContractRoomInfo> houseInfoList) {
        this.houseInfoList = houseInfoList;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getQuitDate() {
        return quitDate;
    }

    public void setQuitDate(String quitDate) {
        this.quitDate = quitDate;
    }

    public String getQuitFlag() {
        return quitFlag;
    }

    public void setQuitFlag(String quitFlag) {
        this.quitFlag = quitFlag;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(String totalArea) {
        this.totalArea = totalArea;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
