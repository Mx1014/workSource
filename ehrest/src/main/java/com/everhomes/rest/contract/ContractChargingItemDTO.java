package com.everhomes.rest.contract;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>chargingItemId: 收费项id</li>
 *     <li>chargingStandardId: 收费标准id</li>
 *     <li>billingCycle: 计费周期</li>
 *     <li>formula: 公式</li>
 *     <li>formulaType: 公式类型</li>
 *     <li>lateFeeStandardId: 滞纳金标准id</li>
 *     <li>chargingAmountValue: 收费金额参数</li>
 *     <li>chargingStartTime: 起记日期</li>
 *     <li>chargingExpiredTime: 截止日期</li>
 *     <li>apartments: 计价条款适用资产列表 参考{@link com.everhomes.rest.contract.BuildingApartmentDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class ContractChargingItemDTO {
    private Long id;
    private Integer namespaceId;
    private Long chargingItemId;
    private Long chargingStandardId;
    private Long lateFeeStandardId;
    private String formula;
    private Byte formulaType;
    private Byte billingCycle;
    private Long chargingAmountValue;
    private Long chargingStartTime;
    private Long chargingExpiredTime;
    @ItemType(BuildingApartmentDTO.class)
    private List<BuildingApartmentDTO> apartments;

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

    public Byte getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(Byte billingCycle) {
        this.billingCycle = billingCycle;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Byte getFormulaType() {
        return formulaType;
    }

    public void setFormulaType(Byte formulaType) {
        this.formulaType = formulaType;
    }

    public List<BuildingApartmentDTO> getApartments() {
        return apartments;
    }

    public void setApartments(List<BuildingApartmentDTO> apartments) {
        this.apartments = apartments;
    }

    public Long getChargingAmountValue() {
        return chargingAmountValue;
    }

    public void setChargingAmountValue(Long chargingAmountValue) {
        this.chargingAmountValue = chargingAmountValue;
    }

    public Long getChargingExpiredTime() {
        return chargingExpiredTime;
    }

    public void setChargingExpiredTime(Long chargingExpiredTime) {
        this.chargingExpiredTime = chargingExpiredTime;
    }

    public Long getChargingItemId() {
        return chargingItemId;
    }

    public void setChargingItemId(Long chargingItemId) {
        this.chargingItemId = chargingItemId;
    }

    public Long getChargingStandardId() {
        return chargingStandardId;
    }

    public void setChargingStandardId(Long chargingStandardId) {
        this.chargingStandardId = chargingStandardId;
    }

    public Long getChargingStartTime() {
        return chargingStartTime;
    }

    public void setChargingStartTime(Long chargingStartTime) {
        this.chargingStartTime = chargingStartTime;
    }

    public Long getLateFeeStandardId() {
        return lateFeeStandardId;
    }

    public void setLateFeeStandardId(Long lateFeeStandardId) {
        this.lateFeeStandardId = lateFeeStandardId;
    }
}
