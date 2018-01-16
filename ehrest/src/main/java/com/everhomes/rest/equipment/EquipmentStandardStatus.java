package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 	<li>INACTIVE : 0 无效</li>
 *	<li>NOT_COMPLETED : 1 未完成  上一版根据是否有执行周期来判断是否完成 V3.0.2取消</li>
 *	<li>ACTIVE : 2 正常</li>
 * </ul>
 *
 */
public enum EquipmentStandardStatus {
INACTIVE((byte)0), NOT_COMPLETED((byte)1), ACTIVE((byte)2);
	
	private byte code;
	
	private EquipmentStandardStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static EquipmentStandardStatus fromStatus(byte code) {
		for(EquipmentStandardStatus v : EquipmentStandardStatus.values()) {
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
