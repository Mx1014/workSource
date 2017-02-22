package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;


import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/2/21.
 */
public class SimpleAssetBillDTO {

    private Long id;

    private Long ownerId;

    private String ownerType;

    private Long targetId;

    private String targetType;

    private Long templateVersion;

    private Timestamp accountPeriod;

    private Long addressId;

    private String buildingName;

    private String apartmentName;

    private String contactNo;

    private BigDecimal periodUnpaidAccountAmount;

    private Byte status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTemplateVersion() {
        return templateVersion;
    }

    public void setTemplateVersion(Long templateVersion) {
        this.templateVersion = templateVersion;
    }

    public Timestamp getAccountPeriod() {
        return accountPeriod;
    }

    public void setAccountPeriod(Timestamp accountPeriod) {
        this.accountPeriod = accountPeriod;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public BigDecimal getPeriodUnpaidAccountAmount() {
        return periodUnpaidAccountAmount;
    }

    public void setPeriodUnpaidAccountAmount(BigDecimal periodUnpaidAccountAmount) {
        this.periodUnpaidAccountAmount = periodUnpaidAccountAmount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
