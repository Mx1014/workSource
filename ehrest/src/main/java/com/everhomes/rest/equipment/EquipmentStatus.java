package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>0: inactive 已删除</li>
 *  <li>1: incomplete(不完整)</li>
 *  <li>2: in use(使用中)</li>
 *  <li>3: in maintenance(维修中)</li>
 *  <li>4: discarded(报废)</li>
 *  <li>5: disabled(停用)</li>
 *  <li>6: standby(备用)</li>
 * </ul>
 */
public enum EquipmentStatus {

	INACTIVE((byte)0, "已删除"), INCOMPLETE((byte)1, "不完整"), IN_USE((byte)2, "使用中"), IN_MAINTENANCE((byte)3, "维修中"), 
	DISCARDED((byte)4, "报废"), DISABLED((byte)5, "停用"), STANDBY((byte)6, "备用");
	
	private byte code;
	private String name;
	
	private EquipmentStatus(byte code, String name){
		this.code = code;
		this.name = name;
	}
	
	public byte getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public static EquipmentStatus fromStatus(byte code) {
		for(EquipmentStatus v : EquipmentStatus.values()) {
			if(v.getCode() == code)
				return v;
		}
		return null;
	}
	public  static EquipmentStatus fromName(String name){
		for(EquipmentStatus v : EquipmentStatus.values()) {
			if(v.getName().equals(name))
				return v;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
