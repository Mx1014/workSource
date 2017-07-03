package com.everhomes.rest.organization;

public enum AuthFlag {

	ALL(0), YES(1), NO(2);

	private Integer code;

	private AuthFlag(Integer code){
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public static AuthFlag fromCode(Integer code) {
		if(code != null) {
			AuthFlag[] values = AuthFlag.values();
			for(AuthFlag value : values) {
				if(code.byteValue() == value.code) {
					return value;
				}
			}
		}
		return null;
	}
	

}
