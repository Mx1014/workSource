package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>NONE : 0</li>
 *	<li>WAITING_FOR_EXECUTING : 1 待执行</li>
 *	<li>NEED_MAINTENANCE : 2 需维修</li>
 *	<li>IN_MAINTENANCE : 3 待维修</li>
 *	<li>CLOSE : 4 关闭</li>
 *	<li>DELAY : 5 已过期</li>
 * </ul>
 */
public enum EquipmentTaskStatus {
	NONE((byte)0, ""), WAITING_FOR_EXECUTING((byte)1, "待执行"), NEED_MAINTENANCE((byte)2, "需维修"), 
	IN_MAINTENANCE((byte)3, "待维修"), CLOSE((byte)4, "关闭"), DELAY((byte)5, "已过期");
	
	private byte code;
	private String name;
	
	private EquipmentTaskStatus(byte code, String name){
		this.code = code;
		this.name = name;
	}
	
	public byte getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public static EquipmentTaskStatus fromStatus(byte code) {
		for(EquipmentTaskStatus v : EquipmentTaskStatus.values()) {
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
