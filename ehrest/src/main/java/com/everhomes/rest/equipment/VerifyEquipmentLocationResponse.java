package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>equipmentId: 设备id</li>
 *  <li>standardId: 标准id</li>
 * </ul>
 */
public class VerifyEquipmentLocationResponse {

	private Long equipmentId;
	
	private Long standardId;
	
	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Long getStandardId() {
		return standardId;
	}

	public void setStandardId(Long standardId) {
		this.standardId = standardId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
