package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>NONE : 0</li>
 *	<li>COMPLETE_OK : 1 巡检完成</li>
 *	<li>COMPLETE_DELAY : 2 巡检延迟</li>
 *	<li>COMPLETE_MAINTENANCE_OK : 3 维修完成</li>
 *	<li>COMPLETE_MAINTENANCE_DELAY : 4 维修延迟</li>
 * </ul>
 */
public enum EquipmentTaskResult {
	NONE((byte)0), COMPLETE_OK((byte)1), COMPLETE_DELAY((byte)2), COMPLETE_MAINTENANCE_OK((byte)3),
	COMPLETE_MAINTENANCE_DELAY((byte)4);
	
	private byte code;
	
	private EquipmentTaskResult(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static EquipmentTaskResult fromStatus(byte code) {
		for(EquipmentTaskResult v : EquipmentTaskResult.values()) {
			if(v.getCode() == code)
				return v;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
