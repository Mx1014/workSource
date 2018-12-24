//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * Created by Wentian Wang on 2017/8/22.
 */

/**
 *<ul>
 * <li>propertyIdentifier:资产编号</li>
 * <li>chargingItemName:收费项目名称</li>
 * <li>dateStrBegin:开始日期</li>
 * <li>dateStrEnd:计费结束日期</li>
 * <li>amountReceivable:应收金额</li>
 * <li>dueDateStr:付款日期</li>
 * <li>amountReceivableWithoutTax:应收不含税</li>
 * <li>taxAmount:税额</li>
 * <li>billGroupId : 账单组id</li>
 * <li>billGroupName : 账单组名称</li>
 * <li>deleteFlag:删除状态：0：已删除；1：正常使用</li>
 * <li>dateStrGeneration:明细产生时间</li>
 *</ul>
 */
public class PaymentExpectancyDTO {
    private String propertyIdentifier;
    private String chargingItemName;
    private String dateStrBegin;
    private String dateStrEnd;
    private BigDecimal amountReceivable;
    private BigDecimal amountReceivableWithoutTax;
    private BigDecimal taxAmount;
    private String dueDateStr;

    private Long billItemId;
    
    private Long chargingItemId;
    private Long billGroupId;//物业缴费V6.3 签合同选择计价条款前，先选择账单组
    private String billGroupName;//物业缴费V6.3合同概览计价条款需要增加账单组名称字段
    //物业缴费V6.0 账单、费项表增加是否删除状态字段
    private Byte deleteFlag;
    private String dateStrGeneration;
    
	public Long getChargingItemId() {
		return chargingItemId;
	}

	public void setChargingItemId(Long chargingItemId) {
		this.chargingItemId = chargingItemId;
	}

	@Override
    public int hashCode() {
        return getBillItemId() != null ? getBillItemId().hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentExpectancyDTO)) return false;

        PaymentExpectancyDTO that = (PaymentExpectancyDTO) o;

        return getBillItemId() != null ? getBillItemId().equals(that.getBillItemId()) : that.getBillItemId() == null;
    }

    public Long getBillItemId() {
        return billItemId;
    }

    public void setBillItemId(Long billItemId) {
        this.billItemId = billItemId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getPropertyIdentifier() {
        return propertyIdentifier;
    }

    public void setPropertyIdentifier(String propertyIdentifier) {
        this.propertyIdentifier = propertyIdentifier;
    }

    public String getChargingItemName() {
        return chargingItemName;
    }

    public void setChargingItemName(String chargingItemName) {
        this.chargingItemName = chargingItemName;
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

    public BigDecimal getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(BigDecimal amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public String getDueDateStr() {
        return dueDateStr;
    }

    public void setDueDateStr(String dueDateStr) {
        this.dueDateStr = dueDateStr;
    }

	public BigDecimal getAmountReceivableWithoutTax() {
		return amountReceivableWithoutTax;
	}

	public void setAmountReceivableWithoutTax(BigDecimal amountReceivableWithoutTax) {
		this.amountReceivableWithoutTax = amountReceivableWithoutTax;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Long getBillGroupId() {
		return billGroupId;
	}

	public void setBillGroupId(Long billGroupId) {
		this.billGroupId = billGroupId;
	}

	public String getBillGroupName() {
		return billGroupName;
	}

	public void setBillGroupName(String billGroupName) {
		this.billGroupName = billGroupName;
	}

	public Byte getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Byte deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

    public String getDateStrGeneration() {
        return dateStrGeneration;
    }

    public void setDateStrGeneration(String dateStrGeneration) {
        this.dateStrGeneration = dateStrGeneration;
    }
}
