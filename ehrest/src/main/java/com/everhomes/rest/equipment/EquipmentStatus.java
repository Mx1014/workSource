package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>0: none</li>
 *  <li>1: incomplete(不完整)</li>
 *  <li>2: in use(使用中)</li>
 *  <li>3: in maintenance(维修中)</li>
 *  <li>4: discarded(报废)</li>
 *  <li>5: disabled(停用)</li>
 *  <li>6: standby(备用)</li>
 * </ul>
 */
public enum EquipmentStatus {

	NONE((byte)0), INCOMPLETE((byte)1), IN_USE((byte)2), IN_MAINTENANCE((byte)3), 
	DISCARDED((byte)4), DISABLED((byte)5), STANDBY((byte)6);
	
	private byte code;
	
	private EquipmentStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static EquipmentStatus fromStatus(byte code) {
		for(EquipmentStatus v : EquipmentStatus.values()) {
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
