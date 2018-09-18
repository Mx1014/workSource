//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>billItemId:收费项目的id</li>
 * <li>billItemName:收费项目名称</li>
 * <li>description:描述</li>
 * <li>dateStr:账期</li>
 * <li>addressId:地址id</li>
 * <li>apartmentName:楼栋</li>
 * <li>buildingName:门牌</li>
 * <li>billGroupRuleId:计价标准id</li>
 * <li>lateFineAmount:滞纳金</li>
 * <li>chargingItemsId:收费项目对应的账单组费项字典id</li>
 * <li>energyConsume: 费项的用量</li>
 * <li>itemFineType: 费项类型，eh_payment_bill_items：费项，eh_payment_late_fine：滞纳金 ，参考{com.everhomes.rest.asset.AssetItemFineType}</li>
 * <li>itemType:费项类型，eh_payment_bill_items：费项（如：物业费），eh_payment_late_fine：减免滞纳金（如：物业费滞纳金）参考{com.everhomes.rest.asset.AssetSubtractionType}</li>
 * <li>amountReceivable:应收金额</li>
 * <li>amountReceivableWithoutTax:应收（不含税）</li>
 * <li>taxAmount: 税额</li>
 * <li>taxRate: 税率</li>
 * <li>sourceType:各个业务系统定义的唯一标识</li>
 * <li>sourceId:各个业务系统定义的唯一标识</li>
 * <li>sourceName:账单来源（如：停车缴费）</li>
 * <li>consumeUserId:企业下面的某个人的ID</li>
 * <li>deleteFlag:删除状态：0：已删除；1：正常使用</li>
 * <li>canDelete:0：不可删除；1：可删除</li>
 * <li>canModify:0：不可编辑；1：可编辑</li>
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
    //增加费项类型字段
    private String itemFineType; 
    private String itemType;//增加费项类型
    private BigDecimal amountReceivableWithoutTax;//增加应收（不含税）
    private BigDecimal taxAmount;//增加税额
    private BigDecimal taxRate;//增加税率
    //新增账单来源信息
    private String sourceType;
    private Long sourceId;
    private String sourceName;
    private Long consumeUserId;
    //物业缴费V6.0 账单、费项表增加是否删除状态字段
    private Byte deleteFlag;
    //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
    private Byte canDelete;
    private Byte canModify;

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

	public String getItemFineType() {
		return itemFineType;
	}

	public void setItemFineType(String itemFineType) {
		this.itemFineType = itemFineType;
	}
	
	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
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

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Long getConsumeUserId() {
		return consumeUserId;
	}

	public void setConsumeUserId(Long consumeUserId) {
		this.consumeUserId = consumeUserId;
	}

	public Byte getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Byte deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Byte getCanDelete() {
		return canDelete;
	}

	public void setCanDelete(Byte canDelete) {
		this.canDelete = canDelete;
	}

	public Byte getCanModify() {
		return canModify;
	}

	public void setCanModify(Byte canModify) {
		this.canModify = canModify;
	}
}
