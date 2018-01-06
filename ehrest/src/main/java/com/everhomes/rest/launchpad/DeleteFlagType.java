package com.everhomes.rest.launchpad;

/**
 * <ul>
 * <li>NO(不可删除): 0</li>
 * <li>YES(可删除): 1</li>
 * </ul>
 */
public enum DeleteFlagType {
	NO((byte)0),YES((byte)1);
	private byte code;
	private DeleteFlagType(byte code){
		this.code = code;
	}
	public byte getCode() {
		return code;
	}
	public static DeleteFlagType fromCode(byte code){
		for(DeleteFlagType r:DeleteFlagType.values()){
			if(r.getCode() == code)
				return r;
		}
		return null;
	}

}