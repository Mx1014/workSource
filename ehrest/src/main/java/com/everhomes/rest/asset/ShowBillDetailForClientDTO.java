//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>billItemName:项目名称</li>
 * <li>amountOwed:待缴金额</li>
 * <li>addressName:地址</li>
 * <li>payStatus:清账状态</li>
 * <li>amountReceivable:应缴金额</li>
 * <li>dateStrBegin:计费开始时间</li>
 * <li>dateStrEnd:计费结束时间</li>
 * <li>dateStr:账期</li>
 * <li>energyConsume: 费项的用量</li>
 * <li>energyUnit: 费项的用量单位</li>
 * <li>isConfigSubtraction:1：已经配置了减免费项，0：代表没有配置减免费项</li>
 * <li>projectChargingItemName:费项显示名称</li>
 *</ul>
 */
public class ShowBillDetailForClientDTO {
    private String billItemName;
    private BigDecimal amountOwed;
    private String addressName;
    private String payStatus;
    private BigDecimal amountReceivable;
    private String dateStrBegin;
    private String dateStrEnd;
    private String dateStr;
    //费项增加用量字段
    private String energyConsume;
    //费项增加用量单位字段
    private String energyUnit;
    private Byte isConfigSubtraction;
    //APP端也需更新:备注名称改为显示名称
    private String projectChargingItemName;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getBillItemName() {
        return billItemName;
    }

    public BigDecimal getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(BigDecimal amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public String getAddressName() {
        return addressName;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public void setBillItemName(String billItemName) {
        this.billItemName = billItemName;
    }

    public BigDecimal getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(BigDecimal amountOwed) {
        this.amountOwed = amountOwed;
    }

    public ShowBillDetailForClientDTO() {

    }

    public String getDateStrBegin() {
        return dateStrBegin;
    }

    public void setDateStrBegin(String dateStrBegin) {
        this.dateStrBegin = dateStrBegin;
    }

    public String getDateStrEnd() {
        return dateStrEnd;
    }

    public void setDateStrEnd(String dateStrEnd) {
        this.dateStrEnd = dateStrEnd;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

	public String getEnergyConsume() {
		return energyConsume;
	}

	public void setEnergyConsume(String energyConsume) {
		this.energyConsume = energyConsume;
	}

	public String getEnergyUnit() {
		return energyUnit;
	}

	public void setEnergyUnit(String energyUnit) {
		this.energyUnit = energyUnit;
	}

	public Byte getIsConfigSubtraction() {
		return isConfigSubtraction;
	}

	public void setIsConfigSubtraction(Byte isConfigSubtraction) {
		this.isConfigSubtraction = isConfigSubtraction;
	}

	public String getProjectChargingItemName() {
		return projectChargingItemName;
	}

	public void setProjectChargingItemName(String projectChargingItemName) {
		this.projectChargingItemName = projectChargingItemName;
	}
}
