package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>NONE : 0</li>
 *	<li>COMPLETE_OK : 1 巡检完成</li>
 *	<li>COMPLETE_DELAY : 2 巡检延迟</li>
 *	<li>NEED_MAINTENANCE_OK : 3 需维修完成</li>
 *	<li>NEED_MAINTENANCE_DELAY : 4 需维修延迟</li>
 *	<li>NEED_MAINTENANCE_OK_COMPLETE_OK : 5 需维修完成维修完成</li>
 *	<li>NEED_MAINTENANCE_OK_COMPLETE_DELAY : 6 需维修完成维修延迟</li>
 *	<li>NEED_MAINTENANCE_DELAY_COMPLETE_OK : 7 需维修延迟维修完成</li>
 *	<li>NEED_MAINTENANCE_DELAY_COMPLETE_DELAY : 8 需维修延迟维修延迟</li>
 * </ul>
 */
public enum EquipmentTaskResult {

	NONE((byte)0, ""), COMPLETE_OK((byte)1, "巡检完成"), COMPLETE_DELAY((byte)2, " 巡检延迟"),
	NEED_MAINTENANCE_OK((byte)3, " 需维修完成"), NEED_MAINTENANCE_DELAY((byte)4, "需维修延迟"),
	NEED_MAINTENANCE_OK_COMPLETE_OK((byte)5, "需维修完成维修完成"), NEED_MAINTENANCE_OK_COMPLETE_DELAY((byte)6, "需维修完成维修延迟"),
	NEED_MAINTENANCE_DELAY_COMPLETE_OK((byte)7, "需维修延迟维修完成"), NEED_MAINTENANCE_DELAY_COMPLETE_DELAY((byte)8, "需维修延迟维修延迟");

	private byte code;

	private String name;
	private EquipmentTaskResult(byte code, String name){
		this.code = code;
		this.name = name;
	}
	
	public byte getCode() {
		return code;
	}
	
	public String getName() {
		return name;
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
