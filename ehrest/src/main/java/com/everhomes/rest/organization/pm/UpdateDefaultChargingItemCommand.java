package com.everhomes.rest.organization.pm;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 园区id</li>
 *     <li>ownerType: 所属主体类型</li>
 *     <li>ownerId: 所属主体id</li>
 *     <li>chargingItemId: 收费项id</li>
 *     <li>chargingItemName: 收费项名称</li>
 *     <li>chargingStandardId: 收费标准id</li>
 *     <li>chargingStandardName: 收费标准名称</li>
 *     <li>formula: 公式</li>
 *     <li>formulaType: 公式类型</li>
 *     <li>billingCycle: 计费周期</li>
 *     <li>lateFeeStandardId: 滞纳金标准id</li>
 *     <li>chargingVariables: PaymentVariable对象的json字符串</li>
 *     <li>chargingStartTime: 起记日期</li>
 *     <li>chargingExpiredTime: 截止日期</li>
 *     <li>apartments: 计价条款适用资产列表 参考{@link com.everhomes.rest.organization.pm.DefaultChargingItemPropertyDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/10/26.
 */
public class UpdateDefaultChargingItemCommand {
    private Long id;
    private Integer namespaceId;
    private Long communityId;
    private String ownerType;
    private Long ownerId;
    private Long chargingItemId;
    private Long chargingStandardId;
    private String formula;
    private Byte formulaType;
    private Byte billingCycle;
    private Long lateFeeStandardId;
    private String chargingVariables;
    private Long chargingStartTime;
    private Long chargingExpiredTime;
    @ItemType(DefaultChargingItemPropertyDTO.class)
    private List<DefaultChargingItemPropertyDTO> apartments;
    private Long billGroupId;

    public List<DefaultChargingItemPropertyDTO> getApartments() {
        return apartments;
    }

    public Byte getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(Byte billingCycle) {
        this.billingCycle = billingCycle;
    }

    public void setApartments(List<DefaultChargingItemPropertyDTO> apartments) {
        this.apartments = apartments;
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

    public String getChargingVariables() {
        return chargingVariables;
    }

    public void setChargingVariables(String chargingVariables) {
        this.chargingVariables = chargingVariables;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLateFeeStandardId() {
        return lateFeeStandardId;
    }

    public void setLateFeeStandardId(Long lateFeeStandardId) {
        this.lateFeeStandardId = lateFeeStandardId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

	public Long getBillGroupId() {
		return billGroupId;
	}

	public void setBillGroupId(Long billGroupId) {
		this.billGroupId = billGroupId;
	}
}
