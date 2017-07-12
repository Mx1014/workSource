package com.everhomes.rest.techpark.expansion;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>id: 任务log Id</li>
 * <li>namespaceId: 域空间</li>
 * <li>rentAmountFlag: 租金启用标志</li>
 * <li>issuingLeaseFlag: 用户是否可发布招租信息</li>
 * <li>issuerManageFlag: 设置按钮显示与否</li>
 * <li>parkIndroduceFlag: 园区介绍是否显示</li>
 * <li>renewFlag: 续租是否显示</li>
 * <li>areaSearchFlag: 是否支持面积搜索</li>
 * <li>consultFlag: 是否支持面积搜索</li>
 * <li>displayNames: 显示名称字符串</li>
 * <li>displayOrders: 显示排序，与名称对应</li>
 * </ul>
 */
public class LeasePromotionConfigDTO {
    private Long id;
    private Integer namespaceId;
    private Byte rentAmountFlag;
    private Byte issuingLeaseFlag;
    private Byte issuerManageFlag;
    private Byte parkIndroduceFlag;
    private Byte renewFlag;
    private Byte areaSearchFlag;
    private Byte consultFlag;
//    private String displayNameStr;
//    private String displayOrderStr;
    @ItemType(String.class)
    private List<String> displayNames;
    @ItemType(Integer.class)
    private List<Integer> displayOrders;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Byte getIssuerManageFlag() {
        return issuerManageFlag;
    }

    public void setIssuerManageFlag(Byte issuerManageFlag) {
        this.issuerManageFlag = issuerManageFlag;
    }

    public Byte getParkIndroduceFlag() {
        return parkIndroduceFlag;
    }

    public void setParkIndroduceFlag(Byte parkIndroduceFlag) {
        this.parkIndroduceFlag = parkIndroduceFlag;
    }

    public Byte getRenewFlag() {
        return renewFlag;
    }

    public void setRenewFlag(Byte renewFlag) {
        this.renewFlag = renewFlag;
    }
}
