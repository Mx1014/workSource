package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>0: INACTIVE</li>
 *  <li>1: WAITING_FOR_APPROVAL</li>
 *  <li>2: REVIEWED</li>
 *  <li>3: DELETE</li>
 * </ul>
 */
public enum EquipmentReviewStatus {
	INACTIVE((byte)0, "已失效"), WAITING_FOR_APPROVAL((byte)1, "待审核"), REVIEWED((byte)2, "已审核"), DELETE((byte)3, "已删除");
	
	private byte code;
	private String name;
	
	private EquipmentReviewStatus(byte code, String name){
		this.code = code;
		this.name = name;
	}
	
	public byte getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public static EquipmentReviewStatus fromStatus(byte code) {
		for(EquipmentReviewStatus v : EquipmentReviewStatus.values()) {
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
