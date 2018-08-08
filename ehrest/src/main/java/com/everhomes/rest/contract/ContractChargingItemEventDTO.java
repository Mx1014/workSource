package com.everhomes.rest.contract;

import java.util.List;

import com.everhomes.discover.ItemType;
/**
 * <ul>
 *     <li>id: id</li>
 *     <li>chargingItemId: 收费项id</li>
 *     <li>chargingStandardId: 收费标准id</li>
 *     <li>billingCycle: 计费周期</li>
 *     <li>formula: 公式</li>
 *     <li>formulaType: 公式类型</li>
 *     <li>lateFeeStandardId: 滞纳金标准id</li>
 *     <li>chargingVariables: PaymentVariable对象的json字符串</li>
 *     <li>chargingStartTime: 起记日期</li>
 *     <li>chargingExpiredTime: 截止日期</li>
 *     <li>apartments: 计价条款适用资产列表</li>
 * </ul>
 * Created by tangcen on 2018/6/7.
 */
public class ContractChargingItemEventDTO {
    private Long id;
    private Integer namespaceId;
    private Long chargingItemId;
    private Long chargingStandardId;
    private Long lateFeeStandardId;
    private String formula;
    private Byte formulaType;
    private Byte billingCycle;
    private String chargingVariables;
    private Long chargingStartTime;
    private Long chargingExpiredTime;
    @ItemType(BuildingApartmentEventDTO.class)
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
	public Long getLateFeeStandardId() {
		return lateFeeStandardId;
	}
	public void setLateFeeStandardId(Long lateFeeStandardId) {
		this.lateFeeStandardId = lateFeeStandardId;
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
	public Byte getBillingCycle() {
		return billingCycle;
	}
	public void setBillingCycle(Byte billingCycle) {
		this.billingCycle = billingCycle;
	}
	public String getChargingVariables() {
		return chargingVariables;
	}
	public void setChargingVariables(String chargingVariables) {
		this.chargingVariables = chargingVariables;
	}
	public Long getChargingStartTime() {
		return chargingStartTime;
	}
	public void setChargingStartTime(Long chargingStartTime) {
		this.chargingStartTime = chargingStartTime;
	}
	public Long getChargingExpiredTime() {
		return chargingExpiredTime;
	}
	public void setChargingExpiredTime(Long chargingExpiredTime) {
		this.chargingExpiredTime = chargingExpiredTime;
	}
	public List<BuildingApartmentDTO> getApartments() {
		return apartments;
	}
	public void setApartments(List<BuildingApartmentDTO> apartments) {
		this.apartments = apartments;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apartments == null) ? 0 : apartments.hashCode());
		result = prime * result + ((billingCycle == null) ? 0 : billingCycle.hashCode());
		result = prime * result + ((chargingExpiredTime == null) ? 0 : chargingExpiredTime.hashCode());
		result = prime * result + ((chargingItemId == null) ? 0 : chargingItemId.hashCode());
		result = prime * result + ((chargingStandardId == null) ? 0 : chargingStandardId.hashCode());
		result = prime * result + ((chargingStartTime == null) ? 0 : chargingStartTime.hashCode());
		result = prime * result + ((chargingVariables == null) ? 0 : chargingVariables.hashCode());
		result = prime * result + ((formula == null) ? 0 : formula.hashCode());
		result = prime * result + ((formulaType == null) ? 0 : formulaType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lateFeeStandardId == null) ? 0 : lateFeeStandardId.hashCode());
		result = prime * result + ((namespaceId == null) ? 0 : namespaceId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContractChargingItemEventDTO other = (ContractChargingItemEventDTO) obj;
		if (apartments == null) {
			if (other.apartments != null)
				return false;
		} else if (!apartments.equals(other.apartments))
			return false;
		if (billingCycle == null) {
			if (other.billingCycle != null)
				return false;
		} else if (!billingCycle.equals(other.billingCycle))
			return false;
		if (chargingExpiredTime == null) {
			if (other.chargingExpiredTime != null)
				return false;
		} else if (!chargingExpiredTime.equals(other.chargingExpiredTime))
			return false;
		if (chargingItemId == null) {
			if (other.chargingItemId != null)
				return false;
		} else if (!chargingItemId.equals(other.chargingItemId))
			return false;
		if (chargingStandardId == null) {
			if (other.chargingStandardId != null)
				return false;
		} else if (!chargingStandardId.equals(other.chargingStandardId))
			return false;
		if (chargingStartTime == null) {
			if (other.chargingStartTime != null)
				return false;
		} else if (!chargingStartTime.equals(other.chargingStartTime))
			return false;
		if (chargingVariables == null) {
			if (other.chargingVariables != null)
				return false;
		} else if (!chargingVariables.equals(other.chargingVariables))
			return false;
		if (formula == null) {
			if (other.formula != null)
				return false;
		} else if (!formula.equals(other.formula))
			return false;
		if (formulaType == null) {
			if (other.formulaType != null)
				return false;
		} else if (!formulaType.equals(other.formulaType))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lateFeeStandardId == null) {
			if (other.lateFeeStandardId != null)
				return false;
		} else if (!lateFeeStandardId.equals(other.lateFeeStandardId))
			return false;
		if (namespaceId == null) {
			if (other.namespaceId != null)
				return false;
		} else if (!namespaceId.equals(other.namespaceId))
			return false;
		return true;
	}
    
    
	
    
	    
}

