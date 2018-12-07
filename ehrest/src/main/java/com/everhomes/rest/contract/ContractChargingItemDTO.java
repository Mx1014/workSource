package com.everhomes.rest.contract;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.asset.PaymentVariable;
import com.everhomes.rest.asset.VariableIdAndValue;

import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>chargingItemId: 收费项id</li>
 *     <li>chargingItemName: 收费项名称</li>
 *     <li>chargingStandardId: 收费标准id</li>
 *     <li>chargingStandardName: 收费标准名称</li>
 *     <li>billingCycle: 计费周期</li>
 *     <li>formula: 公式</li>
 *     <li>formulaType: 公式类型</li>
 *     <li>lateFeeStandardId: 滞纳金标准id</li>
 *     <li>chargingVariables: PaymentVariable对象的json字符串</li>
 *     <li>chargingStartTime: 起记日期</li>
 *     <li>chargingExpiredTime: 截止日期</li>
 *     <li>apartments: 计价条款适用资产列表 参考{@link com.everhomes.rest.contract.BuildingApartmentDTO}</li>
 *     <li>billGroupId : 账单组id</li>
 *     <li>billGroupName : 账单组名称</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class ContractChargingItemDTO {
    private Long id;
    private Integer namespaceId;
    private Long chargingItemId;
    private String chargingItemName;
    private Long chargingStandardId;
    private String chargingStandardName;
    private Long lateFeeStandardId;
    private String lateFeeStandardName;
    private String lateFeeformula;
    private String formula;
    private Byte formulaType;
    private Byte billingCycle;
    private String chargingVariables;
    private Long chargingStartTime;
    private Long chargingExpiredTime;
    
    @ItemType(BuildingApartmentDTO.class)
    private List<BuildingApartmentDTO> apartments;
    private Long billGroupId;//物业缴费V6.3 签合同选择计价条款前，先选择账单组
    private String billGroupName;//物业缴费V6.3合同概览计价条款需要增加账单组名称字段
    private Byte oneTimeBillStatus;//缺陷 #42424 是否是一次性产生费用 add by 杨崇鑫

	public String getLateFeeformula() {
        return lateFeeformula;
    }

    public void setLateFeeformula(String lateFeeformula) {
        this.lateFeeformula = lateFeeformula;
    }

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

    public String getChargingItemName() {
        return chargingItemName;
    }

    public void setChargingItemName(String chargingItemName) {
        this.chargingItemName = chargingItemName;
    }

    public Long getChargingStandardId() {
        return chargingStandardId;
    }

    public void setChargingStandardId(Long chargingStandardId) {
        this.chargingStandardId = chargingStandardId;
    }

    public String getChargingStandardName() {
        return chargingStandardName;
    }

    public void setChargingStandardName(String chargingStandardName) {
        this.chargingStandardName = chargingStandardName;
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

    public String getLateFeeStandardName() {
        return lateFeeStandardName;
    }

    public void setLateFeeStandardName(String lateFeeStandardName) {
        this.lateFeeStandardName = lateFeeStandardName;
    }

    public String getChargingVariables() {
        return chargingVariables;
    }

    public void setChargingVariables(String chargingVariables) {
        this.chargingVariables = chargingVariables;
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

	public Byte getOneTimeBillStatus() {
		return oneTimeBillStatus;
	}

	public void setOneTimeBillStatus(Byte oneTimeBillStatus) {
		this.oneTimeBillStatus = oneTimeBillStatus;
	}
    
}
