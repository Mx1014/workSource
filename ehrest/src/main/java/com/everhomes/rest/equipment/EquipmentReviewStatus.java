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
	INACTIVE((byte)0), WAITING_FOR_APPROVAL((byte)1), REVIEWED((byte)2), DELETE((byte)2);
	
	private byte code;
	
	private EquipmentReviewStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
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
