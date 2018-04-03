package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id：账单id</li>
 *     <li>ownerId：账单所属物业公司id</li>
 *     <li>ownerType：账单所属物业公司类型</li>
 *     <li>targetId：账单所属园区id</li>
 *     <li>targetType：账单所属园区类型</li>
 *     <li>buildingName：楼栋名</li>
 *     <li>apartmentName：门牌名</li>
 *     <li>source：账单产生模式：0: auto 自动, 1: third party 第三方, 2: manual 手动导入</li>
 *     <li>accountPeriod：账期</li>
 *     <li>addressId：门牌id</li>
 *     <li>contactNo：催缴手机号</li>
 *     <li>rental：租金</li>
 *     <li>propertyManagementFee：物业管理费</li>
 *     <li>unitMaintenanceFund：本体维修基金</li>
 *     <li>lateFee：滞纳金</li>
 *     <li>privateWaterFee：自用水费</li>
 *     <li>privateElectricityFee：自用电费</li>
 *     <li>publicWaterFee：公共部分水费</li>
 *     <li>publicElectricityFee：公共部分电费</li>
 *     <li>wasteDisposalFee：垃圾处理费</li>
 *     <li>pollutionDischargeFee：排污处理费</li>
 *     <li>extraAirConditionFee：加时空调费</li>
 *     <li>coolingWaterFee：冷却水使用费</li>
 *     <li>weakCurrentSlotFee：弱电线槽使用费</li>
 *     <li>depositFromLease：租赁保证金</li>
 *     <li>maintenanceFee：维修费</li>
 *     <li>gasOilProcessFee：燃气燃油加工费</li>
 *     <li>hatchServiceFee：孵化服务费</li>
 *     <li>pressurizedFee：加压费</li>
 *     <li>parkingFee：停车费</li>
 *     <li>other：其他</li>
 *     <li>templateVersion: 版本号</li>
 * </ul>
 */
public class UpdateAssetBillCommand {

    @NotNull
    private Long id;

    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    @NotNull
    private Long targetId;

    @NotNull
    private String targetType;

    private String buildingName;

    private String apartmentName;

    private Byte source;
    private Long accountPeriod;
    private Long addressId;
    private String contactNo;
    private BigDecimal rental;
    private BigDecimal propertyManagementFee;
    private BigDecimal unitMaintenanceFund;
    private BigDecimal lateFee;
    private BigDecimal privateWaterFee;
    private BigDecimal privateElectricityFee;
    private BigDecimal publicWaterFee;
    private BigDecimal publicElectricityFee;
    private BigDecimal wasteDisposalFee;
    private BigDecimal pollutionDischargeFee;
    private BigDecimal extraAirConditionFee;
    private BigDecimal coolingWaterFee;
    private BigDecimal weakCurrentSlotFee;
    private BigDecimal depositFromLease;
    private BigDecimal maintenanceFee;
    private BigDecimal gasOilProcessFee;
    private BigDecimal hatchServiceFee;
    private BigDecimal pressurizedFee;
    private BigDecimal parkingFee;
    private BigDecimal other;
    private Long templateVersion;

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

    public Byte getSource() {
        return source;
    }

    public void setSource(Byte source) {
        this.source = source;
    }

    public Long getAccountPeriod() {
        return accountPeriod;
    }

    public void setAccountPeriod(Long accountPeriod) {
        this.accountPeriod = accountPeriod;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public BigDecimal getRental() {
        return rental;
    }

    public void setRental(BigDecimal rental) {
        this.rental = rental;
    }

    public BigDecimal getPropertyManagementFee() {
        return propertyManagementFee;
    }

    public void setPropertyManagementFee(BigDecimal propertyManagementFee) {
        this.propertyManagementFee = propertyManagementFee;
    }

    public BigDecimal getUnitMaintenanceFund() {
        return unitMaintenanceFund;
    }

    public void setUnitMaintenanceFund(BigDecimal unitMaintenanceFund) {
        this.unitMaintenanceFund = unitMaintenanceFund;
    }

    public BigDecimal getLateFee() {
        return lateFee;
    }

    public void setLateFee(BigDecimal lateFee) {
        this.lateFee = lateFee;
    }

    public BigDecimal getPrivateWaterFee() {
        return privateWaterFee;
    }

    public void setPrivateWaterFee(BigDecimal privateWaterFee) {
        this.privateWaterFee = privateWaterFee;
    }

    public BigDecimal getPrivateElectricityFee() {
        return privateElectricityFee;
    }

    public void setPrivateElectricityFee(BigDecimal privateElectricityFee) {
        this.privateElectricityFee = privateElectricityFee;
    }

    public BigDecimal getPublicWaterFee() {
        return publicWaterFee;
    }

    public void setPublicWaterFee(BigDecimal publicWaterFee) {
        this.publicWaterFee = publicWaterFee;
    }

    public BigDecimal getPublicElectricityFee() {
        return publicElectricityFee;
    }

    public void setPublicElectricityFee(BigDecimal publicElectricityFee) {
        this.publicElectricityFee = publicElectricityFee;
    }

    public BigDecimal getWasteDisposalFee() {
        return wasteDisposalFee;
    }

    public void setWasteDisposalFee(BigDecimal wasteDisposalFee) {
        this.wasteDisposalFee = wasteDisposalFee;
    }

    public BigDecimal getPollutionDischargeFee() {
        return pollutionDischargeFee;
    }

    public void setPollutionDischargeFee(BigDecimal pollutionDischargeFee) {
        this.pollutionDischargeFee = pollutionDischargeFee;
    }

    public BigDecimal getExtraAirConditionFee() {
        return extraAirConditionFee;
    }

    public void setExtraAirConditionFee(BigDecimal extraAirConditionFee) {
        this.extraAirConditionFee = extraAirConditionFee;
    }

    public BigDecimal getCoolingWaterFee() {
        return coolingWaterFee;
    }

    public void setCoolingWaterFee(BigDecimal coolingWaterFee) {
        this.coolingWaterFee = coolingWaterFee;
    }

    public BigDecimal getWeakCurrentSlotFee() {
        return weakCurrentSlotFee;
    }

    public void setWeakCurrentSlotFee(BigDecimal weakCurrentSlotFee) {
        this.weakCurrentSlotFee = weakCurrentSlotFee;
    }

    public BigDecimal getDepositFromLease() {
        return depositFromLease;
    }

    public void setDepositFromLease(BigDecimal depositFromLease) {
        this.depositFromLease = depositFromLease;
    }

    public BigDecimal getMaintenanceFee() {
        return maintenanceFee;
    }

    public void setMaintenanceFee(BigDecimal maintenanceFee) {
        this.maintenanceFee = maintenanceFee;
    }

    public BigDecimal getGasOilProcessFee() {
        return gasOilProcessFee;
    }

    public void setGasOilProcessFee(BigDecimal gasOilProcessFee) {
        this.gasOilProcessFee = gasOilProcessFee;
    }

    public BigDecimal getHatchServiceFee() {
        return hatchServiceFee;
    }

    public void setHatchServiceFee(BigDecimal hatchServiceFee) {
        this.hatchServiceFee = hatchServiceFee;
    }

    public BigDecimal getPressurizedFee() {
        return pressurizedFee;
    }

    public void setPressurizedFee(BigDecimal pressurizedFee) {
        this.pressurizedFee = pressurizedFee;
    }

    public BigDecimal getParkingFee() {
        return parkingFee;
    }

    public void setParkingFee(BigDecimal parkingFee) {
        this.parkingFee = parkingFee;
    }

    public BigDecimal getOther() {
        return other;
    }

    public void setOther(BigDecimal other) {
        this.other = other;
    }

    public Long getTemplateVersion() {
        return templateVersion;
    }

    public void setTemplateVersion(Long templateVersion) {
        this.templateVersion = templateVersion;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
