//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>billItemId:收费项目的id</li>
 * <li>billItemName:收费项目名称</li>
 * <li>amountReceivable:应收金额</li>
 * <li>description:描述</li>
 * <li>dateStr:账期</li>
 * <li>addressId:地址id</li>
 * <li>apartmentName:楼栋</li>
 * <li>buildingName:门牌</li>
 * <li>billGroupRuleId:计价标准id</li>
 * <li>lateFineAmount:滞纳金</li>
 * <li>chargingItemsId:收费项目对应的账单组费项字典id</li>
 * <li>energyConsume: 费项的用量</li>
 *</ul>
 */
public class BillItemDTO {
    private Long billItemId;
    private String billItemName;
    private BigDecimal amountReceivable;
    private String description;
    private String dateStr;
    private Long addressId;
    private String apartmentName;
    private String buildingName;
    private Long billGroupRuleId;
    private BigDecimal lateFineAmount;
    private Long chargingItemsId;
    //费项增加用量字段
    private String energyConsume;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Long getBillItemId() {
        return billItemId;
    }

    public void setBillItemId(Long billItemId) {
        this.billItemId = billItemId;
    }

    public String getBillItemName() {
        return billItemName;
    }

    public void setBillItemName(String billItemName) {
        this.billItemName = billItemName;
    }

    public BigDecimal getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(BigDecimal amountReceivable) {
        amountReceivable = amountReceivable.setScale(2,BigDecimal.ROUND_CEILING);
        this.amountReceivable = amountReceivable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getBillGroupRuleId() {
        return billGroupRuleId;
    }

    public void setBillGroupRuleId(Long billGroupRuleId) {
        this.billGroupRuleId = billGroupRuleId;
    }

    public BillItemDTO() {

    }

	public BigDecimal getLateFineAmount() {
		return lateFineAmount;
	}

	public void setLateFineAmount(BigDecimal lateFineAmount) {
		this.lateFineAmount = lateFineAmount;
	}

	public Long getChargingItemsId() {
		return chargingItemsId;
	}

	public void setChargingItemsId(Long chargingItemsId) {
		this.chargingItemsId = chargingItemsId;
	}

	public String getEnergyConsume() {
		return energyConsume;
	}

	public void setEnergyConsume(String energyConsume) {
		this.energyConsume = energyConsume;
	}
}
