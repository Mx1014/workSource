package com.everhomes.rest.organization;

public enum ExecutiveFlag {

	NO((byte)0), YES((byte)1);

	private Byte code;

	private ExecutiveFlag(Byte code){
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	public static ExecutiveFlag fromCode(Byte code) {
		if(code != null) {
			ExecutiveFlag[] values = ExecutiveFlag.values();
			for(ExecutiveFlag value : values) {
				if(code.byteValue() == value.code) {
					return value;
				}
			}
		}
		return null;
	}
	

}
