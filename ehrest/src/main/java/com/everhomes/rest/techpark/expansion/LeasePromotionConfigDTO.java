package com.everhomes.rest.techpark.expansion;

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
