package com.everhomes.rest.techpark.company;

public enum ContactType {
	MOBILE((byte)0),EMAIL((byte)1);
	private byte code;
	private ContactType(byte code){
		this.code = code;
	}
	public Byte getCode() {
		return code;
	}
	public static ContactType fromCode(byte code){
		for(ContactType r:ContactType.values()){
			if(r.getCode() == code)
				return r;
		}
		return null;
	}
}
