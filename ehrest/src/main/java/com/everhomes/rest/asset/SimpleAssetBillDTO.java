package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;


import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: 账单主键id</li>
 *     <li>ownerId：账单所属物业公司id</li>
 *     <li>ownerType：账单所属物业公司类型</li>
 *     <li>targetId：账单所属园区id</li>
 *     <li>targetType：账单所属园区类型</li>
 *     <li>templateVersion: 版本号</li>
 *     <li>accountPeriod: 账期</li>
 *     <li>addressId: 地址id</li>
 *     <li>buildingName: 楼栋名</li>
 *     <li>apartmentName: 门牌名</li>
 *     <li>contactNo: 催缴手机号</li>
 *     <li>periodAccountAmount: 总计应收</li>
 *     <li>unpaidPeriodAccountAmount: 待缴费用</li>
 *     <li>status: 状态 参考{@link com.everhomes.rest.asset.AssetBillStatus}</li>
 * </ul>
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

    private BigDecimal periodAccountAmount;

    private BigDecimal unpaidPeriodAccountAmount;

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

    public BigDecimal getPeriodAccountAmount() {
        return periodAccountAmount;
    }

    public void setPeriodAccountAmount(BigDecimal periodAccountAmount) {
        this.periodAccountAmount = periodAccountAmount;
    }

    public BigDecimal getUnpaidPeriodAccountAmount() {
        return unpaidPeriodAccountAmount;
    }

    public void setUnpaidPeriodAccountAmount(BigDecimal unpaidPeriodAccountAmount) {
        this.unpaidPeriodAccountAmount = unpaidPeriodAccountAmount;
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
