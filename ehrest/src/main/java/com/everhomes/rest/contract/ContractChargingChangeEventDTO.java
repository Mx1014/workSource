package com.everhomes.rest.contract;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>chargingItemId: 收费项id</li>
 *     <li>chargingItemName: 收费项名称</li>
 *     <li>changeType: 计划类型 参考{@link com.everhomes.rest.contract.ChangeType}</li>
 *     <li>changeMethod: 调整类型 参考{@link com.everhomes.rest.contract.ChangeMethod}</li>
 *     <li>changePeriod: 调整时间</li>
 *     <li>periodUnit: 调整时间单位 参考{@link com.everhomes.rest.contract.PeriodUnit}</li>
 *     <li>changeRange: 调整幅度</li>
 *     <li>changeStartTime: 执行开始日期</li>
 *     <li>changeExpiredTime: 执行结束日期</li>
 *     <li>remark: 备注</li>
 *     <li>apartments: 计划适用资产列表 参考{@link com.everhomes.rest.contract.BuildingApartmentDTO}</li>
 *     <li>changeDurationDays: 调整的天数</li>
 * </ul>
 * Created by tangcen on 2018/6/7.
 */
public class ContractChargingChangeEventDTO {
    private Long id;
    private Integer namespaceId;
    private Long chargingItemId;
    private Byte changeType;
    private Byte changeMethod;
    private Integer changePeriod;
    private Byte periodUnit;
    private BigDecimal changeRange;
    private Long changeStartTime;
    private Long changeExpiredTime;
    @ItemType(BuildingApartmentDTO.class)
    private List<BuildingApartmentDTO> apartments;
    private String remark;
    // 增加免租天数
    private Integer changeDurationDays;
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
	public Byte getChangeType() {
		return changeType;
	}
	public void setChangeType(Byte changeType) {
		this.changeType = changeType;
	}
	public Byte getChangeMethod() {
		return changeMethod;
	}
	public void setChangeMethod(Byte changeMethod) {
		this.changeMethod = changeMethod;
	}
	public Integer getChangePeriod() {
		return changePeriod;
	}
	public void setChangePeriod(Integer changePeriod) {
		this.changePeriod = changePeriod;
	}
	public Byte getPeriodUnit() {
		return periodUnit;
	}
	public void setPeriodUnit(Byte periodUnit) {
		this.periodUnit = periodUnit;
	}
	public BigDecimal getChangeRange() {
		return changeRange;
	}
	public void setChangeRange(BigDecimal changeRange) {
		this.changeRange = changeRange;
	}
	public Long getChangeStartTime() {
		return changeStartTime;
	}
	public void setChangeStartTime(Long changeStartTime) {
		this.changeStartTime = changeStartTime;
	}
	public Long getChangeExpiredTime() {
		return changeExpiredTime;
	}
	public void setChangeExpiredTime(Long changeExpiredTime) {
		this.changeExpiredTime = changeExpiredTime;
	}
	
	public List<BuildingApartmentDTO> getApartments() {
		return apartments;
	}
	public void setApartments(List<BuildingApartmentDTO> apartments) {
		this.apartments = apartments;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getChangeDurationDays() {
		return changeDurationDays;
	}
	public void setChangeDurationDays(Integer changeDurationDays) {
		this.changeDurationDays = changeDurationDays;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apartments == null) ? 0 : apartments.hashCode());
		result = prime * result + ((changeDurationDays == null) ? 0 : changeDurationDays.hashCode());
		result = prime * result + ((changeExpiredTime == null) ? 0 : changeExpiredTime.hashCode());
		result = prime * result + ((changeMethod == null) ? 0 : changeMethod.hashCode());
		result = prime * result + ((changePeriod == null) ? 0 : changePeriod.hashCode());
		result = prime * result + ((changeRange == null) ? 0 : changeRange.hashCode());
		result = prime * result + ((changeStartTime == null) ? 0 : changeStartTime.hashCode());
		result = prime * result + ((changeType == null) ? 0 : changeType.hashCode());
		result = prime * result + ((chargingItemId == null) ? 0 : chargingItemId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((namespaceId == null) ? 0 : namespaceId.hashCode());
		result = prime * result + ((periodUnit == null) ? 0 : periodUnit.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
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
		ContractChargingChangeEventDTO other = (ContractChargingChangeEventDTO) obj;
		if (apartments == null) {
			if (other.apartments != null)
				return false;
		} else if (!apartments.equals(other.apartments))
			return false;
		if (changeDurationDays == null) {
			if (other.changeDurationDays != null)
				return false;
		} else if (!changeDurationDays.equals(other.changeDurationDays))
			return false;
		if (changeExpiredTime == null) {
			if (other.changeExpiredTime != null)
				return false;
		} else if (!changeExpiredTime.equals(other.changeExpiredTime))
			return false;
		if (changeMethod == null) {
			if (other.changeMethod != null)
				return false;
		} else if (!changeMethod.equals(other.changeMethod))
			return false;
		if (changePeriod == null) {
			if (other.changePeriod != null)
				return false;
		} else if (!changePeriod.equals(other.changePeriod))
			return false;
		if (changeRange == null) {
			if (other.changeRange != null)
				return false;
		} else if (!((changeRange.compareTo(other.changeRange))==0))
			return false;
		if (changeStartTime == null) {
			if (other.changeStartTime != null)
				return false;
		} else if (!changeStartTime.equals(other.changeStartTime))
			return false;
		if (changeType == null) {
			if (other.changeType != null)
				return false;
		} else if (!changeType.equals(other.changeType))
			return false;
		if (chargingItemId == null) {
			if (other.chargingItemId != null)
				return false;
		} else if (!chargingItemId.equals(other.chargingItemId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (namespaceId == null) {
			if (other.namespaceId != null)
				return false;
		} else if (!namespaceId.equals(other.namespaceId))
			return false;
		if (periodUnit == null) {
			if (other.periodUnit != null)
				return false;
		} else if (!periodUnit.equals(other.periodUnit))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
    
    
}
