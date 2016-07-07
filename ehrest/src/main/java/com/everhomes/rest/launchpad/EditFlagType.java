package com.everhomes.rest.launchpad;

/**
 * <ul>
 * <li>NO(可编辑): 0</li>
 * <li>YES(不可编辑): 1</li>
 * </ul>
 */
public enum EditFlagType {
	NO((byte)0),YES((byte)1);
	private byte code;
	private EditFlagType(byte code){
		this.code = code;
	}
	public byte getCode() {
		return code;
	}
	public static EditFlagType fromCode(byte code){
		for(EditFlagType r:EditFlagType.values()){
			if(r.getCode() == code)
				return r;
		}
		return null;
	}

}
