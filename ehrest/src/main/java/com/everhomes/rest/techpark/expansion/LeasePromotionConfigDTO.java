package com.everhomes.rest.techpark.expansion;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>rentAmountFlag: 租金启用标志</li>
 * <li>issuingLeaseFlag: 用户是否可发布招租信息</li>
 * <li>renewFlag: 续租是否显示</li>
 * <li>areaSearchFlag: 是否支持面积搜索</li>
 * <li>consultFlag: 是否显示电话咨询按钮</li>
 * <li>displayNamesV2: 显示名称字符串</li>
 * <li>displayOrdersV2: 显示排序，与名称对应</li>
 * <li>rentAmountUnit: 租金单位</li>
 * </ul>
 */
public class LeasePromotionConfigDTO {
    private Integer namespaceId;
    private Byte rentAmountFlag;
    private Byte issuingLeaseFlag;
    private Byte renewFlag;
    private Byte areaSearchFlag;
    private Byte consultFlag;
    private String rentAmountUnit;
    private Byte buildingIntroduceFlag;

    @ItemType(String.class)
    private List<String> displayNames;
    @ItemType(Integer.class)
    private List<Integer> displayOrders;

    public Byte getBuildingIntroduceFlag() {
        return buildingIntroduceFlag;
    }

    public void setBuildingIntroduceFlag(Byte buildingIntroduceFlag) {
        this.buildingIntroduceFlag = buildingIntroduceFlag;
    }

    public String getRentAmountUnit() {
        return rentAmountUnit;
    }

    public void setRentAmountUnit(String rentAmountUnit) {
        this.rentAmountUnit = rentAmountUnit;
    }

    public Byte getConsultFlag() {
        return consultFlag;
    }

    public void setConsultFlag(Byte consultFlag) {
        this.consultFlag = consultFlag;
    }

    public List<String> getDisplayNames() {
        return displayNames;
    }

    public void setDisplayNames(List<String> displayNames) {
        this.displayNames = displayNames;
    }

    public List<Integer> getDisplayOrders() {
        return displayOrders;
    }

    public void setDisplayOrders(List<Integer> displayOrders) {
        this.displayOrders = displayOrders;
    }

    public Byte getAreaSearchFlag() {
        return areaSearchFlag;
    }

    public void setAreaSearchFlag(Byte areaSearchFlag) {
        this.areaSearchFlag = areaSearchFlag;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getRentAmountFlag() {
        return rentAmountFlag;
    }

    public void setRentAmountFlag(Byte rentAmountFlag) {
        this.rentAmountFlag = rentAmountFlag;
    }

    public Byte getIssuingLeaseFlag() {
        return issuingLeaseFlag;
    }

    public void setIssuingLeaseFlag(Byte issuingLeaseFlag) {
        this.issuingLeaseFlag = issuingLeaseFlag;
    }

    public Byte getRenewFlag() {
        return renewFlag;
    }

    public void setRenewFlag(Byte renewFlag) {
        this.renewFlag = renewFlag;
    }
}
