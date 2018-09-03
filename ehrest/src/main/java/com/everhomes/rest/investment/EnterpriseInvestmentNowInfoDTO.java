package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class EnterpriseInvestmentNowInfoDTO {

    private Long id;
    private Integer namespaceId;
    private Long communityId;
    private String nowAddress;
    private BigDecimal nowRental;
    private Byte nowRentalUnit;
    private Double nowArea;
    private Timestamp nowContractEndDate;
    private Byte status;
    private Long customerId;
    private Timestamp createTime;
    private String createBy;
    private Timestamp operatorTime;
    private String operatorBy;

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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getNowAddress() {
        return nowAddress;
    }

    public void setNowAddress(String nowAddress) {
        this.nowAddress = nowAddress;
    }

    public BigDecimal getNowRental() {
        return nowRental;
    }

    public void setNowRental(BigDecimal nowRental) {
        this.nowRental = nowRental;
    }

    public Byte getNowRentalUnit() {
        return nowRentalUnit;
    }

    public void setNowRentalUnit(Byte nowRentalUnit) {
        this.nowRentalUnit = nowRentalUnit;
    }

    public Double getNowArea() {
        return nowArea;
    }

    public void setNowArea(Double nowArea) {
        this.nowArea = nowArea;
    }

    public Timestamp getNowContractEndDate() {
        return nowContractEndDate;
    }

    public void setNowContractEndDate(Timestamp nowContractEndDate) {
        this.nowContractEndDate = nowContractEndDate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Timestamp getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Timestamp operatorTime) {
        this.operatorTime = operatorTime;
    }

    public String getOperatorBy() {
        return operatorBy;
    }

    public void setOperatorBy(String operatorBy) {
        this.operatorBy = operatorBy;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
