package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>categoryId:入口id</li>
 * <li>hideAddressFlag:是否隐藏门牌</li>
 * <li>limitCommunityFlag:是否仅本项目可见 不保存到eh_lease_configs表中</li>
 * <li>buildingIntroduceFlag:楼栋介绍是否显示</li>
 * <li>renewFlag: 续租是否显示</li>
 * <li>areaSearchFlag: 是否支持面积搜索</li>
 * <li>rentAmountFlag: 租金启用标志</li>
 * <li>rentAmountUnit: 租金单位</li>
 * <li>displayNameStr: 显示名称字符串</li>
 * <li>displayOrderStr: 显示排序，与名称对应</li>
 * <ul>
 */
public class LeaseProjectInstanceConfig {
    private Byte categoryId;
    private Byte hideAddressFlag;
    private Byte limitCommunityFlag;
    private Byte buildingIntroduceFlag;
    private Byte renewFlag;
    private Byte areaSearchFlag;
    private Byte rentAmountFlag;
    private String rentAmountUnit;
    private String displayNameStr;
    private String displayOrderStr;


    public Byte getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Byte categoryId) {
        this.categoryId = categoryId;
    }

    public Byte getHideAddressFlag() {
        return hideAddressFlag;
    }

    public void setHideAddressFlag(Byte hideAddressFlag) {
        this.hideAddressFlag = hideAddressFlag;
    }

    public Byte getLimitCommunityFlag() {
        return limitCommunityFlag;
    }

    public void setLimitCommunityFlag(Byte limitCommunityFlag) {
        this.limitCommunityFlag = limitCommunityFlag;
    }

    public Byte getBuildingIntroduceFlag() {
        return buildingIntroduceFlag;
    }

    public void setBuildingIntroduceFlag(Byte buildingIntroduceFlag) {
        this.buildingIntroduceFlag = buildingIntroduceFlag;
    }

    public Byte getRenewFlag() {
        return renewFlag;
    }

    public void setRenewFlag(Byte renewFlag) {
        this.renewFlag = renewFlag;
    }

    public Byte getAreaSearchFlag() {
        return areaSearchFlag;
    }

    public void setAreaSearchFlag(Byte areaSearchFlag) {
        this.areaSearchFlag = areaSearchFlag;
    }

    public Byte getRentAmountFlag() {
        return rentAmountFlag;
    }

    public void setRentAmountFlag(Byte rentAmountFlag) {
        this.rentAmountFlag = rentAmountFlag;
    }

    public String getRentAmountUnit() {
        return rentAmountUnit;
    }

    public void setRentAmountUnit(String rentAmountUnit) {
        this.rentAmountUnit = rentAmountUnit;
    }

    public String getDisplayNameStr() {
        return displayNameStr;
    }

    public void setDisplayNameStr(String displayNameStr) {
        this.displayNameStr = displayNameStr;
    }

    public String getDisplayOrderStr() {
        return displayOrderStr;
    }

    public void setDisplayOrderStr(String displayOrderStr) {
        this.displayOrderStr = displayOrderStr;
    }
}
