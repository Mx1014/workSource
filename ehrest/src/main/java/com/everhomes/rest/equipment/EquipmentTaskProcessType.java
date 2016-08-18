package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 	<li>NONE : 0</li>
 *	<li>COMPLETE : 1 巡检完成</li>
 *	<li>COMPLETE_MAINTENANCE : 2 维修完成</li>
 *	<li>REVIEW : 3 审阅</li>
 *	<li>NEED_MAINTENANCE : 4 需维修</li>
 * </ul>
 */
public enum EquipmentTaskProcessType {
	NONE((byte)0), COMPLETE((byte)1), COMPLETE_MAINTENANCE((byte)2), REVIEW((byte)3),
	NEED_MAINTENANCE((byte)4);
	
	private byte code;
	
	private EquipmentTaskProcessType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static EquipmentTaskProcessType fromStatus(byte code) {
		for(EquipmentTaskProcessType v : EquipmentTaskProcessType.values()) {
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
