package com.everhomes.rest.equipment;

import org.apache.commons.lang.StringUtils;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>EQUIPMENT_STANDARDS("EquipmentStandards"): 设备标准</li>
 *  <li>EQUIPMENTS("Equipments"): routing inspection 设备</li>
 *  <li>EQUIPMENT_ACCESSORIES("EquipmentAccessories"):设备配件</li>
 * </ul>
 */
public enum ImportDataType {

	EQUIPMENT_STANDARDS("EquipmentStandards"), EQUIPMENTS("Equipments"), EQUIPMENT_ACCESSORIES("EquipmentAccessories");
	
	private String code;
	
	private ImportDataType(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static ImportDataType fromCode(String code) {
		for(ImportDataType v : ImportDataType.values()) {
			if(StringUtils.equals(v.getCode(), code))
				return v;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
