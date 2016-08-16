package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 设备参数id</li>
 *  <li>equipmentId: 设备id</li>
 *  <li>parameterName: 参数名称</li>
 *  <li>parameterUnit: 参数单位</li>
 *  <li>parameterValue: 参数值</li>
 * </ul>
 */
public class EquipmentParameterDTO {
	
	private Long id;
	
	private Long equipmentId;
	
	private String parameterName;
	
	private String parameterUnit;
	
	private String parameterValue;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterUnit() {
		return parameterUnit;
	}

	public void setParameterUnit(String parameterUnit) {
		this.parameterUnit = parameterUnit;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
