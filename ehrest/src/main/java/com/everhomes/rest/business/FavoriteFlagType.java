package com.everhomes.rest.business;

public enum FavoriteFlagType {
	CANCEL_FAVORITE((byte)0),FAVORITE((byte)1);
	private byte code;
	
	private FavoriteFlagType(byte code){
		this.code = code;
	}
	
	public byte getCode(){
		return this.code;
	}
	
	public static FavoriteFlagType fromCode(byte code){
		for(FavoriteFlagType r:FavoriteFlagType.values()){
			if(r.getCode() == code)
				return r;
		}
		return null;
	}
}
