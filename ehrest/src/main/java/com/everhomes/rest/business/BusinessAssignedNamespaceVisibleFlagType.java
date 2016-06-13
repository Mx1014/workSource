package com.everhomes.rest.business;

/**
 * <ul>
 * 	<li>hide:隐藏</li>
 * 	<li>visible:显示</li>
 * </ul>
 */
public enum BusinessAssignedNamespaceVisibleFlagType {
	HIDE((byte)0),
	VISIBLE((byte)1);
	private byte code;
	private BusinessAssignedNamespaceVisibleFlagType(byte code){
		this.code=code;
	}
	public byte getCode() {
		return code;
	}

	public static BusinessAssignedNamespaceVisibleFlagType fromCode(Byte code) {
		if(code == null)
			return null;
		BusinessAssignedNamespaceVisibleFlagType[] values = BusinessAssignedNamespaceVisibleFlagType.values();
		for(BusinessAssignedNamespaceVisibleFlagType value : values){
			if(value.getCode() == code.byteValue())
				return value;
		}
		return null;
	}

}
