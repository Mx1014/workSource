package com.everhomes.rest.techpark.expansion;

/**
 * Created by Administrator on 2017/3/15.
 */
public class LeasePromotionConfigDTO {
    private Long id;
    private Integer namespaceId;
    private Byte rentAmountFlag;
    private Byte issuingLeaseFlag;
    private Byte issuerManageFlag;
    private Byte parkIndroduceFlag;
    private Byte renewFlag;

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
