package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class EnterpriseInvestmentDemandDTO {

    private Long id;
    private Integer namespaceId;
    private Long communityId;
    private String expectedLocation;
    private Double demandAreaMin;
    private Double demandAreaMax;
    private BigDecimal demandPriceMin;
    private BigDecimal demandPriceMax;
    private Byte demandPriceUnit;
    private Byte buyOrLease;
    private String expectAddress;
    private String demandVersion;
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

    public String getExpectedLocation() {
        return expectedLocation;
    }

    public void setExpectedLocation(String expectedLocation) {
        this.expectedLocation = expectedLocation;
    }

    public Double getDemandAreaMin() {
        return demandAreaMin;
    }

    public void setDemandAreaMin(Double demandAreaMin) {
        this.demandAreaMin = demandAreaMin;
    }

    public Double getDemandAreaMax() {
        return demandAreaMax;
    }

    public void setDemandAreaMax(Double demandAreaMax) {
        this.demandAreaMax = demandAreaMax;
    }

    public BigDecimal getDemandPriceMin() {
        return demandPriceMin;
    }

    public void setDemandPriceMin(BigDecimal demandPriceMin) {
        this.demandPriceMin = demandPriceMin;
    }

    public BigDecimal getDemandPriceMax() {
        return demandPriceMax;
    }

    public void setDemandPriceMax(BigDecimal demandPriceMax) {
        this.demandPriceMax = demandPriceMax;
    }

    public Byte getDemandPriceUnit() {
        return demandPriceUnit;
    }

    public void setDemandPriceUnit(Byte demandPriceUnit) {
        this.demandPriceUnit = demandPriceUnit;
    }

    public Byte getBuyOrLease() {
        return buyOrLease;
    }

    public void setBuyOrLease(Byte buyOrLease) {
        this.buyOrLease = buyOrLease;
    }

    public String getExpectAddress() {
        return expectAddress;
    }

    public void setExpectAddress(String expectAddress) {
        this.expectAddress = expectAddress;
    }

    public String getDemandVersion() {
        return demandVersion;
    }

    public void setDemandVersion(String demandVersion) {
        this.demandVersion = demandVersion;
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
