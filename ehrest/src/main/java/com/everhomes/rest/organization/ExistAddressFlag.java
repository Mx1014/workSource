package com.everhomes.rest.organization;

public enum ExistAddressFlag {

	NOT_EXIST((byte)0), EXIST((byte)1);

	private byte code;

	private ExistAddressFlag(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static ExistAddressFlag fromCode(Byte code) {
		if(code != null) {
			ExistAddressFlag[] values = ExistAddressFlag.values();
			for(ExistAddressFlag value : values) {
				if(code == value.code) {
					return value;
				}
			}
		}
		return null;
	}
	

}
